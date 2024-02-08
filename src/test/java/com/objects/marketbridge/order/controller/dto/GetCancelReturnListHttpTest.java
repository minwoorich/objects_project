package com.objects.marketbridge.order.controller.dto;

import com.objects.marketbridge.order.infra.dtio.GetCancelReturnListDtio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

import static com.objects.marketbridge.order.domain.StatusCodeType.ORDER_CANCEL;
import static org.assertj.core.api.Assertions.assertThat;

class GetCancelReturnListHttpTest {

    @Test
    @DisplayName("dtio.Response 주어지면 Http.Response 변환한다.")
    public void response_of() {
        // given
        LocalDateTime cancelReceiptDate = LocalDateTime.of(2024, 2, 8, 4, 59);
        LocalDateTime orderDate = LocalDateTime.of(2024, 2, 8, 4, 58);

        GetCancelReturnListDtio.Response response = GetCancelReturnListDtio.Response.builder()
                .cancelReceiptDate(cancelReceiptDate)
                .orderDate(orderDate)
                .orderNo("1")
                .build();

        GetCancelReturnListDtio.OrderDetailInfo orderDetailInfo1 = GetCancelReturnListDtio.OrderDetailInfo.builder()
                .orderNo("1")
                .productId(1L)
                .productNo("1")
                .name("빵빵이키링")
                .price(1000L)
                .quantity(2L)
                .orderStatus(ORDER_CANCEL.getCode())
                .build();
        GetCancelReturnListDtio.OrderDetailInfo orderDetailInfo2 = GetCancelReturnListDtio.OrderDetailInfo.builder()
                .orderNo("1")
                .productId(2L)
                .productNo("2")
                .name("옥지얌키링")
                .price(2000L)
                .quantity(3L)
                .orderStatus(ORDER_CANCEL.getCode())
                .build();
        List<GetCancelReturnListDtio.OrderDetailInfo> orderDetailInfos = List.of(orderDetailInfo1, orderDetailInfo2);

        response.changeOrderDetailInfos(orderDetailInfos);

        // when
        GetCancelReturnListHttp.Response result = GetCancelReturnListHttp.Response.of(response);

        // then
        assertThat(result.getCancelReceiptDate()).isEqualTo(cancelReceiptDate);
        assertThat(result.getOrderDate()).isEqualTo(orderDate);
        assertThat(result.getOrderNo()).isEqualTo("1");
        assertThat(result.getOrderDetailInfos()).hasSize(2);
        assertThat(result.getOrderDetailInfos().get(0).getOrderNo()).isEqualTo("1");
        assertThat(result.getOrderDetailInfos().get(0).getProductId()).isEqualTo(1L);
        assertThat(result.getOrderDetailInfos().get(0).getProductNo()).isEqualTo("1");
        assertThat(result.getOrderDetailInfos().get(0).getName()).isEqualTo("빵빵이키링");
        assertThat(result.getOrderDetailInfos().get(0).getPrice()).isEqualTo(1000L);
        assertThat(result.getOrderDetailInfos().get(0).getQuantity()).isEqualTo(2L);
        assertThat(result.getOrderDetailInfos().get(0).getOrderStatus()).isEqualTo(ORDER_CANCEL.getCode());

        assertThat(result.getOrderDetailInfos().get(1).getOrderNo()).isEqualTo("1");
        assertThat(result.getOrderDetailInfos().get(1).getProductId()).isEqualTo(2L);
        assertThat(result.getOrderDetailInfos().get(1).getProductNo()).isEqualTo("2");
        assertThat(result.getOrderDetailInfos().get(1).getName()).isEqualTo("옥지얌키링");
        assertThat(result.getOrderDetailInfos().get(1).getPrice()).isEqualTo(2000L);
        assertThat(result.getOrderDetailInfos().get(1).getQuantity()).isEqualTo(3L);
        assertThat(result.getOrderDetailInfos().get(1).getOrderStatus()).isEqualTo(ORDER_CANCEL.getCode());
    }

