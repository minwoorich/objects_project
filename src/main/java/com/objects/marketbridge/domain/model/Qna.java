package com.objects.marketbridge.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Qna extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    // TODO
//    @AttributeOverride()  : 중복 클래스 적용
    private Long userCustomerId; //userId
    // TODO
    // @AttributeOverride()
    private Long userSellerId; //userId

    @Enumerated(EnumType.STRING)
    private ContentType boardType;

    private String content;

    @Builder
    private Qna(Long userCustomerId, Long userSellerId, ContentType boardType, String content) {
        this.userCustomerId = userCustomerId;
        this.userSellerId = userSellerId;
        this.boardType = boardType;
        this.content = content;
    }
}
