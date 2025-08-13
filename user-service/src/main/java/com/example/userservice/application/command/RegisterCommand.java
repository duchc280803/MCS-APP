package com.example.userservice.application.command;

import com.example.userservice.domain.entity.user.User;
import com.example.userservice.shared.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterCommand {

    private String email;

    private String username;

    private String fullName;

    private String password;

    public static User toEntity(RegisterCommand registerCommand) {
        return User.builder()
                .id(UUID.randomUUID().toString())
                .username(registerCommand.getUsername())
                .fullName(registerCommand.getFullName())
                .email(registerCommand.getEmail())
                .password(BCrypt.hashpw(registerCommand.getPassword(), BCrypt.gensalt()))
                .passwordExpiredAt(OffsetDateTime.now().plusDays(Constants.PASSWORD_EXPIRE_DAYS))
                .build();
    }

}
