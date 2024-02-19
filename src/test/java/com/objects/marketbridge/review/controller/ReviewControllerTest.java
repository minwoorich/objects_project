package com.objects.marketbridge.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.objects.marketbridge.common.security.annotation.WithMockCustomUser;
import com.objects.marketbridge.common.security.config.SpringSecurityTestConfig;
import com.objects.marketbridge.review.dto.*;
import com.objects.marketbridge.review.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(ReviewController.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = {SpringSecurityTestConfig.class})
@ExtendWith(RestDocumentationExtension.class)
public class ReviewControllerTest {

    @MockBean
    ReviewService reviewService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentationContextProvider){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentationContextProvider))
                .build();
    }


    @Test
    @WithMockCustomUser
    @DisplayName("리뷰_서베이_선택창_조회를_요청받으면_선택창이_조회되고_응답한다.")
    public void getReviewSurveyQuestionAndOptionsListControllerTest() throws Exception {
        //given
        Long productId = 1L;
        List<ReviewSurveyQuestionAndOptionsDto> mockReviewSurveyQuestionAndOptionsDtoList = new ArrayList<>();
        ReviewSurveyQuestionAndOptionsDto mockReviewSurveyQuestionAndOptionsDto1
                = ReviewSurveyQuestionAndOptionsDto.builder()
                .reviewSurveyQuestion("질문1")
                .build();
        ReviewSurveyQuestionAndOptionsDto mockReviewSurveyQuestionAndOptionsDto2
                = ReviewSurveyQuestionAndOptionsDto.builder()
                .reviewSurveyQuestion("질문2")
                .build();

        List<String> mockeviewSurveyOptionList3
                = new ArrayList<>();
        mockeviewSurveyOptionList3.add("선택지3-1");
        mockeviewSurveyOptionList3.add("선택지3-2");
        mockeviewSurveyOptionList3.add("선택지3-3");
        mockeviewSurveyOptionList3.add("선택지3-4");
        ReviewSurveyQuestionAndOptionsDto mockReviewSurveyQuestionAndOptionsDto3
                = ReviewSurveyQuestionAndOptionsDto.builder()
                .reviewSurveyQuestion("질문3")
                .reviewSurveyOptionList(mockeviewSurveyOptionList3)
                .build();

        List<String> mockeviewSurveyOptionList4
                = new ArrayList<>();
        mockeviewSurveyOptionList4.add("선택지4-1");
        mockeviewSurveyOptionList4.add("선택지4-2");
        mockeviewSurveyOptionList4.add("선택지4-3");
        mockeviewSurveyOptionList4.add("선택지4-4");
        ReviewSurveyQuestionAndOptionsDto mockReviewSurveyQuestionAndOptionsDto4
                = ReviewSurveyQuestionAndOptionsDto.builder()
                .reviewSurveyQuestion("질문4")
                .reviewSurveyOptionList(mockeviewSurveyOptionList4)
                .build();
        mockReviewSurveyQuestionAndOptionsDtoList.add(mockReviewSurveyQuestionAndOptionsDto1);
        mockReviewSurveyQuestionAndOptionsDtoList.add(mockReviewSurveyQuestionAndOptionsDto2);
        mockReviewSurveyQuestionAndOptionsDtoList.add(mockReviewSurveyQuestionAndOptionsDto3);
        mockReviewSurveyQuestionAndOptionsDtoList.add(mockReviewSurveyQuestionAndOptionsDto4);

        given(reviewService.getReviewSurveyQuestionAndOptionsList(productId))
                .willReturn(mockReviewSurveyQuestionAndOptionsDtoList);


        mockMvc.perform(get("/review-survey-options/{productId}", 1L))
                .andExpect(status().isOk())
                .andDo(document("review-get-survey-options",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("productId").description("상품 ID")),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("응답데이터(리뷰서베이 질문과 선택지들)"),
                                subsectionWithPath("data[]").type(JsonFieldType.ARRAY).description("응답데이터(리뷰서베이 질문과 선택지들)"),
                                fieldWithPath("data[].reviewSurveyQuestion").type(JsonFieldType.STRING).description("리뷰서베이 질문"),
                                fieldWithPath("data[].reviewSurveyOptionList").type(JsonFieldType.ARRAY).optional().description("리뷰서베이 선택지들(null인경우 선택아닌 리뷰작성자가 직접 작성 요함"),
                                fieldWithPath("data[].reviewSurveyOptionList[]").type(JsonFieldType.ARRAY).optional().description("리뷰서베이 선택지들(null인경우 선택아닌 리뷰작성자가 직접 작성 요함")
                        )
                ));
    }



    @Test
    @WithMockCustomUser
    @DisplayName("리뷰_생성을_요청받으면_리뷰가_생성되고_응답한다.")
    public void createReviewControllerTest() throws Exception {
        //given
        // 리뷰 생성에 필요한 요청 데이터
        List<ReviewSurveyDataDto> reviewSurveyDataDtoList = new ArrayList<>();
        ReviewSurveyDataDto reviewSurveyDataDto1 = ReviewSurveyDataDto.builder()
                .reviewSurveyCategoryData("리뷰서베이카테고리1(질문1)")
                .writtenOrSelectedSurveyContentData("작성한 내용1")
                .build();
        ReviewSurveyDataDto reviewSurveyDataDto2 = ReviewSurveyDataDto.builder()
                .reviewSurveyCategoryData("리뷰서베이카테고리2(질문2)")
                .writtenOrSelectedSurveyContentData("작성한 내용2")
                .build();
        ReviewSurveyDataDto reviewSurveyDataDto3 = ReviewSurveyDataDto.builder()
                .reviewSurveyCategoryData("리뷰서베이카테고리2(질문3)")
                .writtenOrSelectedSurveyContentData("선택한 내용3")
                .build();
        ReviewSurveyDataDto reviewSurveyDataDto4 = ReviewSurveyDataDto.builder()
                .reviewSurveyCategoryData("리뷰서베이카테고리4(질문4)")
                .writtenOrSelectedSurveyContentData("선택한 내용4")
                .build();
        reviewSurveyDataDtoList.add(reviewSurveyDataDto1);
        reviewSurveyDataDtoList.add(reviewSurveyDataDto2);
        reviewSurveyDataDtoList.add(reviewSurveyDataDto3);
        reviewSurveyDataDtoList.add(reviewSurveyDataDto4);

        List<String> reviewImgUrls = new ArrayList<>();
        reviewImgUrls.add("image0001.jpg");
        reviewImgUrls.add("image0002.jpg");
        reviewImgUrls.add("image0003.jpg");
        CreateReviewDto request = CreateReviewDto.builder()
                .productId(1L)
                .rating(5)
                .reviewSurveyDataDtoList(reviewSurveyDataDtoList)
                .content("리뷰내용")
                .reviewImgUrls(reviewImgUrls)
                .summary("한줄요약")
                .build();
        given(reviewService.createReview(any(CreateReviewDto.class), anyLong())).willReturn(1L);

        //when //then
        // API 요청 및 응답 문서화
        String jsonContent = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent)
                        .header("Authorization", "bearer AccessToken")) // 액세스 토큰
                .andExpect(status().isOk())
                .andDo(document("review-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("productId").description("상품 ID"),
                                fieldWithPath("rating").description("별점"),
                                fieldWithPath("reviewSurveyDataDtoList").description("리뷰 서베이 데이터 리스트(카테고리, 선택 또는 입력된 내용 의 데이터 리스트)")
                                        .type(JsonFieldType.ARRAY).optional(), // 필드가 선택적인 경우 optional()을 사용
//                                        .attributes(key("constraints").value("Optional").description("선택적인 필드")),
                                fieldWithPath("reviewSurveyDataDtoList[].reviewSurveyCategoryData").description("리뷰 서베이 카테고리 데이터"),
                                fieldWithPath("reviewSurveyDataDtoList[].writtenOrSelectedSurveyContentData").description("입력 또는 선택된 내용 데이터"),
                                fieldWithPath("content").description("리뷰 내용"),
                                fieldWithPath("reviewImgUrls").description("리뷰 이미지 URL 목록"),
                                fieldWithPath("summary").description("한줄요약")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("status").description("응답 상태"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                fieldWithPath("data.reviewId").description("생성된 리뷰의 ID")
                        )
                ));
    }



    @Test
    @WithMockCustomUser
    @DisplayName("리뷰_아이디로_리뷰_단건_조회하면_조회되고_응답한다.")
    public void getReviewControllerTest() throws Exception {
        //given
        Long reviewId = 1L;

        List<ReviewSurveyDataDto> reviewSurveyDataDtoList = new ArrayList<>();
        ReviewSurveyDataDto reviewSurveyDataDto1 = ReviewSurveyDataDto.builder()
                .reviewSurveyCategoryData("리뷰서베이카테고리1(질문1)")
                .writtenOrSelectedSurveyContentData("작성한 내용1")
                .build();
        ReviewSurveyDataDto reviewSurveyDataDto2 = ReviewSurveyDataDto.builder()
                .reviewSurveyCategoryData("리뷰서베이카테고리2(질문2)")
                .writtenOrSelectedSurveyContentData("작성한 내용2")
                .build();
        ReviewSurveyDataDto reviewSurveyDataDto3 = ReviewSurveyDataDto.builder()
                .reviewSurveyCategoryData("리뷰서베이카테고리2(질문3)")
                .writtenOrSelectedSurveyContentData("선택한 내용3")
                .build();
        ReviewSurveyDataDto reviewSurveyDataDto4 = ReviewSurveyDataDto.builder()
                .reviewSurveyCategoryData("리뷰서베이카테고리4(질문4)")
                .writtenOrSelectedSurveyContentData("선택한 내용4")
                .build();
        reviewSurveyDataDtoList.add(reviewSurveyDataDto1);
        reviewSurveyDataDtoList.add(reviewSurveyDataDto2);
        reviewSurveyDataDtoList.add(reviewSurveyDataDto3);
        reviewSurveyDataDtoList.add(reviewSurveyDataDto4);

        List<String> reviewImgUrls = new ArrayList<>();
        reviewImgUrls.add("image0001.jpg");
        reviewImgUrls.add("image0002.jpg");
        reviewImgUrls.add("image0003.jpg");
        ReviewSingleReadDto mockReviewSingleReadDto = ReviewSingleReadDto.builder()
                .reviewId(reviewId)
                .memberId(1L)
                .productId(1L)
                .rating(5)
                .reviewSurveyDataDtoList(reviewSurveyDataDtoList)
                .content("리뷰 내용")
                .reviewImgUrls(reviewImgUrls)
                .build();
        given(reviewService.getReview(anyLong(), anyLong())).willReturn(mockReviewSingleReadDto);

        //when //then
        mockMvc.perform(get("/review/{reviewId}", reviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "bearer AccessToken")) // 액세스 토큰
                .andExpect(status().isOk())
                .andDo(document("review-get-single-review",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("reviewId").description("리뷰 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("status").description("응답 상태"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                fieldWithPath("data.reviewId").description("조회된 리뷰의 ID"),
                                fieldWithPath("data.memberId").description("조회된 리뷰의 멤버 ID"),
                                fieldWithPath("data.productId").description("조회된 리뷰의 상품 ID"),
                                fieldWithPath("data.rating").description("조회된 리뷰의 별점"),
                                fieldWithPath("data.reviewSurveyDataDtoList").description("조회된 리뷰 서베이 데이터 리스트(카테고리, 선택 또는 입력된 내용 의 데이터 리스트)")
                                        .type(JsonFieldType.ARRAY).optional(), // 필드가 선택적인 경우 optional()을 사용
                                fieldWithPath("data.reviewSurveyDataDtoList[].reviewSurveyCategoryData").description("조회된 리뷰 서베이 카테고리 데이터"),
                                fieldWithPath("data.reviewSurveyDataDtoList[].writtenOrSelectedSurveyContentData").description("조회된 입력 또는 선택된 내용 데이터"),
                                fieldWithPath("data.content").description("조회된 리뷰의 내용"),
                                fieldWithPath("data.reviewImgUrls").description("조회된 리뷰의 리뷰 이미지 URL들")
                        )
                ));
    }


