package com.example.userservice.shared;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommonResponse {

    public static final int CODE_SUCCESS = 200; // OK
    public static final int CODE_PERMISSION_DENIED = 403; // FORBIDDEN
    public static final int CODE_NOT_FOUND = 404; // NOT_FOUND
    public static final int CODE_ALREADY_EXIST = 409; // CONFLICT
    public static final int CODE_BUSINESS_ERROR = 400; // BAD_REQUEST
    public static final int CODE_ACCOUNT_EXCEPTION = 423; // LOCKED
    public static final int CODE_LIKE_OLD_PASSWORD = 406; // NOT_ACCEPTABLE
    public static final int CODE_PASSWORD_EXPIRED = 419; // SESSION_EXPIRED (custom)
    public static final int CODE_DATA_IS_DELETED = 410; // GONE
    public static final int CODE_ERROR_SERVER = 500; // SERVER
    public static final int CODE_EMAIL_ALREADY_EXIST = 7;
    public static final int CODE_EMAIL_AND_ID_ALREADY_EXIST = 8;

    @JsonProperty("code")
    @Builder.Default
    private Integer code = CODE_SUCCESS;

    @JsonProperty("message")
    @Builder.Default
    private String message = "";

    @JsonProperty("data")
    @Builder.Default
    private Object data = null;

    @JsonProperty("totalPage")
    private Integer totalPage;
}
