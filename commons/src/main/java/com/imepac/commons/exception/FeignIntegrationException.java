package com.imepac.commons.exception;

public class FeignIntegrationException extends RuntimeException {

    public FeignIntegrationException(String message) {
        super(message);
    }

    public FeignIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
