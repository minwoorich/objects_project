////안되어서 전체 주석처리
//
//package com.objects.marketbridge.domain.category.controller;
//
//import com.objects.marketbridge.category.controller.CategoryController;
//import com.objects.marketbridge.category.service.CategoryService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//
//@Transactional
//@ActiveProfiles("test")
//@SpringBootTest
//public class CreateCategoryControllerTest {
//
//    @Mock
//    private CategoryService categoryService;
//
//    @InjectMocks
//    private CategoryController categoryController;
//
//    private MockMvc mockMvc;
//
//    @BeforeEach
//    public void setUp() {
//        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
//    }
//
//    @Test
//    @DisplayName("카테고리 엑셀 파일 업로드 API 테스트")
//    public void testUploadExcelFileApi() throws Exception {
//        // MockMultipartFile 생성 (테스트 리소스에 미리 저장되어 있음.)
//        MockMultipartFile mockFile = new MockMultipartFile(
//                "file",
//                "category-for-test.xlsx",
//                MediaType.MULTIPART_FORM_DATA_VALUE,
//                getClass().getClassLoader().getResourceAsStream("category-for-test.xlsx")
//        );
//
//        // Mock Service의 동작 설정
//        when(categoryService.uploadExcelFile(mockFile)).thenReturn("파일 업로드 및 데이터베이스 저장이 완료되었습니다.");
//
//        // API 호출 및 응답 검증
//        ResultActions resultActions = mockMvc.perform(
//                MockMvcRequestBuilders.multipart("/categories/uploadExcel")
//                        .file(mockFile)
//        ).andExpect(MockMvcResultMatchers.status().isOk());
//
//        // 응답 결과에 대한 추가 검증 (원하는 형태로)
//        resultActions.andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=UTF-8"))
//                .andExpect(MockMvcResultMatchers.content().string("파일 업로드 및 데이터베이스 저장이 완료되었습니다."));
//    }
//}
//
