package com.objects.marketbridge.domain.model;

import com.objects.marketbridge.domain.order.domain.ProdOrderDetail;
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
public class Product extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category categoryId;

    @OneToMany(mappedBy = "product")
    private List<ProdOrderDetail> prodOrderDetails = new ArrayList<>();
//
//    @OneToMany(mappedBy = "product")
//    private List<ProdOption> prodOptions = new ArrayList<>();

    private String productNo;

    private boolean isOwn; // 로켓 true , 오픈 마켓 false

    private String name;

    private Long price;

    private Boolean isSubs;

    private String thumbImg;

    private Long discountRate;

    private Long stock;

    @Builder
    public Product(Category categoryId, boolean isOwn, String name, Long price, boolean isSubs, String thumbImg, Long discountRate, Long stock) {
        this.categoryId = categoryId;
        this.isOwn = isOwn;
        this.name = name;
        this.price = price;
        this.isSubs = isSubs;
        this.thumbImg = thumbImg;
        this.discountRate = discountRate;
        this.stock = stock;
    }

    public void addProdOrderDetail(ProdOrderDetail prodOrderDetail) {
        prodOrderDetails.add(prodOrderDetail);
        prodOrderDetail.setProduct(this);
    }

//    public void addProdOption(ProdOption prodOption) {
//        prodOptions.add(prodOption);
//        prodOption.setProduct(this);
//    }
}
