package com.objects.marketbridge.order.service.dto;

import com.objects.marketbridge.order.domain.OrderDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class RequestCancelDto {

    @Getter
    @NoArgsConstructor
    public static class Response {
        private List<ProductInfoResponseDto> productInfoResponseDtos;
        private CancelRefundInfoResponseDto cancelRefundInfoResponseDto;

        @Builder
        private Response(List<ProductInfoResponseDto> productInfoResponseDtos, CancelRefundInfoResponseDto cancelRefundInfoResponseDto) {
            this.productInfoResponseDtos = productInfoResponseDtos;
            this.cancelRefundInfoResponseDto = cancelRefundInfoResponseDto;
        }

        public static Response of(List<OrderDetail> orderDetails, String memberShip) {
            return Response.builder()
                    .productInfoResponseDtos(orderDetails.stream()
                            // TODO Product로 인해 N+1 문제 발생 예상 (of)
                            .map(ProductInfoResponseDto::of)
                            .toList()
                    )
                    .cancelRefundInfoResponseDto(
                            // TODO coupon 으로 인해 N+1문제 발생할 것으로 예상 (of) -> fetchJoin으로 쿠폰까지 조인후 해결
                            CancelRefundInfoResponseDto.of(orderDetails, memberShip)
                    )
                    .build();
        }
    }
}