//    //LIKE관련//
//    @Test
//    @DisplayName("리뷰_리스트를_상품_아이디로_최신순_또는_좋아요순_중_정렬을 선택해서_조회하면_" +
//            "해당_상품의_리뷰_리스트가_조회되고_응답한다.")
//    public void getProductReviewsControllerTest() throws Exception {
//        // given
//        Long productId = 1L;
//        List<String> reviewImgUrls = new ArrayList<>();
//        reviewImgUrls.add("image0001.jpg");
//        reviewImgUrls.add("image0002.jpg");
//        reviewImgUrls.add("image0003.jpg");
//        ReviewWholeInfoDto mockReviewWholeInfoDto =
//                ReviewWholeInfoDto.builder()
//                        .productName("상품명")
//                        .memberName("멤버이름")
//                        .rating(5)
//                        .createdAt(LocalDateTime.now())
//                        .sellerName("MarketBridge")
//                        .reviewImgUrls(reviewImgUrls)
//                        .content("리뷰 내용")
//                        .likes(0L)
//                        .build();
//
//        // page 및 sortBy 설정
//        // 정렬 선택: createdAt 또는 likes
//        int page = 0;
//        String sortBy = "likes";
//        Pageable pageable = PageRequest.of(page, 5, Sort.by(sortBy).descending());
//
//        // 실제 서비스 메서드 호출 결과를 설정
//        Page<ReviewWholeInfoDto> mockReviewPage
//                = new PageImpl<>(Collections.singletonList(mockReviewWholeInfoDto), pageable, 1);
//        given(reviewService.getProductReviews(productId, pageable, sortBy)).willReturn(mockReviewPage);
//
//        // when // then
//        mockMvc.perform(get("/product/{productId}/reviews", productId)
//                        .param("productId", String.valueOf(productId))
//                        .param("page", String.valueOf(page))
//                        .param("sortBy", sortBy)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andDo(document("reviews-of-product",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        pathParameters(
//                                parameterWithName("productId").description("상품 ID")
//                        ),
//                        responseFields(
//                                fieldWithPath("code").description("응답 코드"),
//                                fieldWithPath("status").description("응답 상태"),
//                                fieldWithPath("message").description("응답 메시지"),
//                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
//                                fieldWithPath("data.content[0].memberName").description("멤버 이름"),
//                                fieldWithPath("data.content[0].rating").description("별점"),
//                                fieldWithPath("data.content[0].createdAt").description("리뷰 작성일시"),
//                                fieldWithPath("data.content[0].sellerName").description("셀러명"),
//                                fieldWithPath("data.content[0].productName").description("상품명"),
//                                fieldWithPath("data.content[0].reviewImgUrls").description("리뷰 이미지 URL"),
//                                fieldWithPath("data.content[0].content").description("리뷰 내용"),
//                                fieldWithPath("data.content[0].likes").description("좋아요 수"),
//                                fieldWithPath("data.pageable").description("페이징 정보"),
//                                fieldWithPath("data.pageable.pageNumber").description("페이지 번호"),
//                                fieldWithPath("data.pageable.pageSize").description("페이지 크기"),
//                                fieldWithPath("data.pageable.sort").description("페이지 정렬 정보"),
//                                fieldWithPath("data.pageable.offset").description("페이지 오프셋"),
//                                fieldWithPath("data.pageable.paged").description("페이징 여부"),
//                                fieldWithPath("data.pageable.unpaged").description("페이징 되지 않았는지 여부"),
//                                fieldWithPath("data.pageable.sort").description("페이지 정렬 정보"),
//                                fieldWithPath("data.pageable.sort.empty").description("페이지 정렬 여부"),
//                                fieldWithPath("data.pageable.sort.sorted").description("페이지 정렬되었는지 여부"),
//                                fieldWithPath("data.pageable.sort.unsorted").description("페이지 정렬되지 않았는지 여부"),
//                                fieldWithPath("data.sort.empty").description("정렬 여부"),
//                                fieldWithPath("data.sort.sorted").description("정렬되었는지 여부"),
//                                fieldWithPath("data.sort.unsorted").description("정렬되지 않았는지 여부"),
//                                fieldWithPath("data.last").description("마지막 페이지 여부"),
//                                fieldWithPath("data.totalElements").description("총 요소 수"),
//                                fieldWithPath("data.totalPages").description("총 페이지 수"),
//                                fieldWithPath("data.size").description("페이지 크기"),
//                                fieldWithPath("data.number").description("현재 페이지 번호"),
//                                fieldWithPath("data.first").description("첫 페이지 여부"),
//                                fieldWithPath("data.numberOfElements").description("현재 페이지 요소 수"),
//                                fieldWithPath("data.empty").description("비어있는지 여부")
//                        )
//                ));
//    }



