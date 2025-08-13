package com.example.userservice.application.service;

import com.example.userservice.application.command.ChangePasswordCommand;
import com.example.userservice.application.command.LoginCommand;
import com.example.userservice.application.command.RegisterCommand;
import com.example.userservice.application.exception.BusinessException;
import com.example.userservice.shared.CommonResponse;

public interface AuthService {

    CommonResponse login(LoginCommand loginCommand) throws Exception;

    CommonResponse register(RegisterCommand registerCommand);

    CommonResponse forgotPassword(String email);

    CommonResponse changePassword(ChangePasswordCommand changePasswordCommand) throws BusinessException;
}
