package com.objects.marketbridge.domain.member.dto;


import com.objects.marketbridge.domain.model.Membership;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.objects.marketbridge.domain.model.Membership.BASIC;

@Getter
@NoArgsConstructor
public class CreateMemberDto {
    @NotBlank(message = "이메일 필수값 입니다.")
    @Email(message = "이메일 양식에 맞게 작성하셔야합니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 6, message = "비밀번호는 최소 6자리 이상이여야합니다.")
    private String password;

    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    @NotBlank(message = "핸드폰 번호는 필수입니다.")
    @Pattern(regexp = "\\d{3}\\d{3,4}\\d{4}", message = "핸드폰번호는 - 없이 번호만 입력하셔야합니다.")
    private String phoneNo;

    private Boolean isAgree;

    private Membership membership;

    @Builder
    public CreateMemberDto(String email, String password, String name, String phoneNo, Boolean isAgree, Membership membership) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNo = phoneNo;
        this.isAgree = isAgree;
        this.membership = (membership != null) ? membership : BASIC; // 기본값 설정
    }

}
