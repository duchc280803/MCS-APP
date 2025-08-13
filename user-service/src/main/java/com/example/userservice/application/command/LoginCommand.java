package com.example.userservice.application.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

public class LoginCommand {

    @Getter
    private String username;

    private String password;

    /**
     * Instantiates a new admin user detail.
     *
     * @param username the loginId
     * @param password the password
     */
    public LoginCommand(@JsonProperty("username") String username, @JsonProperty("password") String password) {
        this.username = username.trim();
        this.password = password.trim();
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public Optional<String> getPassword() {
        return Optional.ofNullable(password).map(p -> new BCryptPasswordEncoder().encode(p));
    }

    public Optional<String> getPasswordLogin() {
        return Optional.ofNullable(password);
    }

    /**
     * To authentication token.
     *
     * @return the username password authentication token
     */
    public UsernamePasswordAuthenticationToken toAuthenticationToken() {
        return new UsernamePasswordAuthenticationToken(username, password);
    }
}