//    //LIKE관련//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("리뷰_리스트를_멤버_아이디로_최신순_또는_좋아요순_중_정렬을 선택해서_조회하면_" +
//            "해당_멤버의_리뷰_리스트가_조회되고_응답한다.")
//    public void getMemberReviewsControllerTest() throws Exception {
//        // given
//        Long memberId = 1L;
//        List<String> reviewImgUrls = new ArrayList<>();
//        reviewImgUrls.add("image0001.jpg");
//        reviewImgUrls.add("image0002.jpg");
//        reviewImgUrls.add("image0003.jpg");
//        ReviewWholeInfoDto mockReviewWholeInfoDto =
//                ReviewWholeInfoDto.builder()
//                        .memberName("멤버이름")
//                        .rating(5)
//                        .createdAt(LocalDateTime.now())
//                        .sellerName("MarketBridge")
//                        .productName("상품명")
//                        .reviewImgUrls(reviewImgUrls)
//                        .content("리뷰 내용")
//                        .likes(0L)
//                        .build();
//
//        // page 및 sortBy 설정
//        // 정렬 선택: createdAt 또는 likes
//        int page = 0;
//        String sortBy = "createdAt";
//        Pageable pageable = PageRequest.of(page, 5, Sort.by(sortBy).descending());
//
//        // 실제 서비스 메서드 호출 결과를 설정
//        Page<ReviewWholeInfoDto> mockReviewPage
//                = new PageImpl<>(Collections.singletonList(mockReviewWholeInfoDto), pageable, 1);
//        given(reviewService.getMemberReviews(memberId, pageable, sortBy)).willReturn(mockReviewPage);
//
//        // when // then
//        mockMvc.perform(get("/member/{memberId}/reviews", memberId)
//                        .param("memberId", String.valueOf(memberId))
//                        .param("page", String.valueOf(page))
//                        .param("sortBy", sortBy)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andDo(document("reviews-of-member",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        pathParameters(
//                                parameterWithName("memberId").description("멤버 ID")
//                        ),
//                        responseFields(
//                                fieldWithPath("code").description("응답 코드"),
//                                fieldWithPath("status").description("응답 상태"),
//                                fieldWithPath("message").description("응답 메시지"),
//                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
//                                fieldWithPath("data.content[0].memberName").description("멤버 이름"),
//                                fieldWithPath("data.content[0].rating").description("별점"),
//                                fieldWithPath("data.content[0].createdAt").description("리뷰 작성일시"),
//                                fieldWithPath("data.content[0].sellerName").description("셀러명"),
//                                fieldWithPath("data.content[0].productName").description("상품명"),
//                                fieldWithPath("data.content[0].reviewImgUrls").description("리뷰 이미지 URL"),
//                                fieldWithPath("data.content[0].content").description("리뷰 내용"),
//                                fieldWithPath("data.content[0].likes").description("좋아요 수"),
//                                fieldWithPath("data.pageable").description("페이징 정보"),
//                                fieldWithPath("data.pageable.pageNumber").description("페이지 번호"),
//                                fieldWithPath("data.pageable.pageSize").description("페이지 크기"),
//                                fieldWithPath("data.pageable.sort").description("페이지 정렬 정보"),
//                                fieldWithPath("data.pageable.offset").description("페이지 오프셋"),
//                                fieldWithPath("data.pageable.paged").description("페이징 여부"),
//                                fieldWithPath("data.pageable.unpaged").description("페이징 되지 않았는지 여부"),
//                                fieldWithPath("data.pageable.sort").description("페이지 정렬 정보"),
//                                fieldWithPath("data.pageable.sort.empty").description("페이지 정렬 여부"),
//                                fieldWithPath("data.pageable.sort.sorted").description("페이지 정렬되었는지 여부"),
//                                fieldWithPath("data.pageable.sort.unsorted").description("페이지 정렬되지 않았는지 여부"),
//                                fieldWithPath("data.sort.empty").description("정렬 여부"),
//                                fieldWithPath("data.sort.sorted").description("정렬되었는지 여부"),
//                                fieldWithPath("data.sort.unsorted").description("정렬되지 않았는지 여부"),
//                                fieldWithPath("data.last").description("마지막 페이지 여부"),
//                                fieldWithPath("data.totalElements").description("총 요소 수"),
//                                fieldWithPath("data.totalPages").description("총 페이지 수"),
//                                fieldWithPath("data.size").description("페이지 크기"),
//                                fieldWithPath("data.number").description("현재 페이지 번호"),
//                                fieldWithPath("data.first").description("첫 페이지 여부"),
//                                fieldWithPath("data.numberOfElements").description("현재 페이지 요소 수"),
//                                fieldWithPath("data.empty").description("비어있는지 여부")
//                        )
//                ));
//    }



    @Test
    @DisplayName("상품_아이디로_리뷰_총갯수를_조회하면_조회되고_응답한다.")
    public void getProductReviewsCountControllerTest() throws Exception {
        //given
        Long productId = 1L;
        ReviewsCountDto mockReviewsCountDto = ReviewsCountDto.builder().count(0L).build();
        given(reviewService.getProductReviewsCount(productId)).willReturn(mockReviewsCountDto);

        //when //then
        mockMvc.perform(get("/product/{productId}/reviews-count", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("reviews-count-of-product",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("productId").description("상품 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("status").description("응답 상태"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                fieldWithPath("data.count").description("리뷰 총 갯수")
                        )
                ));
    }



    @Test
    @WithMockCustomUser
    @DisplayName("멤버_아이디로_리뷰_총갯수를_조회하면_조회되고_응답한다.")
    public void getMemberReviewsCountControllerTest() throws Exception {
        //given
        Long memberId = 1L;
        ReviewsCountDto mockReviewsCountDto = ReviewsCountDto.builder().count(0L).build();
        given(reviewService.getMemberReviewsCount(memberId)).willReturn(mockReviewsCountDto);

        //when //then
        mockMvc.perform(get("/member/{memberId}/reviews-count", memberId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("reviews-count-of-member",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("memberId").description("멤버 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("status").description("응답 상태"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                fieldWithPath("data.count").description("리뷰 총 갯수")
                        )
                ));
    }



    @Test
    @WithMockCustomUser
    @DisplayName("리뷰를_수정하면_수정되고_응답한다.")
    public void updateReviewControllerTest() throws Exception {
        //given
        // 리뷰 수정에 필요한 요청 데이터
        Long reviewId = 1L;

        List<ReviewSurveyDataDto> updatedReviewSurveyDataDtoList = new ArrayList<>();
        ReviewSurveyDataDto updatedReviewSurveyDataDto1 = ReviewSurveyDataDto.builder()
                .reviewSurveyCategoryData("리뷰서베이카테고리1(질문1)")
                .writtenOrSelectedSurveyContentData("수정된 작성한 내용1")
                .build();
        ReviewSurveyDataDto updatedReviewSurveyDataDto2 = ReviewSurveyDataDto.builder()
                .reviewSurveyCategoryData("리뷰서베이카테고리2(질문2)")
                .writtenOrSelectedSurveyContentData("수정된 작성한 내용2")
                .build();
        ReviewSurveyDataDto updatedReviewSurveyDataDto3 = ReviewSurveyDataDto.builder()
                .reviewSurveyCategoryData("리뷰서베이카테고리2(질문3)")
                .writtenOrSelectedSurveyContentData("수정된 선택한 내용3")
                .build();
        ReviewSurveyDataDto updatedReviewSurveyDataDto4 = ReviewSurveyDataDto.builder()
                .reviewSurveyCategoryData("리뷰서베이카테고리4(질문4)")
                .writtenOrSelectedSurveyContentData("수정된 선택한 내용4")
                .build();
        updatedReviewSurveyDataDtoList.add(updatedReviewSurveyDataDto1);
        updatedReviewSurveyDataDtoList.add(updatedReviewSurveyDataDto2);
        updatedReviewSurveyDataDtoList.add(updatedReviewSurveyDataDto3);
        updatedReviewSurveyDataDtoList.add(updatedReviewSurveyDataDto4);

        List<String> reviewImgUrls = new ArrayList<>();
        reviewImgUrls.add("image0003.jpg");
        reviewImgUrls.add("image0004.jpg");
        reviewImgUrls.add("image0005.jpg");
        ReviewModifiableValuesDto request = ReviewModifiableValuesDto.builder()
                .rating(4)
                .reviewSurveyDataDtoList(updatedReviewSurveyDataDtoList)
                .content("수정된리뷰내용")
                .reviewImgUrls(reviewImgUrls)
                .summary("한줄요약")
                .build();
        // 리뷰 수정 서비스 메서드의 반환값을 목 객체로 설정
        ReviewIdDto updatedReviewIdDto = ReviewIdDto.builder().reviewId(reviewId).build();
        given(reviewService.updateReview(any(ReviewModifiableValuesDto.class), eq(reviewId), anyLong()))
                .willReturn(updatedReviewIdDto);

        //when //then
        mockMvc.perform(patch("/review/{reviewId}", reviewId) // PATCH 요청으로 수정
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))) // 요청 바디에 수정 정보를 포함
                .andExpect(status().isOk())
                .andDo(document("review-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("reviewId").description("리뷰 ID")
                        ),
                        requestFields( // 요청 바디에 대한 문서화
                                fieldWithPath("rating").description("수정할 별점"),
                                fieldWithPath("reviewSurveyDataDtoList").description("수정할 리뷰 서베이 데이터 리스트(카테고리, 선택 또는 입력된 내용 의 데이터 리스트)")
                                        .type(JsonFieldType.ARRAY).optional(), // 필드가 선택적인 경우 optional()을 사용
                                fieldWithPath("reviewSurveyDataDtoList[].reviewSurveyCategoryData").description("수정할 리뷰 서베이 카테고리 데이터"),
                                fieldWithPath("reviewSurveyDataDtoList[].writtenOrSelectedSurveyContentData").description("수정할 입력 또는 선택된 내용 데이터"),
                                fieldWithPath("content").description("수정할 리뷰내용"),
                                fieldWithPath("reviewImgUrls").description("수정할 리뷰 이미지 URL 리스트"),
                                fieldWithPath("summary").description("수정할 한줄요약")
                        ),
                        responseFields( // 응답에 대한 문서화
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("status").description("응답 상태"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                fieldWithPath("data.reviewId").description("수정된 리뷰의 ID")
                        )
                ));
    }



    @Test
    @WithMockCustomUser
    @DisplayName("리뷰를_삭제하면_삭제되고_응답한다.")
    public void deleteReviewControllerTest() throws Exception {
        //given
        Long reviewId = 1L;

        //when //then
        mockMvc.perform(delete("/review/{reviewId}", reviewId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("review-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("reviewId").description("삭제할 리뷰의 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("status").description("응답 상태"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.VARIES).description("응답 데이터")
                        )
                ));
    }


