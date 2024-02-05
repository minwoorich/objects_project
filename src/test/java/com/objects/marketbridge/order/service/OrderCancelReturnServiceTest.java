package com.objects.marketbridge.order.service;

import com.objects.marketbridge.member.domain.Coupon;
import com.objects.marketbridge.common.service.port.DateTimeHolder;
import com.objects.marketbridge.mock.BaseFakeOrderDetailRepository;
import com.objects.marketbridge.mock.TestContainer;
import com.objects.marketbridge.mock.TestDateTimeHolder;
import com.objects.marketbridge.order.domain.MemberShipPrice;
import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.order.domain.StatusCodeType;
import com.objects.marketbridge.order.service.dto.ConfirmCancelReturnDto;
import com.objects.marketbridge.order.service.dto.GetCancelReturnDetailDto;
import com.objects.marketbridge.order.service.dto.RequestCancelDto;
import com.objects.marketbridge.order.service.dto.RequestReturnDto;
import com.objects.marketbridge.product.domain.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static com.objects.marketbridge.member.domain.MembershipType.BASIC;
import static com.objects.marketbridge.member.domain.MembershipType.WOW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class OrderCancelReturnServiceTest {

    private OrderCancelReturnService orderCancelReturnService;
    LocalDateTime cancelDate = LocalDateTime.of(2024, 1, 31, 9, 26);

    @BeforeEach
    void beforeEach() {
        TestContainer testContainer = TestContainer.builder()
                .dateTimeHolder(TestDateTimeHolder.builder()
                        .now(cancelDate)
                        .build())
                .build();
        this.orderCancelReturnService = testContainer.orderCancelReturnService;

        Product product1 = Product.builder()
                .name("빵빵이키링")
                .productNo("1")
                .price(1000L)
                .thumbImg("빵빵이썸네일")
                .stock(5L)
                .build();
        Product product2 = Product.builder()
                .name("옥지얌키링")
                .productNo("2")
                .price(2000L)
                .thumbImg("옥지얌썸네일")
                .stock(5L)
                .build();

        Coupon coupon1 = Coupon.builder()
                .name("빵빵이키링쿠폰")
                .product(product1)
                .price(1000L)
                .count(10L)
                .build();
        Coupon coupon2 = Coupon.builder()
                .name("옥지얌키링쿠폰")
                .product(product2)
                .price(2000L)
                .count(10L)
                .build();

        Order order = Order.builder()
                .orderNo("1")
                .tid("1")
                .totalDiscount(0L)
                .totalPrice(8000L)
                .realPrice(8000L)
                .build();

        LocalDateTime cancelledAt = LocalDateTime.of(2024, 1, 31, 3, 33);
        OrderDetail orderDetail1 = OrderDetail.builder()
                .cancelledAt(cancelledAt)
                .quantity(2L)
                .product(product1)
                .price(1000L)
                .coupon(coupon1)
                .order(order)
                .reason("단순변심")
                .statusCode(StatusCodeType.ORDER_RECEIVED.getCode())
                .tid("1")
                .build();
        OrderDetail orderDetail2 = OrderDetail.builder()
                .cancelledAt(cancelledAt)
                .quantity(3L)
                .product(product2)
                .price(2000L)
                .coupon(coupon2)
                .order(order)
                .reason("단순변심")
                .statusCode(StatusCodeType.DELIVERY_ING.getCode())
                .tid("1")
                .build();

        order.addOrderDetail(orderDetail1);
        order.addOrderDetail(orderDetail2);

        testContainer.productRepository.save(product1);
        testContainer.productRepository.save(product2);
        testContainer.orderDetailCommendRepository.save(orderDetail1);
        testContainer.orderDetailCommendRepository.save(orderDetail2);
        testContainer.orderCommendRepository.save(order);
    }

    @AfterEach
    void afterEach() {
        BaseFakeOrderDetailRepository.getInstance().clear();
    }

    @Test
    @DisplayName("취소/반품 확정")
    public void confirmCancelReturn() {
        // given
        ConfirmCancelReturnDto.Request request = ConfirmCancelReturnDto.Request.builder()
                .orderNo("1")
                .cancelReason("단순변심")
                .build();

        LocalDateTime updateTime = LocalDateTime.of(2024, 1, 31, 6, 7);
        DateTimeHolder dateTimeHolder = TestDateTimeHolder.builder()
                .updateTime(updateTime)
                .build();

        // when
        ConfirmCancelReturnDto.Response result = orderCancelReturnService.confirmCancelReturn(request, dateTimeHolder);

        // then
        assertThat(result.getOrderId()).isEqualTo(1L);
        assertThat(result.getOrderNo()).isEqualTo("1");
        assertThat(result.getTotalPrice()).isEqualTo(8000L);
        assertThat(result.getCancellationDate()).isEqualTo(updateTime);
        assertThat(result.getRefundInfo())
                .extracting("totalRefundAmount", "refundMethod", "refundProcessedAt")
                .contains(8000L, "카드", cancelDate);
        assertThat(result.getCancelledItems()).hasSize(2)
                .extracting("productId", "productNo", "name", "price", "quantity")
                .contains(
                        tuple(1L, "1", "빵빵이키링", 1000L, 2L),
                        tuple(2L, "2", "옥지얌키링", 2000L, 3L)
                );
    }

    @Test
    @DisplayName("WOW회원 일때 취소 요청")
    public void requestCancel1() {
        // given
        String orderNo = "1";
        List<Long> productIds = List.of(1L, 2L);
        String membership = WOW.getText();

        // when
        RequestCancelDto.Response result = orderCancelReturnService.findCancelInfo(orderNo, productIds, membership);
        RequestCancelDto.CancelRefundInfo cancelRefundInfo = result.getCancelRefundInfo();
        List<RequestCancelDto.ProductInfo> productInfos = result.getProductInfos();

        // then
        assertThat(cancelRefundInfo.getDeliveryFee()).isEqualTo(MemberShipPrice.WOW.getDeliveryFee());
        assertThat(cancelRefundInfo.getRefundFee()).isEqualTo(MemberShipPrice.WOW.getRefundFee());
        assertThat(cancelRefundInfo.getDiscountPrice()).isEqualTo(3000L);
        assertThat(cancelRefundInfo.getTotalPrice()).isEqualTo(8000L);

        assertThat(productInfos.get(0).getQuantity()).isEqualTo(2L);
        assertThat(productInfos.get(0).getName()).isEqualTo("빵빵이키링");
        assertThat(productInfos.get(0).getPrice()).isEqualTo(1000L);
        assertThat(productInfos.get(0).getImage()).isEqualTo("빵빵이썸네일");

        assertThat(productInfos.get(1).getQuantity()).isEqualTo(3L);
        assertThat(productInfos.get(1).getName()).isEqualTo("옥지얌키링");
        assertThat(productInfos.get(1).getPrice()).isEqualTo(2000L);
        assertThat(productInfos.get(1).getImage()).isEqualTo("옥지얌썸네일");

    }

    @Test
    @DisplayName("BASIC회원 일때 취소 요청")
    public void requestCancel2() {
        // given
        String orderNo = "1";
        List<Long> productIds = List.of(1L, 2L);
        String membership = BASIC.getText();

        // when
        RequestCancelDto.Response result = orderCancelReturnService.findCancelInfo(orderNo, productIds, membership);
        RequestCancelDto.CancelRefundInfo cancelRefundInfo = result.getCancelRefundInfo();
        List<RequestCancelDto.ProductInfo> productInfos = result.getProductInfos();

        // then
        assertThat(cancelRefundInfo.getDeliveryFee()).isEqualTo(MemberShipPrice.BASIC.getDeliveryFee());
        assertThat(cancelRefundInfo.getRefundFee()).isEqualTo(MemberShipPrice.BASIC.getRefundFee());
        assertThat(cancelRefundInfo.getDiscountPrice()).isEqualTo(3000L);
        assertThat(cancelRefundInfo.getTotalPrice()).isEqualTo(8000L);

        assertThat(productInfos.get(0).getQuantity()).isEqualTo(2L);
        assertThat(productInfos.get(0).getName()).isEqualTo("빵빵이키링");
        assertThat(productInfos.get(0).getPrice()).isEqualTo(1000L);
        assertThat(productInfos.get(0).getImage()).isEqualTo("빵빵이썸네일");

        assertThat(productInfos.get(1).getQuantity()).isEqualTo(3L);
        assertThat(productInfos.get(1).getName()).isEqualTo("옥지얌키링");
        assertThat(productInfos.get(1).getPrice()).isEqualTo(2000L);
        assertThat(productInfos.get(1).getImage()).isEqualTo("옥지얌썸네일");

    }

    @Test
    @DisplayName("WOW회원 일때 반품 요청")
    public void requestReturn1() {
        // given
        String orderNo = "1";
        List<Long> productIds = List.of(1L, 2L);
        String membership = WOW.getText();

        // when
        RequestReturnDto.Response result = orderCancelReturnService.findReturnInfo(orderNo, productIds, membership);
        RequestReturnDto.ReturnRefundInfo returnRefundInfo = result.getReturnRefundInfo();
        List<RequestReturnDto.ProductInfo> productInfos = result.getProductInfos();

        // then
        assertThat(returnRefundInfo.getDeliveryFee()).isEqualTo(MemberShipPrice.WOW.getDeliveryFee());
        assertThat(returnRefundInfo.getReturnFee()).isEqualTo(MemberShipPrice.WOW.getRefundFee());
        assertThat(returnRefundInfo.getProductTotalPrice()).isEqualTo(8000L);

        assertThat(productInfos.get(0).getQuantity()).isEqualTo(2L);
        assertThat(productInfos.get(0).getName()).isEqualTo("빵빵이키링");
        assertThat(productInfos.get(0).getPrice()).isEqualTo(1000L);
        assertThat(productInfos.get(0).getImage()).isEqualTo("빵빵이썸네일");

        assertThat(productInfos.get(1).getQuantity()).isEqualTo(3L);
        assertThat(productInfos.get(1).getName()).isEqualTo("옥지얌키링");
        assertThat(productInfos.get(1).getPrice()).isEqualTo(2000L);
        assertThat(productInfos.get(1).getImage()).isEqualTo("옥지얌썸네일");
    }

    @Test
    @DisplayName("BASIC회원 일때 반품 요청")
    public void requestReturn2() {
        // given
        String orderNo = "1";
        List<Long> productIds = List.of(1L, 2L);
        String membership = BASIC.getText();

        // when
        RequestReturnDto.Response result = orderCancelReturnService.findReturnInfo(orderNo, productIds, membership);
        RequestReturnDto.ReturnRefundInfo returnRefundInfo = result.getReturnRefundInfo();
        List<RequestReturnDto.ProductInfo> productInfos = result.getProductInfos();

        // then
        assertThat(returnRefundInfo.getDeliveryFee()).isEqualTo(MemberShipPrice.BASIC.getDeliveryFee());
        assertThat(returnRefundInfo.getReturnFee()).isEqualTo(MemberShipPrice.BASIC.getRefundFee());
        assertThat(returnRefundInfo.getProductTotalPrice()).isEqualTo(8000L);

        assertThat(productInfos.get(0).getQuantity()).isEqualTo(2L);
        assertThat(productInfos.get(0).getName()).isEqualTo("빵빵이키링");
        assertThat(productInfos.get(0).getPrice()).isEqualTo(1000L);
        assertThat(productInfos.get(0).getImage()).isEqualTo("빵빵이썸네일");

        assertThat(productInfos.get(1).getQuantity()).isEqualTo(3L);
        assertThat(productInfos.get(1).getName()).isEqualTo("옥지얌키링");
        assertThat(productInfos.get(1).getPrice()).isEqualTo(2000L);
        assertThat(productInfos.get(1).getImage()).isEqualTo("옥지얌썸네일");
    }

    @Test
    @DisplayName("WOW회원 일때 취소/반품 상세 정보")
    public void findCancelReturnDetail1() {
        // given
        String orderNo = "1";
        List<Long> productIds = List.of(1L, 2L);
        String membership = WOW.getText();

        LocalDateTime orderDateTime = LocalDateTime.of(2024, 1, 31, 6, 7);
        LocalDateTime cancelDateTime = LocalDateTime.of(2024, 1, 31, 11, 17);
        DateTimeHolder dateTimeHolder = TestDateTimeHolder.builder()
                .createTime(orderDateTime)
                .updateTime(cancelDateTime)
                .build();

        // when
        GetCancelReturnDetailDto.Response result = orderCancelReturnService.findCancelReturnDetail(orderNo, productIds, membership, dateTimeHolder);

        // then
        assertThat(result.getOrderDate()).isEqualTo(orderDateTime);
        assertThat(result.getCancelDate()).isEqualTo(cancelDateTime);
        assertThat(result.getOrderNo()).isEqualTo(orderNo);
        assertThat(result.getCancelReason()).isEqualTo("단순변심");

        assertThat(result.getCancelRefundInfo().getDeliveryFee()).isEqualTo(MemberShipPrice.WOW.getDeliveryFee());
        assertThat(result.getCancelRefundInfo().getRefundFee()).isEqualTo(MemberShipPrice.WOW.getRefundFee());
        assertThat(result.getCancelRefundInfo().getDiscountPrice()).isEqualTo(3000L);
        assertThat(result.getCancelRefundInfo().getTotalPrice()).isEqualTo(8000L);

        assertThat(result.getProductInfos().get(0).getProductId()).isEqualTo(1L);
        assertThat(result.getProductInfos().get(0).getProductNo()).isEqualTo("1");
        assertThat(result.getProductInfos().get(0).getName()).isEqualTo("빵빵이키링");
        assertThat(result.getProductInfos().get(0).getPrice()).isEqualTo(1000L);
        assertThat(result.getProductInfos().get(0).getQuantity()).isEqualTo(2L);
        assertThat(result.getProductInfos().get(1).getProductId()).isEqualTo(2L);
        assertThat(result.getProductInfos().get(1).getProductNo()).isEqualTo("2");
        assertThat(result.getProductInfos().get(1).getName()).isEqualTo("옥지얌키링");
        assertThat(result.getProductInfos().get(1).getPrice()).isEqualTo(2000L);
        assertThat(result.getProductInfos().get(1).getQuantity()).isEqualTo(3L);
    }

    @Test
    @DisplayName("BASIC회원 일때 취소/반품 상세 정보")
    public void findCancelReturnDetail2() {
        // given
        String orderNo = "1";
        List<Long> productIds = List.of(1L, 2L);
        String membership = BASIC.getText();

        LocalDateTime orderDateTime = LocalDateTime.of(2024, 1, 31, 6, 7);
        LocalDateTime cancelDateTime = LocalDateTime.of(2024, 1, 31, 11, 17);
        DateTimeHolder dateTimeHolder = TestDateTimeHolder.builder()
                .createTime(orderDateTime)
                .updateTime(cancelDateTime)
                .build();

        // when
        GetCancelReturnDetailDto.Response result = orderCancelReturnService.findCancelReturnDetail(orderNo, productIds, membership, dateTimeHolder);

        // then
        assertThat(result.getOrderDate()).isEqualTo(orderDateTime);
        assertThat(result.getCancelDate()).isEqualTo(cancelDateTime);
        assertThat(result.getOrderNo()).isEqualTo(orderNo);
        assertThat(result.getCancelReason()).isEqualTo("단순변심");

        assertThat(result.getCancelRefundInfo().getDeliveryFee()).isEqualTo(MemberShipPrice.BASIC.getDeliveryFee());
        assertThat(result.getCancelRefundInfo().getRefundFee()).isEqualTo(MemberShipPrice.BASIC.getRefundFee());
        assertThat(result.getCancelRefundInfo().getDiscountPrice()).isEqualTo(3000L);
        assertThat(result.getCancelRefundInfo().getTotalPrice()).isEqualTo(8000L);

        assertThat(result.getProductInfos().get(0).getProductId()).isEqualTo(1L);
        assertThat(result.getProductInfos().get(0).getProductNo()).isEqualTo("1");
        assertThat(result.getProductInfos().get(0).getName()).isEqualTo("빵빵이키링");
        assertThat(result.getProductInfos().get(0).getPrice()).isEqualTo(1000L);
        assertThat(result.getProductInfos().get(0).getQuantity()).isEqualTo(2L);
        assertThat(result.getProductInfos().get(1).getProductId()).isEqualTo(2L);
        assertThat(result.getProductInfos().get(1).getProductNo()).isEqualTo("2");
        assertThat(result.getProductInfos().get(1).getName()).isEqualTo("옥지얌키링");
        assertThat(result.getProductInfos().get(1).getPrice()).isEqualTo(2000L);
        assertThat(result.getProductInfos().get(1).getQuantity()).isEqualTo(3L);
    }

}