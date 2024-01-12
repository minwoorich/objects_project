package com.objects.marketbridge.domain.order.dto;

import lombok.Getter;

@Getter

public class MemberDto {

    private String email;
    private String password;
    private String name;
    private String phoneNo;

    // 알림
    private boolean isAlert;
    // 약관동의
    private boolean isAgree;        
}
