package com.example.userservice.application.exception;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BusinessException extends Exception {

    private static final long serialVersionUID = -2556322490055461261L;

    /** The code. */
    int code;

    public List<BusinessError> errors = new ArrayList<>();

    public BusinessException(String errorMessage) {
        super(errorMessage);
        this.errors.add(new BusinessError(errorMessage));
    }

    public BusinessException(BusinessError error) {
        this.errors.add(error);
    }

    public BusinessException(List<BusinessError> errors) {
        this.errors = errors;
    }

    public BusinessException(String errorMessage, Throwable err) {
        super(errorMessage, err);
        this.errors.add(new BusinessError(errorMessage));
    }

    /**
     * Instantiates a new business exception.
     *
     * @param message the message
     * @param code the code
     */
    public BusinessException(String message, int code) {
        super(message);
        this.code = code;
    }

}