    @Test
    @DisplayName("(page)dtio.Response 주어지면 Http.Response 변환한다.")
    public void response_page_of() {
        // given
        LocalDateTime cancelReceiptDate1 = LocalDateTime.of(2024, 2, 8, 4, 59);
        LocalDateTime orderDate1 = LocalDateTime.of(2024, 2, 8, 4, 58);

        GetCancelReturnListDtio.Response response1 = GetCancelReturnListDtio.Response.builder()
                .cancelReceiptDate(cancelReceiptDate1)
                .orderDate(orderDate1)
                .orderNo("1")
                .build();

        GetCancelReturnListDtio.OrderDetailInfo orderDetailInfo1 = GetCancelReturnListDtio.OrderDetailInfo.builder()
                .orderNo("1")
                .productId(1L)
                .productNo("1")
                .name("빵빵이키링")
                .price(1000L)
                .quantity(2L)
                .orderStatus(ORDER_CANCEL.getCode())
                .build();
        List<GetCancelReturnListDtio.OrderDetailInfo> orderDetailInfos1 = List.of(orderDetailInfo1);

        response1.changeOrderDetailInfos(orderDetailInfos1);

        LocalDateTime cancelReceiptDate2 = LocalDateTime.of(2024, 2, 8, 4, 59);
        LocalDateTime orderDate2 = LocalDateTime.of(2024, 2, 8, 4, 58);

        GetCancelReturnListDtio.Response response2 = GetCancelReturnListDtio.Response.builder()
                .cancelReceiptDate(cancelReceiptDate2)
                .orderDate(orderDate2)
                .orderNo("2")
                .build();

        GetCancelReturnListDtio.OrderDetailInfo orderDetailInfo2 = GetCancelReturnListDtio.OrderDetailInfo.builder()
                .orderNo("2")
                .productId(2L)
                .productNo("2")
                .name("옥지얌키링")
                .price(2000L)
                .quantity(3L)
                .orderStatus(ORDER_CANCEL.getCode())
                .build();
        List<GetCancelReturnListDtio.OrderDetailInfo> orderDetailInfos2 = List.of(orderDetailInfo2);

        response2.changeOrderDetailInfos(orderDetailInfos2);

        List<GetCancelReturnListDtio.Response> responses = List.of(response1, response2);

        PageImpl<GetCancelReturnListDtio.Response> given = new PageImpl<>(responses, PageRequest.of(0, 3), 2);

        // when
        Page<GetCancelReturnListHttp.Response> result = GetCancelReturnListHttp.Response.of(given);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.getContent().get(0).getCancelReceiptDate()).isEqualTo(cancelReceiptDate1);
        assertThat(result.getContent().get(0).getOrderDate()).isEqualTo(orderDate1);
        assertThat(result.getContent().get(0).getOrderNo()).isEqualTo("1");
        assertThat(result.getContent().get(0).getOrderDetailInfos().get(0).getOrderNo()).isEqualTo("1");
        assertThat(result.getContent().get(0).getOrderDetailInfos().get(0).getProductId()).isEqualTo(1L);
        assertThat(result.getContent().get(0).getOrderDetailInfos().get(0).getProductNo()).isEqualTo("1");
        assertThat(result.getContent().get(0).getOrderDetailInfos().get(0).getName()).isEqualTo("빵빵이키링");
        assertThat(result.getContent().get(0).getOrderDetailInfos().get(0).getPrice()).isEqualTo(1000L);
        assertThat(result.getContent().get(0).getOrderDetailInfos().get(0).getQuantity()).isEqualTo(2L);
        assertThat(result.getContent().get(0).getOrderDetailInfos().get(0).getOrderStatus()).isEqualTo(ORDER_CANCEL.getCode());

        assertThat(result.getContent().get(1).getCancelReceiptDate()).isEqualTo(cancelReceiptDate2);
        assertThat(result.getContent().get(1).getOrderDate()).isEqualTo(orderDate2);
        assertThat(result.getContent().get(1).getOrderNo()).isEqualTo("2");
        assertThat(result.getContent().get(1).getOrderDetailInfos().get(0).getOrderNo()).isEqualTo("2");
        assertThat(result.getContent().get(1).getOrderDetailInfos().get(0).getProductId()).isEqualTo(2L);
        assertThat(result.getContent().get(1).getOrderDetailInfos().get(0).getProductNo()).isEqualTo("2");
        assertThat(result.getContent().get(1).getOrderDetailInfos().get(0).getName()).isEqualTo("옥지얌키링");
        assertThat(result.getContent().get(1).getOrderDetailInfos().get(0).getPrice()).isEqualTo(2000L);
        assertThat(result.getContent().get(1).getOrderDetailInfos().get(0).getQuantity()).isEqualTo(3L);
        assertThat(result.getContent().get(1).getOrderDetailInfos().get(0).getOrderStatus()).isEqualTo(ORDER_CANCEL.getCode());
    }

    @Test
    @DisplayName("dtio.OrderDetailInfo가 주어지면 Http.OrderDetailInfo로 변환한다.")
    public void orderDetailInfo_of() {
        // given
        GetCancelReturnListDtio.OrderDetailInfo dtio = GetCancelReturnListDtio.OrderDetailInfo.builder()
                .orderNo("1")
                .productId(1L)
                .productNo("1")
                .name("빵빵이키링")
                .price(1000L)
                .quantity(2L)
                .orderStatus(ORDER_CANCEL.getCode())
                .build();

        // when
        GetCancelReturnListHttp.OrderDetailInfo result = GetCancelReturnListHttp.OrderDetailInfo.of(dtio);

        // then
        assertThat(result.getOrderNo()).isEqualTo("1");
        assertThat(result.getProductId()).isEqualTo(1L);
        assertThat(result.getProductNo()).isEqualTo("1");
        assertThat(result.getName()).isEqualTo("빵빵이키링");
        assertThat(result.getPrice()).isEqualTo(1000L);
        assertThat(result.getQuantity()).isEqualTo(2L);
        assertThat(result.getOrderStatus()).isEqualTo(ORDER_CANCEL.getCode());
    }

}