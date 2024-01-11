package com.objects.marketbridge.domain.user.dto;


import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateUser {
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "이메일 필수값 입니다.")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Password should have at least 6 characters")
    private String password;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Pattern(regexp = "\\d{3}-\\d{3,4}-\\d{4}", message = "Phone number should have the format XXX-XXXX-XXXX")
    private String phoneNo;

    private Boolean isAgree;


    @Builder
    public CreateUser(String email, String password, String name, String phoneNo, Boolean isAgree) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNo = phoneNo;
        this.isAgree = isAgree;
    }
}
