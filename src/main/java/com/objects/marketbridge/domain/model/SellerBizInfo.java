package com.objects.marketbridge.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SellerBizInfo extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "seller_biz_info_id")
    private Long id;
    // TODO
    private Long userId;

    private String name;

    private String bizNo;

    private String owner;
    //업태
    private String category;
    //종목
    private String detail;
    // 사업장 주소
    private String address;
    // 통신 판매업 번호
    private String licenseNo;

    private String email;

    private String accountNo;

    @Builder
    private SellerBizInfo(Long userId, String name, String bizNo, String owner, String category, String detail, String address, String licenseNo, String email, String accountNo) {
        this.userId = userId;
        this.name = name;
        this.bizNo = bizNo;
        this.owner = owner;
        this.category = category;
        this.detail = detail;
        this.address = address;
        this.licenseNo = licenseNo;
        this.email = email;
        this.accountNo = accountNo;
    }
}

