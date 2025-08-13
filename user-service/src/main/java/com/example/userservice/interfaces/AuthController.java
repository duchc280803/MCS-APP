package com.example.userservice.interfaces;

import com.example.userservice.application.command.ChangePasswordCommand;
import com.example.userservice.application.command.LoginCommand;
import com.example.userservice.application.command.RegisterCommand;
import com.example.userservice.application.service.AuthService;
import com.example.userservice.shared.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("login")
    public CommonResponse login(@RequestBody LoginCommand loginCommand) throws Exception {
        return authService.login(loginCommand);
    }

    @PostMapping("register")
    public CommonResponse register(@RequestBody RegisterCommand registerCommand) throws Exception {
        return authService.register(registerCommand);
    }

    @PostMapping("change-password")
    public CommonResponse changePassword(@RequestBody ChangePasswordCommand changePasswordCommand) throws Exception {
        return authService.changePassword(changePasswordCommand);
    }

    @PostMapping("forgot-password/{email}")
    public CommonResponse forgotPassword(@PathVariable("email") String email) throws Exception {
        return authService.forgotPassword(email);
    }
}
