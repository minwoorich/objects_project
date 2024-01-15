package com.objects.marketbridge.domain.model;

import com.objects.marketbridge.domain.member.dto.CreateMember;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String socialType;

    private String membership;

    private String email;

    private String password;

    private String name;

    private String phoneNo;

    // 알림
    private Boolean isAlert;
    // 약관동의
    private Boolean isAgree;


    // 양방향 설정
    @OneToMany(mappedBy = "member" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    @OneToOne(mappedBy = "member" , cascade = CascadeType.ALL, orphanRemoval = true)
    private Point point;

    @Builder
    private Member(String socialType, String membership, String email, String password, String name, String phoneNo, Boolean isAlert, Boolean isAgree) {
        this.socialType = socialType;
        this.membership = membership;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNo = phoneNo;
        this.isAlert = isAlert;
        this.isAgree = isAgree;
    }

    public void changePoint(Point point) {
        this.point = point;
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

    public static Member fromDto(CreateMember createMember){
        return Member.builder()
                .email(createMember.getEmail())
                .name(createMember.getName())
                .phoneNo(createMember.getPhoneNo())
                .password(createMember.getPassword())
                .isAgree(createMember.getIsAgree()).build();
    }

}


