package com.objects.marketbridge.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HelpDesk extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "help_desk_id")
    private Long id;

    // TODO
    private Long orderId;
    // TODO
    private Long userId;
    // TODO
    private Long productId;

    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    private String content;

    @Builder
    private HelpDesk(Long orderId, Long userId, Long productId, ContentType contentType, String content) {
        this.orderId = orderId;
        this.userId = userId;
        this.productId = productId;
        this.contentType = contentType;
        this.content = content;
    }
}
