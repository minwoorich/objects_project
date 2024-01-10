package com.objects.marketbridge.domain.user.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateUser {
   private String email;
   private String password;
   private String name;
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
