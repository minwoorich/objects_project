package com.objects.marketbridge.order.controller.docs;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.objects.marketbridge.common.security.annotation.WithMockCustomUser;
import com.objects.marketbridge.common.service.port.DateTimeHolder;
import com.objects.marketbridge.member.domain.Member;
import com.objects.marketbridge.member.domain.MembershipType;
import com.objects.marketbridge.member.service.port.MemberRepository;
import com.objects.marketbridge.order.controller.OrderCancelReturnController;
import com.objects.marketbridge.order.infra.dtio.GetCancelReturnListDtio;
import com.objects.marketbridge.order.service.OrderCancelReturnService;
import com.objects.marketbridge.order.service.dto.GetCancelDetailDto;
import com.objects.marketbridge.order.service.dto.RequestCancelDto;
import com.objects.marketbridge.order.service.dto.RequestReturnDto;
import com.objects.marketbridge.order.service.port.OrderDetailDtoRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;

import static com.objects.marketbridge.order.domain.StatusCodeType.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ActiveProfiles("test")
@WebMvcTest(OrderCancelReturnController.class)
@ExtendWith(RestDocumentationExtension.class)
public class OrderCancelReturnControllerRestDocsTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    OrderCancelReturnService orderCancelReturnService;
    @MockBean
    OrderDetailDtoRepository orderDetailDtoRepository;
    @MockBean
    MemberRepository memberRepository;
    @MockBean
    DateTimeHolder dateTimeHolder;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentationContextProvider) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .build();
    }

    @Test
    @DisplayName("주문 취소 요청 API")
    @WithMockCustomUser
    public void requestCancel() throws Exception {
        // given
        RequestCancelDto.Response response = RequestCancelDto.Response.builder()
                .cancelRefundInfo(
                        RequestCancelDto.CancelRefundInfo.builder()
                                .deliveryFee(0L)
                                .refundFee(0L)
                                .totalPrice(2000L)
                                .discountPrice(0L)
                                .build()
                )
                .productInfo(
                        RequestCancelDto.ProductInfo.builder()
                                .name("빵빵이키링")
                                .quantity(2L)
                                .price(1000L)
                                .image("빵빵이썸네일")
                                .build()
                )
                .build();

        Member member = Member.builder()
                .membership(MembershipType.WOW.getText())
                .build();

        given(memberRepository.findById(anyLong())).willReturn(member);
        given(orderCancelReturnService.findCancelInfo(anyLong(), anyLong(), anyString()))
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/orders/cancel-flow")
                                .header(HttpHeaders.AUTHORIZATION, "bearer AccessToken")
                                .accept(MediaType.APPLICATION_JSON)
                                .param("orderDetailId", "1")
                                .param("numberOfCancellation", "2")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("order-cancel-request",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("orderDetailId")
                                        .description("주문 상세 ID"),
                                parameterWithName("numberOfCancellation")
                                        .description("취소 수량")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.productInfo").type(JsonFieldType.OBJECT)
                                        .description("취소 상품 정보"),
                                fieldWithPath("data.productInfo.quantity").type(JsonFieldType.NUMBER)
                                        .description("취소 상품 수량"),
                                fieldWithPath("data.productInfo.name").type(JsonFieldType.STRING)
                                        .description("취소 상품 이름"),
                                fieldWithPath("data.productInfo.price").type(JsonFieldType.NUMBER)
                                        .description("취소 상품 가격"),
                                fieldWithPath("data.productInfo.image").type(JsonFieldType.STRING)
                                        .description("취소 상품 썸네일"),
                                fieldWithPath("data.cancelRefundInfo").type(JsonFieldType.OBJECT)
                                        .description("취소 환불 정보"),
                                fieldWithPath("data.cancelRefundInfo.deliveryFee").type(JsonFieldType.NUMBER)
                                        .description("환불 배송비"),
                                fieldWithPath("data.cancelRefundInfo.refundFee").type(JsonFieldType.NUMBER)
                                        .description("환불 금액"),
                                fieldWithPath("data.cancelRefundInfo.discountPrice").type(JsonFieldType.NUMBER)
                                        .description("할인 금액"),
                                fieldWithPath("data.cancelRefundInfo.totalPrice").type(JsonFieldType.NUMBER)
                                        .description("상품 전체 금액")
                        )));
    }

    @Test
    @DisplayName("주문 반품 요청 API")
    @WithMockCustomUser
    public void requestReturnOrder() throws Exception {
        // given
        RequestReturnDto.Response response = RequestReturnDto.Response.builder()
                .returnRefundInfo(
                        RequestReturnDto.ReturnRefundInfo.builder()
                                .returnFee(0L)
                                .deliveryFee(0L)
                                .productTotalPrice(2000L)
                                .build()
                )
                .productInfo(
                        RequestReturnDto.ProductInfo.builder()
                                .quantity(2L)
                                .name("빵빵이키링")
                                .price(1000L)
                                .image("빵빵이썸네일")
                                .build()
                )
                .build();

        Member member = Member.builder()
                .membership(MembershipType.WOW.getText())
                .build();

        given(memberRepository.findById(anyLong())).willReturn(member);
        given(orderCancelReturnService.findReturnInfo(anyLong(), anyLong(), anyString()))
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/orders/return-flow")
                                .header(HttpHeaders.AUTHORIZATION, "bearer AccessToken")
                                .accept(MediaType.APPLICATION_JSON)
                                .param("orderDetailId", "1")
                                .param("numberOfReturns", "2")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("order-return-request",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("orderDetailId")
                                        .description("주문 상세 ID"),
                                parameterWithName("numberOfReturns")
                                        .description("취소 수량")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.productInfo").type(JsonFieldType.OBJECT)
                                        .description("반품 상품 정보"),
                                fieldWithPath("data.productInfo.quantity").type(JsonFieldType.NUMBER)
                                        .description("반품 상품 수량"),
                                fieldWithPath("data.productInfo.name").type(JsonFieldType.STRING)
                                        .description("반품 상품 이름"),
                                fieldWithPath("data.productInfo.price").type(JsonFieldType.NUMBER)
                                        .description("반품 상품 가격"),
                                fieldWithPath("data.productInfo.image").type(JsonFieldType.STRING)
                                        .description("반품 상품 썸네일"),
                                fieldWithPath("data.returnRefundInfo").type(JsonFieldType.OBJECT)
                                        .description("반품 환불 정보"),
                                fieldWithPath("data.returnRefundInfo.deliveryFee").type(JsonFieldType.NUMBER)
                                        .description("환불 배송비"),
                                fieldWithPath("data.returnRefundInfo.returnFee").type(JsonFieldType.NUMBER)
                                        .description("반품 배송비"),
                                fieldWithPath("data.returnRefundInfo.productTotalPrice").type(JsonFieldType.NUMBER)
                                        .description("환불(상품) 금액")
                        )));
    }

    @Test
    @DisplayName("주문 취소/반품 리스트 반환 API")
    @WithMockCustomUser
    public void getCancelReturnList() throws Exception {
        // given
        LocalDateTime orderDate1 = LocalDateTime.of(2024, 2, 8, 11, 39);
        LocalDateTime cancelDate1 = LocalDateTime.of(2024, 2, 8, 11, 40);
        GetCancelReturnListDtio.Response content1 = GetCancelReturnListDtio.Response.builder()
                .orderDate(orderDate1)
                .cancelReceiptDate(cancelDate1)
                .orderDetailInfo(
                        GetCancelReturnListDtio.OrderDetailInfo.builder()
                                .orderNo("1")
                                .productId(1L)
                                .productNo("1")
                                .name("빵빵이키링")
                                .price(1000L)
                                .quantity(2L)
                                .orderStatus(ORDER_CANCEL.getCode())
                                .build()
                )
                .build();

        LocalDateTime orderDate2 = LocalDateTime.of(2024, 2, 8, 11, 39);
        LocalDateTime cancelDate2 = LocalDateTime.of(2024, 2, 8, 11, 40);
        GetCancelReturnListDtio.Response content2 = GetCancelReturnListDtio.Response.builder()
                .orderDate(orderDate2)
                .cancelReceiptDate(cancelDate2)
                .orderDetailInfo(
                        GetCancelReturnListDtio.OrderDetailInfo.builder()
                                .orderNo("1")
                                .productId(2L)
                                .productNo("2")
                                .name("옥지얌키링")
                                .price(2000L)
                                .quantity(4L)
                                .orderStatus(ORDER_CANCEL.getCode())
                                .build()
                )
                .build();

        List<GetCancelReturnListDtio.Response> contents = List.of(content1, content2);

        PageRequest pageRequest = PageRequest.of(0, 5);
        given(orderDetailDtoRepository.findCancelReturnListDtio(anyLong(), any(PageRequest.class)))
                .willReturn(new PageImpl<>(contents, pageRequest, 2));

        // when // then
        mockMvc.perform(
                        get("/orders/cancel-return/list")
                                .accept(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, "bearer AccessToken")
                                .param("page", "0")
                                .param("size", "5")

                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("order-cancel-return-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("page")
                                        .description("페이지 번호"),
                                parameterWithName("size")
                                        .description("사이즈 크기")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.content[].cancelReceiptDate").type(JsonFieldType.STRING)
                                        .description("주문 취소 날짜"),
                                fieldWithPath("data.content[].orderDate").type(JsonFieldType.STRING)
                                        .description("주문 날짜"),
                                fieldWithPath("data.content[].orderDetailInfo.orderNo").type(JsonFieldType.STRING)
                                        .description("주문 번호"),
                                fieldWithPath("data.content[].orderDetailInfo.productId").type(JsonFieldType.NUMBER)
                                        .description("상품 Id"),
                                fieldWithPath("data.content[].orderDetailInfo.productNo").type(JsonFieldType.STRING)
                                        .description("상품 번호"),
                                fieldWithPath("data.content[].orderDetailInfo.name").type(JsonFieldType.STRING)
                                        .description("상품 이름"),
                                fieldWithPath("data.content[].orderDetailInfo.price").type(JsonFieldType.NUMBER)
                                        .description("상품 가격"),
                                fieldWithPath("data.content[].orderDetailInfo.quantity").type(JsonFieldType.NUMBER)
                                        .description("상품 주문 수량"),
                                fieldWithPath("data.content[].orderDetailInfo.orderStatus").type(JsonFieldType.STRING)
                                        .description("주문 상태"),
                                fieldWithPath("data.pageable.pageNumber").type(JsonFieldType.NUMBER)
                                        .description("페이지 번호"),
                                fieldWithPath("data.pageable.pageSize").type(JsonFieldType.NUMBER)
                                        .description("페이지당 요소 수"),
                                fieldWithPath("data.pageable.sort.empty").type(JsonFieldType.BOOLEAN)
                                        .description("정렬이 비어 있는지 여부"),
                                fieldWithPath("data.pageable.sort.sorted").type(JsonFieldType.BOOLEAN)
                                        .description("정렬이 되어 있는지 여부"),
                                fieldWithPath("data.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN)
                                        .description("정렬이 되어 있지 않은지 여부"),
                                fieldWithPath("data.pageable.offset").type(JsonFieldType.NUMBER)
                                        .description("페이지 오프셋"),
                                fieldWithPath("data.pageable.paged").type(JsonFieldType.BOOLEAN)
                                        .description("페이지가 있는지 여부"),
                                fieldWithPath("data.pageable.unpaged").type(JsonFieldType.BOOLEAN)
                                        .description("페이지가 없는지 여부"),
                                fieldWithPath("data.last").type(JsonFieldType.BOOLEAN)
                                        .description("마지막 페이지 여부"),
                                fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER)
                                        .description("전체 페이지 수"),
                                fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER)
                                        .description("전체 요소 수"),
                                fieldWithPath("data.size").type(JsonFieldType.NUMBER)
                                        .description("페이지 크기"),
                                fieldWithPath("data.number").type(JsonFieldType.NUMBER)
                                        .description("페이지 번호"),
                                fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN)
                                        .description("정렬이 비어 있는지 여부"),
                                fieldWithPath("data.sort.sorted").type(JsonFieldType.BOOLEAN)
                                        .description("정렬이 되어 있는지 여부"),
                                fieldWithPath("data.sort.unsorted").type(JsonFieldType.BOOLEAN)
                                        .description("정렬이 되어 있지 않은지 여부"),
                                fieldWithPath("data.first").type(JsonFieldType.BOOLEAN)
                                        .description("첫 페이지 여부"),
                                fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER)
                                        .description("현재 페이지의 요소 수"),
                                fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN)
                                        .description("컨텐츠가 비어 있는지 여부")
                        )));
    }


    @Test
    @DisplayName("취소/반품 상세를 조회한다.")
    @WithMockCustomUser
    public void getCancelReturnDetail() throws Exception {
        // given
        GetCancelDetailDto.ProductInfo productInfo = GetCancelDetailDto.ProductInfo.builder()
                .productId(1L)
                .productNo("1")
                .name("빵빵이 키링")
                .price(10000L)
                .quantity(2L)
                .build();

        GetCancelDetailDto.RefundInfo refundInfo = GetCancelDetailDto.RefundInfo.builder()
                .deliveryFee(0L)
                .refundFee(0L)
                .discountPrice(5000L)
                .totalPrice(100000L)
                .build();

        GetCancelDetailDto.Response response = GetCancelDetailDto.Response.builder()
                .orderDate(LocalDateTime.now())
                .cancelDate(LocalDateTime.now())
                .orderNo("123")
                .cancelReason("빵빵이 기여워")
                .productInfo(productInfo)
                .refundInfo(refundInfo)
                .build();

        Member member = Member.builder()
                .membership(MembershipType.WOW.getText())
                .build();

        given(memberRepository.findById(anyLong())).willReturn(member);
        given(orderCancelReturnService.findCancelDetail(anyLong(), any(String.class)))
                .willReturn(response);

        // when // then
        mockMvc.perform(
                        get("/orders/cancel/detail")
                                .header(HttpHeaders.AUTHORIZATION, "bearer AccessToken")
                                .accept(MediaType.APPLICATION_JSON)
                                .param("orderDetailId", "1")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("order-cancel-detail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("orderDetailId")
                                        .description("주문 상세 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.orderDate").type(JsonFieldType.STRING)
                                        .description("주문 날짜"),
                                fieldWithPath("data.cancelDate").type(JsonFieldType.STRING)
                                        .description("주문 취소 날짜"),
                                fieldWithPath("data.orderNo").type(JsonFieldType.STRING)
                                        .description("주문 번호"),
                                fieldWithPath("data.cancelReason").type(JsonFieldType.STRING)
                                        .description("주문 취소 이유"),
                                fieldWithPath("data.productInfo").type(JsonFieldType.OBJECT)
                                        .description("상품 정보"),
                                fieldWithPath("data.productInfo.productId").type(JsonFieldType.NUMBER)
                                        .description("상품 ID"),
                                fieldWithPath("data.productInfo.productNo").type(JsonFieldType.STRING)
                                        .description("상품 번호"),
                                fieldWithPath("data.productInfo.name").type(JsonFieldType.STRING)
                                        .description("상품 이름"),
                                fieldWithPath("data.productInfo.price").type(JsonFieldType.NUMBER)
                                        .description("주문 가격"),
                                fieldWithPath("data.productInfo.quantity").type(JsonFieldType.NUMBER)
                                        .description("상품 주문 수량"),
                                fieldWithPath("data.refundInfo").type(JsonFieldType.OBJECT)
                                        .description("취소/반품 정보"),
                                fieldWithPath("data.refundInfo.deliveryFee").type(JsonFieldType.NUMBER)
                                        .description("배송 비용"),
                                fieldWithPath("data.refundInfo.refundFee").type(JsonFieldType.NUMBER)
                                        .description("반품 비용"),
                                fieldWithPath("data.refundInfo.discountPrice").type(JsonFieldType.NUMBER)
                                        .description("할인 금액"),
                                fieldWithPath("data.refundInfo.totalPrice").type(JsonFieldType.NUMBER)
                                        .description("상품 할인 전 금액 합계")
                        )));
    }