//    //LIKE관련//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("리뷰에_좋아요_상태를_변경하면_좋아요_상태가_변경_되고_응답한다.")
//    public void addOrChangeReviewLikeControllerTest() throws Exception {
//    //given
//        ReviewLikeDto mockResponse = ReviewLikeDto.builder()
//                .reviewId(1L)
//                .memberId(123L)
//                .liked(true)
//                .build();
//        given(reviewService.addOrChangeReviewLike(anyLong(), anyLong())).willReturn(mockResponse);
//
//    //when
//        mockMvc.perform(post("/review/1/like")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"reviewId\":1,\"memberId\":123,\"liked\":true}")
//                        .header("Authorization", "bearer AccessToken")) // 액세스 토큰
//                //then
//                .andExpect(status().isOk())
//                .andDo(document("review-add-or-change-review-like",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        requestFields(
//                                fieldWithPath("reviewId").description("리뷰 ID"),
//                                fieldWithPath("memberId").description("회원 ID"),
//                                fieldWithPath("liked").description("좋아요 상태")
//                        ),
//                        responseFields(
//                                fieldWithPath("code").description("응답 코드"),
//                                fieldWithPath("status").description("응답 상태"),
//                                fieldWithPath("message").description("응답 메시지"),
//                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
//                                fieldWithPath("data.reviewId").description("리뷰 ID"),
//                                fieldWithPath("data.memberId").description("멤버 ID"),
//                                fieldWithPath("data.liked").description("좋아요 상태")
//                        )
//                ));
//    }


