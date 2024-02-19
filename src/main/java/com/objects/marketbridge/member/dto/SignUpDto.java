package com.objects.marketbridge.member.dto;


import com.objects.marketbridge.member.domain.Member;
import com.objects.marketbridge.member.domain.MembershipType;
import jakarta.validation.constraints.*;
import lombok.*;
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SignUpDto {

    @NotBlank()
    @Email()
    private String email;

    @NotBlank()
    @Size(min=4)
    private String password;

    @NotBlank()
    private String name;

    @NotBlank()
    @Size(min = 11)
    private String phoneNo;

    private Boolean isAgree;

    public Member toEntity(String encodedPassword) {
        return Member.builder()
                .email(email)
                .name(name)
                .password(encodedPassword)
                .phoneNo(phoneNo)
                .isAgree(isAgree)
                .isAlert(true)
                .membership(MembershipType.BASIC.toString())
                .build();
    }
}
