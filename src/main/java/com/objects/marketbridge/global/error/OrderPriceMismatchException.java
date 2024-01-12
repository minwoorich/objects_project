package com.objects.marketbridge.global.error;

public class OrderPriceMismatchException extends RuntimeException {
    public OrderPriceMismatchException(String message) {
        super(message);
    }
}
