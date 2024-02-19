package com.objects.marketbridge.order.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CancelReturnStatusCode {

    private String previousStatusCode;
    private String statusCode;

    @Builder
    public CancelReturnStatusCode(String previousStatusCode, String statusCode) {
        this.previousStatusCode = previousStatusCode;
        this.statusCode = statusCode;
    }
}
