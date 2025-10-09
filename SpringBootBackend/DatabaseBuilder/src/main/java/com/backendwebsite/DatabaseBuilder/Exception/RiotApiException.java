package com.backendwebsite.DatabaseBuilder.Exception;

public class RiotApiException extends RuntimeException {
    public RiotApiException(String message) {
        super(message);
    }

    public RiotApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
