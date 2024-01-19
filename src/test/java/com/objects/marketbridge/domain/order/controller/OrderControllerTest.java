package com.objects.marketbridge.domain.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.objects.marketbridge.domain.member.repository.MemberRepository;
import com.objects.marketbridge.domain.order.controller.request.CheckoutRequest;
import com.objects.marketbridge.domain.order.controller.request.CreateOrderRequest;
import com.objects.marketbridge.domain.order.dto.ProductInfoDto;
import com.objects.marketbridge.domain.order.entity.ProductValue;
import com.objects.marketbridge.domain.order.service.CreateOrderService;
import com.objects.marketbridge.domain.payment.config.TossPaymentConfig;
import com.objects.marketbridge.global.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

/**
 * @SpringBootTest -> 컨테이너에 모든 빈을 등록하고 주입해줘서 무거움(느림)
 * @WebMvcTest -> 웹 관련 빈만 주입해줌(ex,@Controller) 그래서 가벼움,
 *                 대신 다른 빈들은 @MockBean 으로 등록해줘야함
 */
@ActiveProfiles("test")
@WebMvcTest(controllers = OrderController.class)
class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    MemberRepository memberRepository;
    @MockBean
    TossPaymentConfig tossPaymentConfig;
    @MockBean
    CreateOrderService createOrderService;
    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @DisplayName("신규 주문을 생성한다")
    @Test
    void createOrder() throws Exception {

        String token = "token";
        BDDMockito.given(jwtTokenProvider.validateToken(token)).willReturn(true);

        ProductValue productValue1 = ProductValue.builder()
                .couponId(1L)
                .productId(1L)
                .quantity(1L).build();

        ProductValue productValue2 = ProductValue.builder()
                .couponId(2L)
                .productId(2L)
                .quantity(2L).build();

        List<ProductValue> productValues = List.of(productValue1, productValue2);

        CheckoutRequest request = CheckoutRequest.builder()
                .orderId("aaaa-aaaa-aaaa")
                .orderName("가방외 1건")
                .amount(20000L)
                .productValues(productValues)
                .build();

        // given
        mockMvc.perform(MockMvcRequestBuilders
                    .post("/orders/checkout")
                        .header("authorization", "bearer " + token)
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isOk());

        // when

        //then

    }

}