package com.objects.marketbridge.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.objects.marketbridge.category.domain.Category;
import com.objects.marketbridge.common.security.config.SpringSecurityTestConfig;
import com.objects.marketbridge.product.mock.FakeProductRepository;
import com.objects.marketbridge.product.controller.request.CreateProductRequestDto;
import com.objects.marketbridge.product.domain.Product;
import com.objects.marketbridge.product.dto.ProductSimpleDto;
import com.objects.marketbridge.product.mock.FakeCategoryRepository;
import com.objects.marketbridge.product.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(ProductController.class)
@ContextConfiguration(classes = {SpringSecurityTestConfig.class})
class ProductControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockBean private ProductService productService;
    @Autowired private ObjectMapper objectMapper;
    @MockBean(name = "productRepository")
    FakeProductRepository productRepository;
    @MockBean(name = "categoryRepository")
    FakeCategoryRepository categoryRepository;

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
                .andExpect(status().isOk());
    }

    @DisplayName("신규 상품을 등록한다.")
    @Test
    void createProduct() throws Exception{
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
                .andExpect(status().isOk());
    }
}