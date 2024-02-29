package com.objects.marketbridge.domains.order.controller.docs;

import com.objects.marketbridge.common.RestDocsSupportWebAppContext;
import com.objects.marketbridge.common.exception.exceptions.CustomLogicException;
import com.objects.marketbridge.common.security.annotation.WithMockCustomUser;
import com.objects.marketbridge.domains.order.controller.OrderController;
import com.objects.marketbridge.domains.order.service.CreateOrderService;
import com.objects.marketbridge.domains.order.service.GetOrderService;
import com.objects.marketbridge.domains.order.service.dto.CreateOrderDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static com.objects.marketbridge.common.exception.exceptions.ErrorCode.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ActiveProfiles("test")
@WebMvcTest(OrderController.class)
public class OrderControllerErrorTest extends RestDocsSupportWebAppContext {

    @MockBean CreateOrderService createOrderService;
    @MockBean GetOrderService getOrderService;

    @DisplayName("[POST/orders] 상품 재고가 없는경우")
    @Test
    @WithMockCustomUser
    void createOrder_error1() throws Exception {
        // given

        doThrow(CustomLogicException.createBadRequestError(OUT_OF_STOCK))
                .when(createOrderService).create(any(CreateOrderDto.class));

        // when
        MockHttpServletRequestBuilder requestBuilder =
                post("/orders")
                        .header(HttpHeaders.AUTHORIZATION, "bearer AccessToken");

        // then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("order-create-outofstock-error",
                        preprocessResponse(prettyPrint())
                ));
    }

    @DisplayName("[POST/orders] 이미 사용한 쿠폰을 사용하려고 할 경우")
    @Test
    @WithMockCustomUser
    void createOrder_error2() throws Exception {

        // given
        doThrow(CustomLogicException.createBadRequestError(COUPON_ALREADY_USED))
                .when(createOrderService).create(any(CreateOrderDto.class));

        // when
        MockHttpServletRequestBuilder requestBuilder =
                post("/orders")
                        .header(HttpHeaders.AUTHORIZATION, "bearer AccessToken");

        // then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("order-create-couponalreadyused-error",
                        preprocessResponse(prettyPrint())
                ));
    }

    @DisplayName("[POST/orders] 쿠폰 최소 주문금액보다 적게 주문 한 경우")
    @Test
    @WithMockCustomUser
    void createOrder_error3() throws Exception {

        // given
        doThrow(CustomLogicException.createBadRequestError(COUPON_CONDITION_VIOLATION))
                .when(createOrderService).create(any(CreateOrderDto.class));

        // when
        MockHttpServletRequestBuilder requestBuilder =
                post("/orders")
                        .header(HttpHeaders.AUTHORIZATION, "bearer AccessToken");

        // then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("order-create-couponconditionviolation-error",
                        preprocessResponse(prettyPrint())
                ));
    }

    @DisplayName("[POST/orders] 유효기간 만료된 쿠폰 사용할 경우")
    @Test
    @WithMockCustomUser
    void createOrder_error4() throws Exception {

        // given
        doThrow(CustomLogicException.createBadRequestError(COUPON_EXPIRED))
                .when(createOrderService).create(any(CreateOrderDto.class));

        // when
        MockHttpServletRequestBuilder requestBuilder =
                post("/orders")
                        .header(HttpHeaders.AUTHORIZATION, "bearer AccessToken");

        // then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("order-create-couponexpired-error",
                        preprocessResponse(prettyPrint())
                ));
    }

    @DisplayName("[POST/orders] 상품과 관련이없는 쿠폰을 사용하려고 할 경우")
    @Test
    @WithMockCustomUser
    void createOrder_error5() throws Exception {

        // given
        doThrow(CustomLogicException.createBadRequestError(COUPON_INCOMPATIBLE))
                .when(createOrderService).create(any(CreateOrderDto.class));

        // when
        MockHttpServletRequestBuilder requestBuilder =
                post("/orders")
                        .header(HttpHeaders.AUTHORIZATION, "bearer AccessToken");

        // then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("order-create-couponincompatible-error",
                        preprocessResponse(prettyPrint())
                ));
    }

}
