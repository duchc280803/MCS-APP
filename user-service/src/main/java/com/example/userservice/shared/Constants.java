package com.example.userservice.shared;

import io.jsonwebtoken.lang.Arrays;

import java.util.List;

public class Constants {

    public static final List<String> WHITE_LIST_API = Arrays.asList(new String[]{
            "/webapi/master", "/webapi/user", "/change-password", "/webapi/area-office", "/user/reset-password"
    });

    public static final int PASSWORD_EXPIRE_DAYS = 365;

    public static final int PASSWORD_RESET_MAIL_EXPIRED = 24;

}
