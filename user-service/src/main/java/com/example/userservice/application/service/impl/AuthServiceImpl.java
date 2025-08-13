package com.example.userservice.application.service.impl;

import com.example.userservice.application.command.ChangePasswordCommand;
import com.example.userservice.application.command.LoginCommand;
import com.example.userservice.application.command.RegisterCommand;
import com.example.userservice.application.query.AuthTokenResponse;
import com.example.userservice.application.exception.BusinessException;
import com.example.userservice.application.service.AuthService;
import com.example.userservice.domain.entity.role.Role;
import com.example.userservice.domain.entity.role.UserRole;
import com.example.userservice.domain.entity.user.Member;
import com.example.userservice.domain.enums.RsTokenStatus;
import com.example.userservice.domain.entity.user.PasswordResetToken;
import com.example.userservice.domain.entity.user.User;
import com.example.userservice.infrastructure.repository.MemberRepository;
import com.example.userservice.infrastructure.repository.PwResetTokenRepository;
import com.example.userservice.infrastructure.repository.UserRepository;
import com.example.userservice.infrastructure.repository.UserRoleRepository;
import com.example.userservice.infrastructure.security.auth.JwtService;
import com.example.userservice.infrastructure.security.auth.UserDetail;
import com.example.userservice.shared.CommonResponse;
import com.example.userservice.shared.Constants;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final PwResetTokenRepository pwResetTokenRepository;
    private final MemberRepository memberRepository;

    @Override
    public CommonResponse login(LoginCommand loginCommand) throws Exception {
        User activeUser = userRepository.findByUsernameAndIsDeleted(loginCommand.getUsername(), false).orElse(null);
        if (activeUser == null) {
            return CommonResponse.builder()
                    .data(new AuthTokenResponse(null))
                    .build();
        }

        String rawPassword = loginCommand.getPasswordLogin().get();
        String encodedPassword = activeUser.getPassword();

        if (!BCrypt.checkpw(rawPassword, encodedPassword)) {
            return CommonResponse.builder()
                    .data(new AuthTokenResponse(null))
                    .build();
        }

//        if (activeUser.getPasswordExpiredAt() != null && activeUser.getPasswordExpiredAt().isBefore(OffsetDateTime.now())) {
//            return CommonResponse.builder()
//                    .code(CommonResponse.CODE_PASSWORD_EXPIRED)
//                    .data(activeUser.getId())
//                    .build();
//        }

        this.authenticationManager.authenticate(loginCommand.toAuthenticationToken());

        userRepository.save(activeUser);
        String token = jwtService.generateToken(UserDetail.fromEntity(activeUser));
        return CommonResponse.builder()
                .data(new AuthTokenResponse(token))
                .build();
    }

    @Override
    public CommonResponse register(RegisterCommand registerCommand) {
        User entity = RegisterCommand.toEntity(registerCommand);

        User userMailCheck = userRepository.findFirstByEmailAndIsDeleted(registerCommand.getEmail(), false).orElse(null);

        User userCheck = userRepository.findByUsernameAndIsDeleted(registerCommand.getUsername(), false).orElse(null);

        if (userCheck != null && userMailCheck != null && Strings.isNotEmpty(registerCommand.getEmail())) {
            return CommonResponse.builder()
                    .code(CommonResponse.CODE_EMAIL_AND_ID_ALREADY_EXIST)
                    .message("Mail and id already exists")
                    .build();
        } else if (userMailCheck != null && Strings.isNotEmpty(registerCommand.getEmail())) {
            return CommonResponse.builder()
                    .code(CommonResponse.CODE_EMAIL_ALREADY_EXIST)
                    .message("Mail already exists")
                    .build();
        } else if (userCheck != null) {
            return CommonResponse.builder()
                    .code(CommonResponse.CODE_ALREADY_EXIST)
                    .message("User already exists")
                    .build();
        }

        UserRole userRole = userRoleRepository.findByName(Role.ROLE_CUSTOMER.name()).orElse(null);

        if (userRole == null) {
            return CommonResponse.builder()
                    .code(CommonResponse.CODE_NOT_FOUND)
                    .build();
        }

        entity.setPasswordExpiredAt(OffsetDateTime.now().minusDays(1));
        userRepository.saveAndFlush(entity);

        Member member = new Member();
        member.setMemberId(UUID.randomUUID().toString());
        member.setUser(entity);
        member.setRole(userRole);
        memberRepository.saveAndFlush(member);

        return CommonResponse.builder()
                .code(CommonResponse.CODE_SUCCESS)
                .build();
    }

    @Override
    public CommonResponse forgotPassword(String email) {
        List<User> users = userRepository.findByEmail(email);
        if (users.isEmpty()) {
            return CommonResponse.builder()
                    .code(CommonResponse.CODE_NOT_FOUND)
                    .message("User email not found")
                    .build();
        }
        String userId = users.getFirst().getId();
        PasswordResetToken token = new PasswordResetToken();
        PasswordResetToken currentToken = pwResetTokenRepository.findByUserId(userId).orElse(null);
        if (currentToken != null) {
            token = currentToken;
            token.setToken(RandomStringUtils.randomNumeric(6).toLowerCase(Locale.ROOT));
            token.setExpireTime(OffsetDateTime.now().plusHours(Constants.PASSWORD_RESET_MAIL_EXPIRED));
        } else {
            token.setToken(RandomStringUtils.randomNumeric(6).toLowerCase(Locale.ROOT));
            token.setExpireTime(OffsetDateTime.now().plusHours(Constants.PASSWORD_RESET_MAIL_EXPIRED));
            token.setUserId(userId);
        }

        token.setStatus(RsTokenStatus.WAITING);
        this.pwResetTokenRepository.save(token);

        return CommonResponse.builder()
                .code(CommonResponse.CODE_SUCCESS)
                .data(true)
                .build();
    }

    @Override
    public CommonResponse changePassword(ChangePasswordCommand changePasswordCommand) throws BusinessException {
        User user;
        if (StringUtils.isEmpty(changePasswordCommand.getUsername())) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserDetail userDetail = (UserDetail) auth.getPrincipal();
            user = userRepository.findByUsernameAndIsDeleted(userDetail.getLoginId(), false).orElse(null);
        } else {
            user = userRepository.findByUsernameAndIsDeleted(changePasswordCommand.getUsername(), false).orElse(null);
        }

        if (user == null) {
            return CommonResponse.builder()
                    .code(CommonResponse.CODE_NOT_FOUND)
                    .message("User not found")
                    .build();
        }

        if (!this.passwordEncoder.matches(changePasswordCommand.getOldPassword(), user.getPassword())) {
            return CommonResponse.builder()
                    .code(CommonResponse.CODE_ACCOUNT_EXCEPTION)
                    .build();
        }

        if (!StringUtils.equals(changePasswordCommand.getNewPassword(), changePasswordCommand.getNewPasswordConfirm())) {
            return CommonResponse.builder()
                    .code(CommonResponse.CODE_BUSINESS_ERROR)
                    .build();
        }

        user.setPassword(BCrypt.hashpw(changePasswordCommand.getNewPassword(), BCrypt.gensalt()));
        user.setPasswordExpiredAt(OffsetDateTime.now().plusDays(Constants.PASSWORD_EXPIRE_DAYS));
        userRepository.save(user);
        return CommonResponse.builder()
                .code(CommonResponse.CODE_SUCCESS)
                .message("Password changed")
                .build();
    }
}
