package com.example.userservice.application.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordCommand {

    private String username;

    private String oldPassword;

    private String newPassword;

    private String newPasswordConfirm;

    private String resetToken;
}
