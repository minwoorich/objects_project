package com.objects.marketbridge.domain.payment.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Transfer {
    private String bankCode;
    private String settlementStatus; // 정산상태 INCOMPLETED, COMPLETED
}
