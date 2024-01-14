package com.objects.marketbridge.domain.model;

import com.objects.marketbridge.domain.member.dto.CreateMemberDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.Optional;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Enumerated(EnumType.STRING)
    @Setter
    private Membership membership;

    private String email;

    private String password;

    private String name;

    private String phoneNo;

    @OneToMany(mappedBy = "member" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    @OneToOne(mappedBy = "member" , cascade = CascadeType.ALL, orphanRemoval = true)
    private Point point;

    // 알림
    private boolean isAlert;
    // 약관동의
    private boolean isAgree;

    @Builder
    private Member(SocialType socialType, Membership membership, String email, String password, String name, String phoneNo, boolean isAlert, boolean isAgree) {
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

    public static Member fromDto(CreateMemberDto createMemberDto){
        return Member.builder()
                .email(createMemberDto.getEmail())
                .name(createMemberDto.getName())
                .phoneNo(createMemberDto.getPhoneNo())
                .password(createMemberDto.getPassword())
                .isAgree(createMemberDto.getIsAgree())
                .membership(createMemberDto.getMembership()).build();
    }

    public CreateMemberDto toDto(Optional<Member> member){
        return CreateMemberDto.builder()
                .email(email)
                .name(name)
                .phoneNo(phoneNo)
                .password(password)
                .isAgree(isAgree)
                .membership(membership).build();
    }
}


