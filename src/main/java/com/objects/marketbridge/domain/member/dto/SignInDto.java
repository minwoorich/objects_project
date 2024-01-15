package com.objects.marketbridge.domain.member.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class SignInDto {
    private String email;
    private String password;
}
