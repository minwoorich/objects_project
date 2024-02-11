package com.objects.marketbridge.category.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.objects.marketbridge.category.dto.CategoryDto;
import com.objects.marketbridge.category.service.CategoryService;
import com.objects.marketbridge.common.security.annotation.WithMockCustomUser;
import com.objects.marketbridge.common.security.config.SpringSecurityTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = {SpringSecurityTestConfig.class})
@ExtendWith(RestDocumentationExtension.class)
public class CategoryControllerTest {

    @MockBean
    CategoryService categoryService;
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
    @DisplayName("전체_조회:_카테고리_전체를_조회하면_최상위카테고리부터_하위_및_하위의_하위_카테고리들이_전부_조회되고_응답한다.")
    public void testGetTotalCategories() throws Exception {
        // given
        List<CategoryDto> categories = (List<CategoryDto>)Arrays.asList(
                CategoryDto.builder().id(1L).level(1L).parentId(null).name("Category 11").childCategories(new ArrayList<>()).build(),

                CategoryDto.builder().id(2L).level(1L).parentId(null).name("Category 21")
                        .childCategories(
                                (List<CategoryDto>)Arrays.asList(CategoryDto.builder().id(3L).level(2L).parentId(2L).name("Category 32")
                                        .childCategories(new ArrayList<>()).build()
                                )
                        ).build(),
                CategoryDto.builder().id(4L).level(1L).parentId(null).name("Category 41")
                        .childCategories(
                                (List<CategoryDto>)Arrays.asList(CategoryDto.builder().id(5L).level(2L).parentId(4L).name("Category 52")
                                        .childCategories(
                                        (List<CategoryDto>)Arrays.asList(CategoryDto.builder().id(6L).level(3L).parentId(5L).name("Category 63")
                                                .childCategories(new ArrayList<>()).build()
                                        )
                                        ).build())
                        ).build()
        );
        given(categoryService.getTotalCategories()).willReturn(categories);

        // when // then
        mockMvc.perform(get("/categories/getTotalCategories"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("categories-getTotalCategories",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].id").description("카테고리 ID"),
                                fieldWithPath("[].parentId").description("부모 카테고리 ID, 최상위 카테고리는 null->0."),
                                fieldWithPath("[].level").description("카테고리 레벨"),
                                fieldWithPath("[].name").description("카테고리 이름"),
                                fieldWithPath("[].childCategories").description("하위 카테고리의 목록"),
                                fieldWithPath("[].childCategories[]").description("하위 카테고리의 목록"),
                                fieldWithPath("[].childCategories[].id").description("하위 카테고리의 ID"),
                                fieldWithPath("[].childCategories[].parentId").description("하위 카테고리의 상위 카테고리 ID"),
                                fieldWithPath("[].childCategories[].level").description("하위 카테고리의 레벨"),
                                fieldWithPath("[].childCategories[].name").description("하위 카테고리의 이름"),
                                fieldWithPath("[].childCategories[].childCategories").description("하위의 하위 카테고리 목록"),
                                fieldWithPath("[].childCategories[].childCategories[]").description("하위의 하위 카테고리 목록"),
                                fieldWithPath("[].childCategories[].childCategories[].id").description("하위의 하위 카테고리의 ID"),
                                fieldWithPath("[].childCategories[].childCategories[].parentId").description("하위의 하위 카테고리의 상위 카테고리 ID"),
                                fieldWithPath("[].childCategories[].childCategories[].level").description("하위의 하위 카테고리 목록"),
                                fieldWithPath("[].childCategories[].childCategories[].name").description("하의의 하위 하위 카테고리 이름"),
                                fieldWithPath("[].childCategories[].childCategories[].childCategories").description("하위의 하위의 하위 카테고리 목록. 4뎁스 이하는 없음."),
                                fieldWithPath("[].childCategories[].childCategories[].childCategories[]").description("하위의 하위의 하위 카테고리 목록. 4뎁스 이하는 없음.")
                        )
                ));
    }



    @Test
    @WithMockCustomUser
    @DisplayName("2뎁스_이하_조회:_1뎁스_카테고리_ID로_해당_하위_카테고리들를_조회하면_해당_2뎁스_카테고리부터_하위_카테고리들이_전부_조회되고_응답한다.")
    public void testGet2DepthCategories() throws Exception {
        // given
        List<CategoryDto> categories = (List<CategoryDto>)Arrays.asList(
                CategoryDto.builder().id(5L).level(2L).parentId(4L).name("Category 52")
                        .childCategories(
                                (List<CategoryDto>)Arrays.asList(CategoryDto.builder().id(6L).level(3L).parentId(5L).name("Category 63")
                                        .childCategories(new ArrayList<>()).build()
                                )
                        ).build()
        );
        given(categoryService.get2DepthCategories(4L)).willReturn(categories);

        // when // then
        mockMvc.perform(get("/categories/get2DepthCategories/{parentId}", 4L))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("categories-get2DepthCategories",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("parentId").description("상위 카테고리 ID")
                        ),
                        responseFields(
                                fieldWithPath("[].id").description("카테고리 ID"),
                                fieldWithPath("[].parentId").description("부모 카테고리 ID, 최상위 카테고리는 null->0."),
                                fieldWithPath("[].level").description("카테고리 레벨"),
                                fieldWithPath("[].name").description("카테고리 이름"),
                                fieldWithPath("[].childCategories").description("하위 카테고리의 목록"),
                                fieldWithPath("[].childCategories[]").description("하위 카테고리의 목록"),
                                fieldWithPath("[].childCategories[].id").description("하위 카테고리의 ID"),
                                fieldWithPath("[].childCategories[].parentId").description("하위 카테고리의 상위 카테고리 ID"),
                                fieldWithPath("[].childCategories[].level").description("하위 카테고리의 레벨"),
                                fieldWithPath("[].childCategories[].name").description("하위 카테고리의 이름"),
                                fieldWithPath("[].childCategories[].childCategories").description("하위의 하위 카테고리 목록. 4뎁스 이하는 없음."),
                                fieldWithPath("[].childCategories[].childCategories[]").description("하위의 하위 카테고리 목록. 4뎁스 이하는 없음.")
                        )
                ));
    }



    @Test
    @WithMockCustomUser
    @DisplayName("3뎁스_이하_조회:_2뎁스_카테고리_ID로_해당_하위_카테고리들를_조회하면_해당_3뎁스_카테고리들이_전부_조회되고_응답한다.")
    public void testGet3DepthCategories() throws Exception {
        // given
        List<CategoryDto> categories = (List<CategoryDto>)Arrays.asList(
                CategoryDto.builder().id(6L).level(3L).parentId(5L).name("Category 63")
                        .childCategories(new ArrayList<>()
                        ).build()
        );
        given(categoryService.get3DepthCategories(5L)).willReturn(categories);

        // when // then
        mockMvc.perform(get("/categories/get3DepthCategories/{parentId}", 5L))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("categories-get3DepthCategories",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("parentId").description("상위 카테고리 ID")
                        ),
                        responseFields(
                                fieldWithPath("[].id").description("카테고리 ID"),
                                fieldWithPath("[].parentId").description("부모 카테고리 ID, 최상위 카테고리는 null->0."),
                                fieldWithPath("[].level").description("카테고리 레벨"),
                                fieldWithPath("[].name").description("카테고리 이름"),
                                fieldWithPath("[].childCategories").description("하위 카테고리의 목록. 4뎁스 이하는 없음."),
                                fieldWithPath("[].childCategories[]").description("하위 카테고리의 목록. 4뎁스 이하는 없음.")
                        )
                ));
    }
}