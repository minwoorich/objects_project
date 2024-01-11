package com.objects.marketbridge.domain.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.objects.marketbridge.domain.order.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@WebMvcTest(controllers = OrderController.class)
class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    private ObjectMapper objectMapper = new ObjectMapper();
    @DisplayName("CreateOrderRequest로 클라이언트 요청데이터를 제대로 받아야한다")
    @Test
    void receiveRequestBody() throws Exception {
        //given


        //when

        //then
    }

}