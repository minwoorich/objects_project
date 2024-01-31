package com.objects.marketbridge.order.infra.dtio;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class CancelReturnResponseDao {

    private LocalDateTime cancelReceiptDate;
    private LocalDateTime orderDate;
    private String orderNo;
    private List<DetailResponseDao> detailResponseDaos = new ArrayList<>();

    @Builder
    @QueryProjection
    public CancelReturnResponseDao(LocalDateTime cancelReceiptDate, LocalDateTime orderDate, String orderNo) {
        this.cancelReceiptDate = cancelReceiptDate;
        this.orderDate = orderDate;
        this.orderNo = orderNo;
    }

    public void changeDetailResponsDaos(List<DetailResponseDao> detailResponseDaos) {
        if (detailResponseDaos == null) {
            throw new IllegalArgumentException("주어진 주문 상세 리스트가 존재하지 않습니다.");
        }
        this.detailResponseDaos = detailResponseDaos;
    }

}
