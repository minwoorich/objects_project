package com.objects.marketbridge.cart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.objects.marketbridge.cart.controller.dto.CreateCartHttp;
import com.objects.marketbridge.cart.domain.Cart;
import com.objects.marketbridge.cart.service.AddToCartService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ActiveProfiles("test")
@WebMvcTest(CartController.class)
@ExtendWith(RestDocumentationExtension.class)
class CartControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean AddToCartService addToCartService;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentationContextProvider) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .build();
    }

    @DisplayName("[API] POST/carts 테스트")
    @Test
    void addToCart() throws Exception {
        // given
        CreateCartHttp.Request request = CreateCartHttp.Request.create("productNo", 1L, false);
        given(addToCartService.add(request.toDto(anyLong()))).willReturn(any(Cart.class));


        // when
        MockHttpServletRequestBuilder requestBuilder =
                post("/carts")
                    .header(HttpHeaders.AUTHORIZATION, "bearer AccessToken")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("cart-add",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("productNo").type(JsonFieldType.STRING)
                                        .description("상품 번호"),
                                fieldWithPath("quantity").type(JsonFieldType.NUMBER)
                                        .description("상품 수량"),
                                fieldWithPath("isSubs").type(JsonFieldType.BOOLEAN)
                                        .description("정기 구독 상품인지 아닌지 구분 하는 값")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("응답 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("HTTP 응답"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.NULL)
                                        .description("응답 데이터")
                        )
                ));
    }
}