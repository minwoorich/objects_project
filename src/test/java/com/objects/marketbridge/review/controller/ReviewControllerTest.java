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
import org.springframework.http.HttpHeaders;
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
import static org.mockito.BDDMockito.willDoNothing;
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


//    @Test
//    @WithMockCustomUser
//    @DisplayName("리뷰_서베이_선택창_조회를_요청받으면_선택창이_조회되고_응답한다.")
//    public void getReviewSurveyQuestionAndOptionsListControllerTest() throws Exception {
//        //given
//        Long productId = 1L;
//        List<ReviewSurveyQuestionAndOptionsDto> mockReviewSurveyQuestionAndOptionsDtoList = new ArrayList<>();
//        ReviewSurveyQuestionAndOptionsDto mockReviewSurveyQuestionAndOptionsDto1
//                = ReviewSurveyQuestionAndOptionsDto.builder()
//                .reviewSurveyQuestion("질문1")
//                .build();
//        ReviewSurveyQuestionAndOptionsDto mockReviewSurveyQuestionAndOptionsDto2
//                = ReviewSurveyQuestionAndOptionsDto.builder()
//                .reviewSurveyQuestion("질문2")
//                .build();
//
//        List<String> mockeviewSurveyOptionList3
//                = new ArrayList<>();
//        mockeviewSurveyOptionList3.add("선택지3-1");
//        mockeviewSurveyOptionList3.add("선택지3-2");
//        mockeviewSurveyOptionList3.add("선택지3-3");
//        mockeviewSurveyOptionList3.add("선택지3-4");
//        ReviewSurveyQuestionAndOptionsDto mockReviewSurveyQuestionAndOptionsDto3
//                = ReviewSurveyQuestionAndOptionsDto.builder()
//                .reviewSurveyQuestion("질문3")
//                .reviewSurveyOptionList(mockeviewSurveyOptionList3)
//                .build();
//
//        List<String> mockeviewSurveyOptionList4
//                = new ArrayList<>();
//        mockeviewSurveyOptionList4.add("선택지4-1");
//        mockeviewSurveyOptionList4.add("선택지4-2");
//        mockeviewSurveyOptionList4.add("선택지4-3");
//        mockeviewSurveyOptionList4.add("선택지4-4");
//        ReviewSurveyQuestionAndOptionsDto mockReviewSurveyQuestionAndOptionsDto4
//                = ReviewSurveyQuestionAndOptionsDto.builder()
//                .reviewSurveyQuestion("질문4")
//                .reviewSurveyOptionList(mockeviewSurveyOptionList4)
//                .build();
//        mockReviewSurveyQuestionAndOptionsDtoList.add(mockReviewSurveyQuestionAndOptionsDto1);
//        mockReviewSurveyQuestionAndOptionsDtoList.add(mockReviewSurveyQuestionAndOptionsDto2);
//        mockReviewSurveyQuestionAndOptionsDtoList.add(mockReviewSurveyQuestionAndOptionsDto3);
//        mockReviewSurveyQuestionAndOptionsDtoList.add(mockReviewSurveyQuestionAndOptionsDto4);
//
//        given(reviewService.getReviewSurveyQuestionAndOptionsList(productId))
//                .willReturn(mockReviewSurveyQuestionAndOptionsDtoList);
//
//
//        mockMvc.perform(get("/review-survey-options/{productId}", 1L))
//                .andExpect(status().isOk())
//                .andDo(document("review-get-survey-options",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        pathParameters(parameterWithName("productId").description("상품 ID")),
//                        responseFields(
//                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
//                                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태"),
//                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
//                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("응답데이터(리뷰서베이 질문과 선택지들)"),
//                                subsectionWithPath("data[]").type(JsonFieldType.ARRAY).description("응답데이터(리뷰서베이 질문과 선택지들)"),
//                                fieldWithPath("data[].reviewSurveyQuestion").type(JsonFieldType.STRING).description("리뷰서베이 질문"),
//                                fieldWithPath("data[].reviewSurveyOptionList").type(JsonFieldType.ARRAY).optional().description("리뷰서베이 선택지들(null인경우 선택아닌 리뷰작성자가 직접 작성 요함"),
//                                fieldWithPath("data[].reviewSurveyOptionList[]").type(JsonFieldType.ARRAY).optional().description("리뷰서베이 선택지들(null인경우 선택아닌 리뷰작성자가 직접 작성 요함")
//                        )
//                ));
//    }



    @Test
    @WithMockCustomUser
    @DisplayName("리뷰 생성")
    public void createReview() throws Exception {
        //given
        // 리뷰 생성에 필요한 요청 데이터
        CreateReviewDto request = getCreateReviewDto();
        willDoNothing().given(reviewService).createReview(any(CreateReviewDto.class), anyLong());

        //when //then
        // API 요청 및 응답 문서화
        String jsonContent = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent)
                        .header(HttpHeaders.AUTHORIZATION, "bearer AccessToken")) // 액세스 토큰
                .andExpect(status().isCreated())
                .andDo(document("review-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("productId").description("상품 ID"),
                                fieldWithPath("rating").description("별점"),
                                fieldWithPath("summary").description("한줄요약"),
                                fieldWithPath("content").description("리뷰내용"),
                                fieldWithPath("reviewSurveys").description("리뷰 서베이 데이터 리스트(카테고리, 선택 또는 입력된 내용 의 데이터 리스트)")
                                        .type(JsonFieldType.ARRAY).optional(), // 필드가 선택적인 경우 optional()을 사용
                                fieldWithPath("reviewSurveys[].reviewSurveyCategoryId").description("리뷰 서베이 카테고리 아이디"),
                                fieldWithPath("reviewSurveys[].reviewSurveyCategoryName").description("리뷰 서베이 카테고리 이름"),
                                fieldWithPath("reviewSurveys[].content").description("선택하거나 입력한 내용"),
                                fieldWithPath("reviewImgUrls").description("리뷰 이미지 URL 목록").type(JsonFieldType.ARRAY).optional(),
                                fieldWithPath("reviewImgUrls[].seqNo").description("이미지 순번"),
                                fieldWithPath("reviewImgUrls[].imgUrl").description("이미지 주소"),
                                fieldWithPath("reviewImgUrls[].description").description("이미지 설명")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("status").description("응답 상태"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.NULL).description("응답 데이터")
                        )
                ));
    }

    private CreateReviewDto getCreateReviewDto() {
        List<CreateReviewSurveyDto> reviewSurveys = new ArrayList<>();
        CreateReviewSurveyDto reviewSurvey1 = CreateReviewSurveyDto.builder()
                .reviewSurveyCategoryId(1L)
                .reviewSurveyCategoryName("키")
                .content("163")
                .build();
        CreateReviewSurveyDto reviewSurvey2 = CreateReviewSurveyDto.builder()
                .reviewSurveyCategoryId(2L)
                .reviewSurveyCategoryName("평소사이즈")
                .content("-")
                .build();
        CreateReviewSurveyDto reviewSurvey3 = CreateReviewSurveyDto.builder()
                .reviewSurveyCategoryId(3L)
                .reviewSurveyCategoryName("색상")
                .content("화면과같아요")
                .build();
        CreateReviewSurveyDto reviewSurvey4 = CreateReviewSurveyDto.builder()
                .reviewSurveyCategoryId(4L)
                .reviewSurveyCategoryName("사이즈")
                .content("딱맞아요")
                .build();
        reviewSurveys.add(reviewSurvey1);
        reviewSurveys.add(reviewSurvey2);
        reviewSurveys.add(reviewSurvey3);
        reviewSurveys.add(reviewSurvey4);

        List<ReviewImageDto> reviewImgUrls = getReviewImageDtos();

        return CreateReviewDto.builder()
                .productId(1L)
                .rating(5)
                .reviewSurveys(reviewSurveys)
                .content("리뷰내용")
                .reviewImgUrls(reviewImgUrls)
                .summary("한줄요약")
                .build();
    }

    private List<ReviewImageDto> getReviewImageDtos() {
        List<ReviewImageDto> reviewImgUrls = new ArrayList<>();
        ReviewImageDto image1 = ReviewImageDto.builder().seqNo(1L).imgUrl("001.jpg").description("캡션1").build();
        ReviewImageDto image2 = ReviewImageDto.builder().seqNo(2L).imgUrl("002.jpg").description("캡션1").build();
        ReviewImageDto image3 = ReviewImageDto.builder().seqNo(3L).imgUrl("003.jpg").description("캡션1").build();

        reviewImgUrls.add(image1);
        reviewImgUrls.add(image2);
        reviewImgUrls.add(image3);
        return reviewImgUrls;
    }

    @Test
    @WithMockCustomUser
    @DisplayName("리뷰 수정")
    public void updateReview() throws Exception {
        //given
        UpdateReviewDto updateReviewDto = getUpdateReviewDto();
        // 리뷰 수정 서비스 메서드의 반환값을 목 객체로 설정
        willDoNothing().given(reviewService).updateReview(any(UpdateReviewDto.class));

        //when //then
        mockMvc.perform(patch("/review") // PATCH 요청으로 수정
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "bearer AccessToken")
                        .content(objectMapper.writeValueAsString(updateReviewDto))) // 요청 바디에 수정 정보를 포함

                .andExpect(status().isOk())
                .andDo(document("review-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("reviewId").description("수정 요청한 리뷰아이디"),
                                fieldWithPath("rating").description("수정한 별점"),
                                fieldWithPath("summary").description("수정한 한줄요약"),
                                fieldWithPath("content").description("수정한 리뷰내용"),
                                fieldWithPath("updateReviewSurveys").description("리뷰 서베이 데이터 리스트(카테고리, 선택 또는 입력된 내용 의 데이터 리스트)")
                                        .type(JsonFieldType.ARRAY).optional(), // 필드가 선택적인 경우 optional()을 사용
                                fieldWithPath("updateReviewSurveys[].reviewSurveyId").description("리뷰 서베이 아이디(리뷰 조회때 아이디 제공)"),
                                fieldWithPath("updateReviewSurveys[].content").description("선택을 변경하거나 입력을 수정한 내용"),
                                fieldWithPath("reviewImgUrls").description("리뷰 이미지 URL 목록 - 이 목록에 없는 이미지는 데이터베이스에서 삭제됨.").type(JsonFieldType.ARRAY).optional(),
                                fieldWithPath("reviewImgUrls[].seqNo").description("이미지 순번"),
                                fieldWithPath("reviewImgUrls[].imgUrl").description("이미지 주소"),
                                fieldWithPath("reviewImgUrls[].description").description("이미지 설명")
                        ),
                        responseFields( // 응답에 대한 문서화
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("status").description("응답 상태"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.NULL).description("응답 데이터")
                        )
                ));
    }

    private UpdateReviewDto getUpdateReviewDto() {
        List<UpdateReviewSurveyDto> updateReviewSurveys = new ArrayList<>();
        UpdateReviewSurveyDto reviewSurvey1 = UpdateReviewSurveyDto.builder()
                .reviewSurveyId(1L)
                .content("163")
                .build();
        UpdateReviewSurveyDto reviewSurvey2 = UpdateReviewSurveyDto.builder()
                .reviewSurveyId(2L)
                .content("-")
                .build();
        UpdateReviewSurveyDto reviewSurvey3 = UpdateReviewSurveyDto.builder()
                .reviewSurveyId(3L)
                .content("화면과같아요")
                .build();
        UpdateReviewSurveyDto reviewSurvey4 = UpdateReviewSurveyDto.builder()
                .reviewSurveyId(4L)
                .content("딱맞아요")
                .build();
        updateReviewSurveys.add(reviewSurvey1);
        updateReviewSurveys.add(reviewSurvey2);
        updateReviewSurveys.add(reviewSurvey3);
        updateReviewSurveys.add(reviewSurvey4);

        List<ReviewImageDto> reviewImgUrls = getReviewImageDtos();

        return UpdateReviewDto.builder()
                .reviewId(1L)
                .rating(5)
                .updateReviewSurveys(updateReviewSurveys)
                .content("수정한 리뷰내용")
                .reviewImgUrls(reviewImgUrls)
                .summary("수정한 한줄요약")
                .build();

    }

    @Test
    @WithMockCustomUser
    @DisplayName("리뷰 삭제")
    public void deleteReview() throws Exception {
        //given
        Long reviewId = 1L;

        willDoNothing().given(reviewService).deleteReview(reviewId);
        //when //then
        mockMvc.perform(delete("/review/{reviewId}", reviewId)
                        .header(HttpHeaders.AUTHORIZATION, "bearer AccessToken")
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
                                fieldWithPath("data").type(JsonFieldType.NULL).description("응답 데이터")
                        )
                ));
    }

