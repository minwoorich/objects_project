package com.objects.marketbridge.global.error;

public class DataVerificationFailedException extends RuntimeException {
    public DataVerificationFailedException(String message) {
        super(message);
    }
}
