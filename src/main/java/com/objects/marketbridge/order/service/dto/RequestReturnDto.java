package com.objects.marketbridge.order.service.dto;

import com.objects.marketbridge.common.domain.MembershipType;
import com.objects.marketbridge.order.domain.OrderDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

import static com.objects.marketbridge.order.domain.MemberShipPrice.BASIC;
import static com.objects.marketbridge.order.domain.MemberShipPrice.WOW;

public class RequestReturnDto {

    @Getter
    @NoArgsConstructor
    public static class Response {
        private List<ProductInfoResponseDto> productInfoResponseDtos;
        private ReturnRefundInfoResponseDto returnRefundInfoResponseDto;

        @Builder
        private Response(List<ProductInfoResponseDto> productInfoResponseDtos, ReturnRefundInfoResponseDto returnRefundInfoResponseDto) {
            this.productInfoResponseDtos = productInfoResponseDtos;
            this.returnRefundInfoResponseDto = returnRefundInfoResponseDto;
        }

        public static Response of(List<OrderDetail> orderDetails, String memberType) {
            return Response.builder()
                    .productInfoResponseDtos(
                            orderDetails.stream()
                                    .map(ProductInfoResponseDto::of)
                                    .toList()
                    )
                    .returnRefundInfoResponseDto(
                            ReturnRefundInfoResponseDto.of(orderDetails, memberType)
                    )
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ProductInfoResponseDto {
        private Long quantity;
        private String name;
        private Long price;
        private String image; // TODO 주문 취소 요청 이미지 반환

        @Builder
        private ProductInfoResponseDto(Long quantity, String name, Long price, String image) {
            this.quantity = quantity;
            this.name = name;
            this.price = price;
            this.image = image;
        }

        public static ProductInfoResponseDto of(OrderDetail orderDetail) {
            return ProductInfoResponseDto.builder()
                    .quantity(orderDetail.getQuantity())
                    .name(orderDetail.getProduct().getName())
                    .price(orderDetail.getProduct().getPrice())
                    .image(orderDetail.getProduct().getThumbImg())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ReturnRefundInfoResponseDto {
        private Long deliveryFee;
        private Long returnFee;
        private Long productTotalPrice;

        @Builder
        private ReturnRefundInfoResponseDto(Long deliveryFee, Long returnFee, Long productTotalPrice) {
            this.deliveryFee = deliveryFee;
            this.returnFee = returnFee;
            this.productTotalPrice = productTotalPrice;
        }

        public static ReturnRefundInfoResponseDto of(List<OrderDetail> orderDetails, String memberShip) {
            if (isBasicMember(memberShip)) {
                return createDto(orderDetails, BASIC.getDeliveryFee(), BASIC.getReturnFee());
            }
            return createDto(orderDetails, WOW.getDeliveryFee(), WOW.getReturnFee());
        }

        private static boolean isBasicMember(String memberShip) {
            return Objects.equals(memberShip, MembershipType.BASIC.getText());
        }

        private static ReturnRefundInfoResponseDto createDto(List<OrderDetail> orderDetails, Long deliveryFee, Long refundFee) {
            return ReturnRefundInfoResponseDto.builder()
                    .deliveryFee(deliveryFee)
                    .returnFee(refundFee)
                    .productTotalPrice(
                            orderDetails.stream()
                                    .mapToLong(orderDetail -> orderDetail.getPrice() * orderDetail.getQuantity())
                                    .sum()
                    )
                    .build();
        }
    }

}
