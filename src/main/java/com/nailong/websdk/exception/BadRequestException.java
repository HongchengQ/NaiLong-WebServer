package com.nailong.websdk.exception;

public class BadRequestException extends CommonException{

    public static final int ERROR_CODE = 100400;

    public BadRequestException(String message) {
        super(message, ERROR_CODE);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause, ERROR_CODE);
    }

    public BadRequestException(Throwable cause) {
        super(cause, ERROR_CODE);
    }
}
