package com.objects.marketbridge.domain.payment.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class VirtualAccount {
    private String vAccountNo; // 발급된 계좌번호
    private String vDueDate; // 입금 기한 yyyy-MM-dd'T'HH:mm:ss (ISO8601)
    private Boolean vExpired; //가상계좌 만료 여부
}