//    @Test
//    @WithMockCustomUser
//    @DisplayName("리뷰_아이디로_리뷰_단건_조회하면_조회되고_응답한다.")
//    public void getReviewControllerTest() throws Exception {
//        //given
//        Long reviewId = 1L;
//
//        List<ReviewSurveyDto> reviewSurveyDtoList = new ArrayList<>();
//        ReviewSurveyDto reviewSurveyDto1 = ReviewSurveyDto.builder()
//                .reviewSurveyCategoryData("리뷰서베이카테고리1(질문1)")
//                .writtenOrSelectedSurveyContentData("작성한 내용1")
//                .build();
//        ReviewSurveyDto reviewSurveyDto2 = ReviewSurveyDto.builder()
//                .reviewSurveyCategoryData("리뷰서베이카테고리2(질문2)")
//                .writtenOrSelectedSurveyContentData("작성한 내용2")
//                .build();
//        ReviewSurveyDto reviewSurveyDto3 = ReviewSurveyDto.builder()
//                .reviewSurveyCategoryData("리뷰서베이카테고리2(질문3)")
//                .writtenOrSelectedSurveyContentData("선택한 내용3")
//                .build();
//        ReviewSurveyDto reviewSurveyDto4 = ReviewSurveyDto.builder()
//                .reviewSurveyCategoryData("리뷰서베이카테고리4(질문4)")
//                .writtenOrSelectedSurveyContentData("선택한 내용4")
//                .build();
//        reviewSurveyDtoList.add(reviewSurveyDto1);
//        reviewSurveyDtoList.add(reviewSurveyDto2);
//        reviewSurveyDtoList.add(reviewSurveyDto3);
//        reviewSurveyDtoList.add(reviewSurveyDto4);
//
//        List<String> reviewImgUrls = new ArrayList<>();
//        reviewImgUrls.add("image0001.jpg");
//        reviewImgUrls.add("image0002.jpg");
//        reviewImgUrls.add("image0003.jpg");
//        ReviewSingleReadDto mockReviewSingleReadDto = ReviewSingleReadDto.builder()
//                .reviewId(reviewId)
//                .memberId(1L)
//                .productId(1L)
//                .rating(5)
//                .reviewSurveyDataDtoList(reviewSurveyDtoList)
//                .content("리뷰 내용")
//                .reviewImgUrls(reviewImgUrls)
//                .build();
//        given(reviewService.getReview(anyLong(), anyLong())).willReturn(mockReviewSingleReadDto);
//
//        //when //then
//        mockMvc.perform(get("/review/{reviewId}", reviewId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", "bearer AccessToken")) // 액세스 토큰
//                .andExpect(status().isOk())
//                .andDo(document("review-get-single-review",
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
//                                fieldWithPath("data.reviewId").description("조회된 리뷰의 ID"),
//                                fieldWithPath("data.memberId").description("조회된 리뷰의 멤버 ID"),
//                                fieldWithPath("data.productId").description("조회된 리뷰의 상품 ID"),
//                                fieldWithPath("data.rating").description("조회된 리뷰의 별점"),
//                                fieldWithPath("data.reviewSurveyDataDtoList").description("조회된 리뷰 서베이 데이터 리스트(카테고리, 선택 또는 입력된 내용 의 데이터 리스트)")
//                                        .type(JsonFieldType.ARRAY).optional(), // 필드가 선택적인 경우 optional()을 사용
//                                fieldWithPath("data.reviewSurveyDataDtoList[].reviewSurveyCategoryData").description("조회된 리뷰 서베이 카테고리 데이터"),
//                                fieldWithPath("data.reviewSurveyDataDtoList[].writtenOrSelectedSurveyContentData").description("조회된 입력 또는 선택된 내용 데이터"),
//                                fieldWithPath("data.content").description("조회된 리뷰의 내용"),
//                                fieldWithPath("data.reviewImgUrls").description("조회된 리뷰의 리뷰 이미지 URL들")
//                        )
//                ));
//    }


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