//    //LIKE관련//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("리뷰의_좋아요_총갯수를_구하면_총갯수가_카운트되고_응답한다.")
//    public void countReviewLikesControllerTest() throws Exception {
//        // given
//        Long reviewId = 1L;
//        ReviewLikesCountDto mockResponse = ReviewLikesCountDto.builder()
//                .reviewId(reviewId)
//                .count(10L) // 임의의 좋아요 수
//                .build();
//        given(reviewService.countReviewLikes(reviewId)).willReturn(mockResponse);
//
//        // when // then
//        mockMvc.perform(get("/review/{reviewId}/likes/count", reviewId))
//                .andExpect(status().isOk())
////                .andExpect(jsonPath("$.data.reviewId").value(reviewId))
////                .andExpect(jsonPath("$.data.count").value(10L))
//                .andDo(document("review-count-review-likes",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        pathParameters(
//                                parameterWithName("reviewId").description("리뷰 ID")
//                        ),
//                        responseFields(
//                                fieldWithPath("code").description("응답 코드"),
//                                fieldWithPath("status").description("응답 상태"),
//                                fieldWithPath("message").description("응답 메시지"),
//                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
//                                fieldWithPath("data.reviewId").description("리뷰 ID"),
//                                fieldWithPath("data.count").description("좋아요 수")
//                        )
//                ));
//    }
}
