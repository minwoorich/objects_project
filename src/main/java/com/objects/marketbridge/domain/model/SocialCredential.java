package com.objects.marketbridge.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
    // TODO
    private Long userId;

    private String tokenId;

    @Builder
    private SocialCredential(Long userId, String tokenId) {
        this.userId = userId;
        this.tokenId = tokenId;
    }
}
