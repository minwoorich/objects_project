package com.objects.marketbridge.domains.order.controller.dto.select;

import com.objects.marketbridge.domains.order.service.dto.GetOrderDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class GetOrderHttp {

    @Getter
    @NoArgsConstructor
    public static class Response {

        private String orderNo;
        private String createdAt;// yyyy-MM-dd HH:mm:ss
        private List<OrderDetailInfo> orderDetailInfos;

        @Builder
        public Response(String createdAt, String orderNo, List<OrderDetailInfo> orderDetailInfos) {
            this.createdAt = createdAt;
            this.orderNo = orderNo;
            this.orderDetailInfos = orderDetailInfos;
        }

        public static Response of(GetOrderDto getOrderDto) {
            return Response.builder()
                    .createdAt(getOrderDto.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .orderNo(getOrderDto.getOrderNo())
                    .orderDetailInfos(getOrderDto.getOrderDetails().stream().map(OrderDetailInfo::of).collect(Collectors.toList()))
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Condition {
        private String keyword;
        private String year;
        private Long memberId;

        @Builder
        public Condition(String keyword, String year, Long memberId) {
            this.keyword = keyword;
            this.year = year;
            this.memberId = memberId;
        }
    }
}
