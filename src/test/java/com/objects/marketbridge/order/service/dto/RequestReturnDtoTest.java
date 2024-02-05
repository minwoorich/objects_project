//package com.objects.marketbridge.order.service.dto;
//
//import com.objects.marketbridge.member.domain.Coupon;
//import com.objects.marketbridge.member.domain.MembershipType;
//
//import com.objects.marketbridge.product.domain.Product;
//import com.objects.marketbridge.order.domain.OrderDetail;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.tuple;
//
//class RequestReturnDtoTest {
//
//    @Test
//    @DisplayName("주문 상세 리스트와 맴버십이 주어진 경우 Response를 반환한다.(BASIC)")
//    public void response_of_BASIC() {
//        // given
//        String memberShip = MembershipType.BASIC.getText();
//
//        Product product1 = Product.builder()
//                .price(1000L)
//                .name("빵빵이")
//                .thumbImg("빵빵이 이미지")
//                .build();
//        Product product2 = Product.builder()
//                .price(2000L)
//                .name("옥지얌")
//                .thumbImg("옥지얌 이미지")
//                .build();
//
//        Coupon coupon1 = Coupon.builder()
//                .price(1000L)
//                .build();
//        Coupon coupon2 = Coupon.builder()
//                .price(2000L)
//                .build();
//
//        OrderDetail orderDetail1 = OrderDetail.builder()
//                .coupon(coupon1)
//                .product(product1)
//                .price(1000L)
//                .quantity(2L)
//                .build();
//        OrderDetail orderDetail2 = OrderDetail.builder()
//                .coupon(coupon2)
//                .product(product2)
//                .price(2000L)
//                .quantity(2L)
//                .build();
//        List<OrderDetail> orderDetails = List.of(orderDetail1, orderDetail2);
//
//        // when
//        RequestReturnDto.Response result = RequestReturnDto.Response.of(orderDetails, memberShip);
//
//        // then
//        assertThat(result.getProductInfos()).hasSize(2)
//                .extracting("quantity", "name", "price", "image")
//                .contains(
//                        tuple(2L, "빵빵이", 1000L, "빵빵이 이미지"),
//                        tuple(2L, "옥지얌", 2000L, "옥지얌 이미지")
//                );
//
//        assertThat(result.getReturnRefundInfo())
//                .extracting("deliveryFee", "returnFee", "productTotalPrice")
//                .contains(3000L, 1000L, 6000L);
//
//    }
//
//    @Test
//    @DisplayName("주문 상세 리스트와 맴버십이 주어진 경우 Response를 반환한다.(WOW)")
//    public void response_of_WOW() {
//        // given
//        String memberShip = MembershipType.WOW.getText();
//
//        Product product1 = Product.builder()
//                .price(1000L)
//                .name("빵빵이")
//                .thumbImg("빵빵이 이미지")
//                .build();
//        Product product2 = Product.builder()
//                .price(2000L)
//                .name("옥지얌")
//                .thumbImg("옥지얌 이미지")
//                .build();
//
//        Coupon coupon1 = Coupon.builder()
//                .price(1000L)
//                .build();
//        Coupon coupon2 = Coupon.builder()
//                .price(2000L)
//                .build();
//
//        OrderDetail orderDetail1 = OrderDetail.builder()
//                .coupon(coupon1)
//                .product(product1)
//                .price(1000L)
//                .quantity(2L)
//                .build();
//        OrderDetail orderDetail2 = OrderDetail.builder()
//                .coupon(coupon2)
//                .product(product2)
//                .price(2000L)
//                .quantity(2L)
//                .build();
//        List<OrderDetail> orderDetails = List.of(orderDetail1, orderDetail2);
//
//        // when
//        RequestReturnDto.Response result = RequestReturnDto.Response.of(orderDetails, memberShip);
//
//        // then
//        assertThat(result.getProductInfos()).hasSize(2)
//                .extracting("quantity", "name", "price", "image")
//                .contains(
//                        tuple(2L, "빵빵이", 1000L, "빵빵이 이미지"),
//                        tuple(2L, "옥지얌", 2000L, "옥지얌 이미지")
//                );
//
//        assertThat(result.getReturnRefundInfo())
//                .extracting("deliveryFee", "returnFee", "productTotalPrice")
//                .contains(0L, 0L, 6000L);
//
//    }
//
//    @Test
//    @DisplayName("주문 상세가 주어질 경우 ProductInfo를 반환한다.")
//    public void productInfo_of() {
//        // given
//        Product product = Product.builder()
//                .price(1000L)
//                .name("빵빵이")
//                .thumbImg("빵빵이 이미지")
//                .build();
//
//        Coupon coupon = Coupon.builder()
//                .price(1000L)
//                .build();
//
//        OrderDetail orderDetail = OrderDetail.builder()
//                .coupon(coupon)
//                .product(product)
//                .price(1000L)
//                .quantity(2L)
//                .build();
//
//        // when
//        RequestReturnDto.ProductInfo result = RequestReturnDto.ProductInfo.of(orderDetail);
//
//        // then
//        assertThat(result.getQuantity()).isEqualTo(2L);
//        assertThat(result.getName()).isEqualTo("빵빵이");
//        assertThat(result.getPrice()).isEqualTo(1000L);
//        assertThat(result.getImage()).isEqualTo("빵빵이 이미지");
//    }
//
//    @Test
//    @DisplayName("주문 상세 리스트와 맴버십이 주어진 경우 ReturnRefundInfo를 반환한다.(BASIC)")
//    public void returnRefundInfo_of_BASIC() {
//        // given
//        String memberShip = MembershipType.BASIC.getText();
//
//        OrderDetail orderDetail1 = OrderDetail.builder()
//                .price(1000L)
//                .quantity(2L)
//                .build();
//        OrderDetail orderDetail2 = OrderDetail.builder()
//                .price(2000L)
//                .quantity(2L)
//                .build();
//        List<OrderDetail> orderDetails = List.of(orderDetail1, orderDetail2);
//
//        // when
//        RequestReturnDto.ReturnRefundInfo result = RequestReturnDto.ReturnRefundInfo.of(orderDetails, memberShip);
//
//        // then
//        assertThat(result)
//                .extracting("deliveryFee", "returnFee", "productTotalPrice")
//                .contains(3000L, 1000L, 6000L);
//    }
//
//    @Test
//    @DisplayName("주문 상세 리스트와 맴버십이 주어진 경우 ReturnRefundInfo를 반환한다.(WOW)")
//    public void returnRefundInfo_of_WOW() {
//        // given
//        String memberShip = MembershipType.WOW.getText();
//
//        OrderDetail orderDetail1 = OrderDetail.builder()
//                .price(1000L)
//                .quantity(2L)
//                .build();
//        OrderDetail orderDetail2 = OrderDetail.builder()
//                .price(2000L)
//                .quantity(2L)
//                .build();
//        List<OrderDetail> orderDetails = List.of(orderDetail1, orderDetail2);
//
//        // when
//        RequestReturnDto.ReturnRefundInfo result = RequestReturnDto.ReturnRefundInfo.of(orderDetails, memberShip);
//
//        // then
//        assertThat(result)
//                .extracting("deliveryFee", "returnFee", "productTotalPrice")
//                .contains(0L, 0L, 6000L);
//    }
//}