package com.objects.marketbridge.product.controller.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.objects.marketbridge.category.domain.Category;
import com.objects.marketbridge.product.controller.ProductController;
import com.objects.marketbridge.product.controller.request.CreateProductRequestDto;
import com.objects.marketbridge.product.domain.Product;
import com.objects.marketbridge.product.dto.ProductSimpleDto;
import com.objects.marketbridge.product.mock.FakeCategoryRepository;
import com.objects.marketbridge.product.mock.FakeProductRepository;
import com.objects.marketbridge.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@WebMvcTest(ProductController.class)
@ExtendWith(RestDocumentationExtension.class)
class ProductControllerRestDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @MockBean(name = "productRepository")
    FakeProductRepository productRepository;
    @MockBean(name = "categoryRepository")
    FakeCategoryRepository categoryRepository;


    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider provider){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(provider))
                .build();
    }

    @Test
    @DisplayName("카테고리별 상품을 조회한다.")
    void getProductByCategory() throws Exception {
        //given
        PageRequest pageRequest = PageRequest.of(0,5);

        categoryRepository.save(Category.builder()
                .parentId(2L)
                .name("TEST CATE")
                .level(1L)
                .build());

        Product product1 = Product.builder()
                .id(1L)
                .productNo("12343213-3423333")
                .isOwn(false)
                .price(10000L)
                .name("TEST PRODUCT1")
                .isSubs(true)
                .stock(1000L)
                .thumbImg("MAIN_IMG1.com")
                .discountRate(12L)
                .build();
        product1.setCategory(categoryRepository.findById(1L));
        productRepository.save(product1);

        Product product2 = Product.builder()
                .id(2L)
                .isOwn(true)
                .name("TEST PRODUCT2")
                .stock(10030L)
                .isSubs(false)
                .price(1001L)
                .thumbImg("MAIN_IMG111.com")
                .discountRate(14L)
                .productNo("12343213-3423434")
                .build();

        product2.setCategory(categoryRepository.findById(1L));
        productRepository.save(product2);

        List<ProductSimpleDto> contents = List.of(ProductSimpleDto.of(product1),ProductSimpleDto.of(product2));

        given(productService.getProductByCategory(any(PageRequest.class),anyString()))
                .willReturn(new PageImpl<>(contents,pageRequest,2));

        //when //then
        mockMvc.perform(get("/product")
                        .param("size","60")
                        .param("page","1")
                        .param("categoryCode","1"
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("product-search-by-category",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("size").description("한 페이지에 보여질 상품 갯수"),
                                parameterWithName("page").description("조회 페이지 숫자"),
                                parameterWithName("categoryCode").description("카테고리 코드 숫자, 해당 카테고리에 속하는 상품 목록 예) 여성패션=9")
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
                                fieldWithPath("data.content[].productId").type(JsonFieldType.NUMBER)
                                        .description("상품 아이디"),
                                fieldWithPath("data.content[].prodNo").type(JsonFieldType.STRING)
                                        .description("상품 고유 번호"),
                                fieldWithPath("data.content[].name").type(JsonFieldType.STRING)
                                        .description("상품 이름"),
                                fieldWithPath("data.content[].price").type(JsonFieldType.NUMBER)
                                        .description("상품 가격 (할인 전)"),
                                fieldWithPath("data.content[].discountRate").type(JsonFieldType.NUMBER)
                                        .description("상품 할인율"),
                                fieldWithPath("data.content[].thumUrl").type(JsonFieldType.STRING)
                                        .description("상품 썸네일 이미지 url"),
                                fieldWithPath("data.content[].isOwn").type(JsonFieldType.BOOLEAN)
                                        .description("마켓브릿지 상품인지 입점 판매자 상품인지 판별하는 값"),
                                fieldWithPath("data.content[].stock").type(JsonFieldType.NUMBER)
                                        .description("상품 재고"),

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
                        )
                ));
    }

    @Test
    @DisplayName("상품 등록 api 최종 테스트")
    void create_new_product() throws Exception{
        //given
        List<String> itemImgUrls = new ArrayList<>();
        List<String> detailImgUrls = new ArrayList<>();
        detailImgUrls.addAll(List.of("https://thumbnail7.coupangcdn.com/thumbnails/remote/q89/image/retail/images/2022/10/19/18/6/2e87b707-770c-4e54-9094-bcedfb9d540c.jpg",
                "https://thumbnail6.coupangcdn.com/thumbnails/remote/q89/image/rs_quotation_api/weibrfc9/b793564fe3da48808e4234e7277ff97e.jpg"));
        itemImgUrls.addAll(List.of("https://thumbnail10.coupangcdn.com/thumbnails/remote/492x492ex/image/rs_quotation_api/xael3d49/cd83cb8368d240a0aec9fea44270493f.jpg",
                "https://thumbnail10.coupangcdn.com/thumbnails/remote/492x492ex/image/rs_quotation_api/ribx3yvf/69a7a9f477054210a69a40709d09995f.jpg"));
        Map<String,String> optionInfo =  new HashMap<>();
        Map<String,String> tagInfo = new HashMap<>();
        optionInfo.put("사이즈","M");
        optionInfo.put("색상","네이비");
        tagInfo.put("출시 연도","2022년도");
        tagInfo.put("소재","폴리에스터/나일론");

        CreateProductRequestDto request = CreateProductRequestDto.builder()
                .categoryId(1L)
                .isOwn(false)
                .thumbImg("https://thumbnail9.coupangcdn.com/thumbnails/remote/492x492ex/image/rs_quotation_api/yh93aogx/73e66303ce794816a9659a4684d53c08.jpg")
                .productNo("6854390244 - 16340644607")
                .name("ExampleProduct")
                .discountRate(14L)
                .price(1300L)
                .stock(100L)
                .itemImgUrls(itemImgUrls)
                .detailImgUrls(detailImgUrls)
                .tagInfo(tagInfo)
                .optionInfo(optionInfo)
                .isSubs(false)
                .build();

        given(productService.create(any(CreateProductRequestDto.class))).willReturn(1L);

        //when //then
        mockMvc.perform(post("/product/new")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "bearer AccessToken")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("product-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("categoryId").type(JsonFieldType.NUMBER)
                                                .description("카테고리 아이디"),
                                fieldWithPath("isOwn").type(JsonFieldType.BOOLEAN)
                                                .description("마켓브릿지 상품 여부"),
                                fieldWithPath("name").type(JsonFieldType.STRING)
                                                .description("상품명"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER)
                                                .description("상품 가격"),
                                fieldWithPath("isSubs").type(JsonFieldType.BOOLEAN)
                                                .description("구독 가능 상품 여부"),
                                fieldWithPath("stock").type(JsonFieldType.NUMBER)
                                                .description("상품 재고"),
                                fieldWithPath("thumbImg").type(JsonFieldType.STRING)
                                                .description("메인 이미지 url"),
                                fieldWithPath("itemImgUrls").type(JsonFieldType.ARRAY)
                                                .description("상품 이미지 url리스트"),
                                fieldWithPath("detailImgUrls").type(JsonFieldType.ARRAY)
                                                .description("상품 설명 이미지 url리스트"),
                                fieldWithPath("discountRate").type(JsonFieldType.NUMBER)
                                                .description("상품 할인율"),
                                fieldWithPath("productNo").type(JsonFieldType.STRING)
                                                .description("상품 관리 번호"),
                                fieldWithPath("optionInfo").type(JsonFieldType.OBJECT)
                                                .description("상품의 옵션 정보"),
                                fieldWithPath("optionInfo.색상").type(JsonFieldType.STRING)
                                        .description("상품의 색상 옵션 정보"),
                                fieldWithPath("optionInfo.사이즈").type(JsonFieldType.STRING)
                                        .description("상품의 사이즈 옵션 정보"),
                                fieldWithPath("tagInfo").type(JsonFieldType.OBJECT)
                                        .description("상품의 태그 정보"),
                                fieldWithPath("tagInfo.출시 연도").type(JsonFieldType.STRING)
                                        .description("상품의 출시 연도 태그 정보"),
                                fieldWithPath("tagInfo.소재").type(JsonFieldType.STRING)
                                        .description("상품의 소재 태그 정보")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("상태"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지"),
                                fieldWithPath("data").type(JsonFieldType.NUMBER)
                                        .description("응답 데이터, 등록된 상품 id 값")
                        )
                        )
                );
    }



}