//
//    @Test
//    @DisplayName("상품_아이디로_리뷰_총갯수를_조회하면_조회되고_응답한다.")
//    public void getProductReviewsCountControllerTest() throws Exception {
//        //given
//        Long productId = 1L;
//        ReviewsCountDto mockReviewsCountDto = ReviewsCountDto.builder().count(0L).build();
//        given(reviewService.getProductReviewsCount(productId)).willReturn(mockReviewsCountDto);
//
//        //when //then
//        mockMvc.perform(get("/product/{productId}/reviews-count", productId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andDo(document("reviews-count-of-product",
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
//                                fieldWithPath("data.count").description("리뷰 총 갯수")
//                        )
//                ));
//    }



//    @Test
//    @WithMockCustomUser
//    @DisplayName("멤버_아이디로_리뷰_총갯수를_조회하면_조회되고_응답한다.")
//    public void getMemberReviewsCountControllerTest() throws Exception {
//        //given
//        Long memberId = 1L;
//        ReviewsCountDto mockReviewsCountDto = ReviewsCountDto.builder().count(0L).build();
//        given(reviewService.getMemberReviewsCount(memberId)).willReturn(mockReviewsCountDto);
//
//        //when //then
//        mockMvc.perform(get("/member/{memberId}/reviews-count", memberId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andDo(document("reviews-count-of-member",
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
//                                fieldWithPath("data.count").description("리뷰 총 갯수")
//                        )
//                ));
//    }










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
