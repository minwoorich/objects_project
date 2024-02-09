package com.objects.marketbridge.order.controller.dto;

import com.objects.marketbridge.order.infra.dtio.OrderDtio;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

public class GetOrderHttp {

    @Getter
    @NoArgsConstructor
    public static class Response {
        List<OrderInfo> orderInfos;

        @Builder
        private Response(List<OrderInfo> orderInfos) {
            this.orderInfos = orderInfos;
        }

        public static Response of(List<OrderDtio> orderDtios) {
            return Response.builder()
                    .orderInfos(orderDtios.stream().map(OrderInfo::of).collect(Collectors.toList()))
                    .build();
        }

        public static Response create(List<OrderInfo> orderInfos) {
            return Response.builder()
                    .orderInfos(orderInfos)
                    .build();
        }

    }

    @Getter
    @NoArgsConstructor
    public static class Condition {
        private String keyword;
        private String year;
        private Long memberId;
        private Boolean isSearch;

        @Builder
        public Condition(String keyword, String year, Long memberId, Boolean isSearch) {
            this.keyword = keyword;
            this.year = year;
            this.memberId = memberId;
            this.isSearch = isSearch;
        }
    }

}
