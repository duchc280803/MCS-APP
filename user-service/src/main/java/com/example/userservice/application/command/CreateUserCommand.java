package com.example.userservice.application.command;

import com.example.userservice.domain.entity.user.User;
import com.example.userservice.shared.Constants;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.time.OffsetDateTime;

@Data
@Builder
public class CreateUserCommand {

    private String id;

    private String fullName;

    private String profileImg;

    private String email;

    private String telNo;

    private OffsetDateTime lastActive;

    private String note;

    private OffsetDateTime applyAt;

    private OffsetDateTime expiredAt;

    private String password;

    private String username;

    public static User toEntity(CreateUserCommand createUserCommand) {
        return User.builder()
                .id(createUserCommand.getId())
                .username(createUserCommand.getUsername())
                .fullName(createUserCommand.getFullName() == null ? null : createUserCommand.getFullName().trim())
                .profileImage(createUserCommand.getProfileImg())
                .email(createUserCommand.getEmail())
                .telNo(createUserCommand.getTelNo())
                .note(createUserCommand.getNote())
                .password(BCrypt.hashpw(createUserCommand.getPassword(), BCrypt.gensalt()))
                .passwordExpiredAt(OffsetDateTime.now().plusDays(Constants.PASSWORD_EXPIRE_DAYS))
                .build();
    }

}
