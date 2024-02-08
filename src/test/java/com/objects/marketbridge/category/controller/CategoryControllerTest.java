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
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

import static org.hamcrest.Matchers.*;
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
    @DisplayName("카테고리_전체를_조회하면_최상위카테고리부터_그하위소속의_카테고리들이_전부_조회되고_응답한다.")
    public void testGetTotalCategories() throws Exception {
        // given
        List<CategoryDto> categories = Arrays.asList(
                CategoryDto.builder().id(1L).parentId(null).name("Category 11").build(),
                CategoryDto.builder().id(2L).parentId(null).name("Category 12").build(),
                CategoryDto.builder().id(3L).parentId(2L).name("Category 21").build(),
                CategoryDto.builder().id(4L).parentId(2L).name("Category 22").build(),
                CategoryDto.builder().id(5L).parentId(3L).name("Category 31").build(),
                CategoryDto.builder().id(6L).parentId(3L).name("Category 32").build()
        );
        given(categoryService.getTotalCategories()).willReturn(categories);

        // when // then
        mockMvc.perform(get("/categories/getTotalCategories"))
                .andExpect(status().isOk())
                .andDo(document("categories-getTotalCategories",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].id").description("카테고리 ID"),
                                fieldWithPath("[].parentId").description("부모 카테고리 ID. 최상위 카테고리의 경우에는 null이 될 수 있음"),
                                fieldWithPath("[].level").description("카테고리 레벨"),
                                fieldWithPath("[].name").description("카테고리 이름"),
                                fieldWithPath("[].childCategories").description("하위 카테고리 목록")
                        )
                ));
    }
}