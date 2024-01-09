package com.objects.marketbridge.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 소셜정보
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialCredential extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "social_credential_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userId;

    private String tokenId;

    @Builder
    private SocialCredential(User userId, String tokenId) {
        this.userId = userId;
        this.tokenId = tokenId;
    }
}
