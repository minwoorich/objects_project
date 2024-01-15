package com.objects.marketbridge.domain.model;

import com.objects.marketbridge.domain.member.dto.CreateMember;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

//    @Enumerated(EnumType.STRING)
    private String socialType;

//    @Enumerated(EnumType.STRING)
    private String membership;

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
    private Member(String socialType, String membership, String email, String password, String name, String phoneNo, boolean isAlert, boolean isAgree, List<String> roles) {
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


