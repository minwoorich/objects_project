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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prod_order_id")
    private ProdOrder prodOrderId;
    //private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product productId;

    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    private String content;

    @Builder
    private HelpDesk(ProdOrder prodOrderId, User userId, Product productId, ContentType contentType, String content) {
        this.prodOrderId = prodOrderId;
        this.userId = userId;
        this.productId = productId;
        this.contentType = contentType;
        this.content = content;
    }
}
