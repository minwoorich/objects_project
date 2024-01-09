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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userId; //userId

    // @AttributeOverride()

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Seller sellerId; //userId

    @Enumerated(EnumType.STRING)
    private ContentType boardType;

    private String content;

    @Builder
    private Qna(User userId, Seller sellerId, ContentType boardType, String content) {
        this.userId = userId;
        this.sellerId = sellerId;
        this.boardType = boardType;
        this.content = content;
    }
}