//    @Test
//    @DisplayName("주문 취소 확정 API")
//    public void confirmCancelReturn() throws Exception {
//        // given
//        ConfirmCancelReturnHttp.OrderDetailInfo orderDetailInfo1 = ConfirmCancelReturnHttp.OrderDetailInfo.builder()
//                .orderDetailId(1L)
//                .numberOfCancellation(1L)
//                .build();
//        ConfirmCancelReturnHttp.OrderDetailInfo orderDetailInfo2 = ConfirmCancelReturnHttp.OrderDetailInfo.builder()
//                .orderDetailId(2L)
//                .numberOfCancellation(2L)
//                .build();
//        List<ConfirmCancelReturnHttp.OrderDetailInfo> orderDetailInfos = List.of(orderDetailInfo1, orderDetailInfo2);
//
//        ConfirmCancelReturnHttp.Request request = ConfirmCancelReturnHttp.Request.builder()
//                .orderDetailInfos(orderDetailInfos)
//                .cancelReason("단순변심")
//                .build();
//
//
//        LocalDateTime cancellationDate = LocalDateTime.of(2024, 1, 18, 12, 26);
//        LocalDateTime refundProcessedAt = LocalDateTime.of(2024, 1, 18, 12, 26);
//
//        given(orderCancelReturnService.confirmCancelReturn(any(ConfirmCancelReturnDto.Request.class), any(DateTimeHolder.class)))
//                .willReturn(ConfirmCancelReturnDto.Response.builder()
//                        .orderId(1L)
//                        .orderNo("ORD001")
//                        .totalPrice(300L)
//                        .cancellationDate(cancellationDate)
//                        .refundInfo(ConfirmCancelReturnDto.RefundInfo.of(
//                                RefundDto.builder()
//                                        .totalRefundAmount(10000L)
//                                        .refundMethod("card")
//                                        .refundProcessedAt(refundProcessedAt)
//                                        .build())
//                        )
//                        .cancelledItems(List.of(
//                                        ConfirmCancelReturnDto.ProductInfo.builder()
//                                                .productId(1L)
//                                                .name("빵빵이 키링")
//                                                .productNo("P123456")
//                                                .price(10000L)
//                                                .quantity(2L)
//                                                .build(),
//                                        ConfirmCancelReturnDto.ProductInfo.builder()
//                                                .productId(2L)
//                                                .name("옥지얌 키링")
//                                                .productNo("P2345667")
//                                                .price(20000L)
//                                                .quantity(3L)
//                                                .build()
//                                )
//                        )
//                        .build()
//                );
//
//        // when // then
//        mockMvc.perform(
//                        post("/orders/cancel-return-flow/thank-you")
//                                .content(objectMapper.writeValueAsString(request))
//                                .contentType(MediaType.APPLICATION_JSON)
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andDo(document("order-cancel-return",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestFields(
//                                fieldWithPath("orderDetailInfos[]").type(JsonFieldType.ARRAY)
//                                        .description("주문 상세 정보 리스트"),
//                                fieldWithPath("orderDetailInfos[].orderDetailId").type(JsonFieldType.NUMBER)
//                                        .description("주문 상세 ID"),
//                                fieldWithPath("orderDetailInfos[].numberOfCancellation").type(JsonFieldType.NUMBER)
//                                        .description("주문 취소 개수"),
//                                fieldWithPath("cancelReason").type(JsonFieldType.STRING)
//                                        .description("상품 판매상태")
//                        ),
//                        responseFields(
//                                fieldWithPath("code").type(JsonFieldType.NUMBER)
//                                        .description("코드"),
//                                fieldWithPath("status").type(JsonFieldType.STRING)
//                                        .description("상태"),
//                                fieldWithPath("message").type(JsonFieldType.STRING)
//                                        .description("메시지"),
//                                fieldWithPath("data").type(JsonFieldType.OBJECT)
//                                        .description("응답 데이터"),
//                                fieldWithPath("data.orderId").type(JsonFieldType.NUMBER)
//                                        .description("주문 ID"),
//                                fieldWithPath("data.orderNo").type(JsonFieldType.STRING)
//                                        .description("주문 번호"),
//                                fieldWithPath("data.totalPrice").type(JsonFieldType.NUMBER)
//                                        .description("주문 총 가격"),
//                                fieldWithPath("data.cancellationDate").type(JsonFieldType.ARRAY)
//                                        .description("주문 취소 시간"),
//                                fieldWithPath("data.cancelledItems").type(JsonFieldType.ARRAY)
//                                        .description("상품 이름"),
//                                fieldWithPath("data.cancelledItems[].productId").type(JsonFieldType.NUMBER)
//                                        .description("취소된 상품의 제품 ID"),
//                                fieldWithPath("data.cancelledItems[].productNo").type(JsonFieldType.STRING)
//                                        .description("취소된 상품의 제품 번호"),
//                                fieldWithPath("data.cancelledItems[].name").type(JsonFieldType.STRING)
//                                        .description("취소된 상품의 이름"),
//                                fieldWithPath("data.cancelledItems[].price").type(JsonFieldType.NUMBER)
//                                        .description("취소된 상품의 가격"),
//                                fieldWithPath("data.cancelledItems[].quantity").type(JsonFieldType.NUMBER)
//                                        .description("취소된 주문 수량"),
//                                fieldWithPath("data.refundInfo").type(JsonFieldType.OBJECT)
//                                        .description("환불 정보"),
//                                fieldWithPath("data.refundInfo.totalRefundAmount").type(JsonFieldType.NUMBER)
//                                        .description("환불 총 금액"),
//                                fieldWithPath("data.refundInfo.refundMethod").type(JsonFieldType.STRING)
//                                        .description("환불 방법"),
//                                fieldWithPath("data.refundInfo.refundProcessedAt").type(JsonFieldType.ARRAY)
//                                        .description("환불 시간")
//                        )
//                ));
//    }


}
