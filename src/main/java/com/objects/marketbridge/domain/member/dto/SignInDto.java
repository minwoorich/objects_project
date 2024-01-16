package com.objects.marketbridge.domain.member.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class SignInDto {
    @NotBlank()
    @Email()
    private String email;

    @NotBlank()
    private String password;
}
