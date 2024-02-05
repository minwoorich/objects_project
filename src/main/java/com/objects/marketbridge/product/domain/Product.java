package com.objects.marketbridge.product.domain;

import com.objects.marketbridge.category.domain.Category;
import com.objects.marketbridge.member.domain.BaseEntity;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.common.exception.exceptions.CustomLogicException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.objects.marketbridge.common.exception.exceptions.ErrorCode.OUT_OF_STOCK;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product")
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<ProdOption> prodOptions = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<ProductImage> productImages = new ArrayList<>();

    private Boolean isOwn; // 로켓 true , 오픈 마켓 false

    private String name;

    private Long price;

    private Boolean isSubs;

    private Long stock;

    private String thumbImg;

    private Long discountRate;

    private String productNo;

    @Builder
    private Product(Category category, Boolean isOwn, String name, Long price, Boolean isSubs, Long stock, String thumbImg, Long discountRate,String productNo) {
        this.category = category;
        this.isOwn = isOwn; // 로켓 true , 오픈 마켓 false
        this.name = name;
        this.price = price;
        this.isSubs = isSubs;
        this.thumbImg = thumbImg;
        this.discountRate = discountRate;
        this.stock = stock;
        this.productNo = productNo;
    }

    public static Product create(Category category, Boolean isOwn, String name, Long price, Boolean isSubs, Long stock, String thumbImg, Long discountRate, String productNo){
        return Product.builder()
                .category(category)
                .isOwn(isOwn)
                .name(name)
                .price(price)
                .isSubs(isSubs)
                .stock(stock)
                .thumbImg(thumbImg)
                .discountRate(discountRate)
                .productNo(productNo)
                .build();
    }

    public void addProductImages(ProductImage productImage){
        productImages.add(productImage);
        productImage.setProduct(this);
    }

    public void addProdOptions(ProdOption prodOption){
        prodOptions.add(prodOption);
        prodOption.setProduct(this);
    }

    public Product update(Category category, Boolean isOwn, String name, Long price,
                          Boolean isSubs, Long stock, String thumbImg, Long discountRate,
                          String productNo ) {
        this.category = category;
        this.isOwn = isOwn; // 로켓 true , 오픈 마켓 false
        this.name = name;
        this.price = price;
        this.isSubs = isSubs;
        this.stock = stock;
        this.thumbImg = thumbImg;
        this.discountRate = discountRate;
        this.productNo = productNo;

        return this;
    }

    public void addOrderDetail(OrderDetail orderDetail) {
        orderDetails.add(orderDetail);
        orderDetail.setProduct(this);
    }

    public void increase(Long quantity) {
        stock += quantity;
    }

    public void decrease(Long quantity) {
        verifyStockAvailable(quantity);
        stock -= quantity;
    }

    private void verifyStockAvailable(Long quantity) {
        if (stock - quantity < 0) {
            throw CustomLogicException.builder()
                    .errorCode(OUT_OF_STOCK)
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("재고가 부족합니다")
                    .timestamp(LocalDateTime.now())
                    .build();
        }
    }
}
