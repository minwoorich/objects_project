package com.objects.marketbridge.domain.model;

import com.objects.marketbridge.domain.user.dto.CreateUser;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Enumerated(EnumType.STRING)
    private Membership membership;

    private String email;

    private String password;

    private String name;

    private String phoneNo;

    private String birth;
    // 알림
    private boolean isAlert;
    // 약관동의
    private boolean isAgree;

    @Builder
    private User(SocialType socialType, Membership membership, String email, String password, String name, String phoneNo, String birth, boolean isAlert, boolean isAgree) {
        this.socialType = socialType;
        this.membership = membership;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNo = phoneNo;
        this.birth = birth;
        this.isAlert = isAlert;
        this.isAgree = isAgree;
    }

//    public CreateUser toDto(){
//
//        return CreateUser.builder()
//                .email()
//                .name()
//                .phoneNo()
//                .password()
//                .isAgree();
//    }

    public static User fromDto(CreateUser createUser){
        return User.builder()
                .email(createUser.getEmail())
                .name(createUser.getName())
                .phoneNo(createUser.getPhoneNo())
                .password(createUser.getPassword())
                .isAgree(createUser.getIsAgree()).build();
    }

}


