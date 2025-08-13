package com.example.userservice.application.exception;

import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Builder
@NoArgsConstructor
public class BusinessError {

    public String message;
    public Map<String, String> values;

    public BusinessError(String message) {
        this.message = message;
        this.values = new HashMap<>();
    }

    public BusinessError(String message, Map<String, String> values) {
        this.message = message;
        this.values = values;
    }

}