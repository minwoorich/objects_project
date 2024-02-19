package com.objects.marketbridge.product.domain;

import com.objects.marketbridge.image.domain.Image;
import com.objects.marketbridge.member.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image image;

    //String으로 할것. 예시 : ImageType.ITEM_IMG.toString()
    private String type;

    private Long seqNo;

    @Builder
    private ProductImage(Product product, Image image, Long seqNo) {
        this.product = product;
        this.image = image;
        this.seqNo = seqNo;
    }

    public void setProduct(Product product) { this.product = product; }

    public void setImage(Image image){ this.image = image; }

    public static ProductImage create(Product product, Image image, Long seqNo){
        return ProductImage.builder()
                .product(product)
                .image(image)
                .seqNo(seqNo)
                .build();
    }

}

