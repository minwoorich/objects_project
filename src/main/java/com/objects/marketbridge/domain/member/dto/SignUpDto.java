package com.objects.marketbridge.domain.member.dto;


import com.objects.marketbridge.common.domain.Member;
import com.objects.marketbridge.common.domain.Membership;
import com.objects.marketbridge.common.domain.SocialType;
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

    static public SignUpDto toDto(Member member) {
        return SignUpDto.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .name(member.getName())
                .phoneNo(member.getPhoneNo())
                .isAgree(member.getIsAgree())
                .build();
    }

    public Member toEntity(String encodedPassword) {
        return Member.builder()
                .email(email)
                .name(name)
                .password(encodedPassword)
                .phoneNo(phoneNo)
                .isAgree(isAgree)
                .isAlert(true)
                .membership(Membership.BASIC.toString())
                .socialType(SocialType.DEFAULT.toString())
                .build();
    }
}
