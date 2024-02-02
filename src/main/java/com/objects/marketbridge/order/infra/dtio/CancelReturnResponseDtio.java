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
public class CancelReturnResponseDtio {

    private LocalDateTime cancelReceiptDate;
    private LocalDateTime orderDate;
    private String orderNo;
    private List<DetailResponseDtio> detailResponseDtios = new ArrayList<>();

    @Builder
    @QueryProjection
    public CancelReturnResponseDtio(LocalDateTime cancelReceiptDate, LocalDateTime orderDate, String orderNo) {
        this.cancelReceiptDate = cancelReceiptDate;
        this.orderDate = orderDate;
        this.orderNo = orderNo;
    }

    public void changeDetailResponsDaos(List<DetailResponseDtio> detailResponseDtios) {
        if (detailResponseDtios == null) {
            throw new IllegalArgumentException("주어진 주문 상세 리스트가 존재하지 않습니다.");
        }
        this.detailResponseDtios = detailResponseDtios;
    }

}
