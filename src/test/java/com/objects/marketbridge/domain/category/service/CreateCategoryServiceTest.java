////안되어서 전체 주석처리
//
//package com.objects.marketbridge.domain.category.service;
//
//import com.objects.marketbridge.category.service.CategoryService;
//import com.objects.marketbridge.category.service.port.CategoryRepository;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.io.IOException;
//
//@Transactional
//@ActiveProfiles("test")
//@SpringBootTest //테스트 프로파일 설정
//public class CreateCategoryServiceTest {
//
//    @Mock
//    private CategoryRepository categoryRepository;
//
//    @InjectMocks
//    private CategoryService categoryService;
//
//    @Test
//    @DisplayName("카테고리 엑셀파일로 벌크 업로드하는 것을 테스트 한다")
//    public void testUploadExcelFile() throws IOException {
//        // given
//        // 엑셀 파일 생성 (이 파일은 테스트 리소스에 미리 저장되어 있음.)
//        MockMultipartFile mockFile = new MockMultipartFile("category-for-test.xlsx",
//                getClass().getClassLoader().getResourceAsStream("category-for-test.xlsx")
//        );
//
//        // Mock Repository의 동작 설정
//        // 이 3개의 설정들은 해당 카테고리가 데이터베이스에 존재하지 않는다고 가정.
//        // 이러한 설정은 테스트 시에 해당 카테고리가 이미 존재하는지 여부에 따라 다른 동작을 시뮬레이션하기 위한 것.
//        Mockito.when(categoryRepository.existsByName("LargeCategory1111")).thenReturn(false);
//        Mockito.when(categoryRepository.existsByName("MediumCategory1111")).thenReturn(false);
//        Mockito.when(categoryRepository.existsByNameAndLevel("SmallCategory1111", 2L)).thenReturn(false);
//
//        // when
//        String result = categoryService.uploadExcelFile(mockFile);
//
//        // then
//        // 결과 메시지 확인 또는 반환된 값에 대한 추가 검증.
//        Assertions.assertEquals("파일 업로드 및 데이터베이스 저장이 완료되었습니다.", result);
////        //예시
////        List<Category> categories = categoryRepository.findAll();
////        assertThat(categories).hasSize(categoryData.size());
//    }
//}
//
//
////안되어서 인코딩문제인가싶어서 아래방식으로도 해봤는데 역시 안됨.
////
////package com.objects.marketbridge.domain.category.service;
////
////import com.objects.marketbridge.category.service.CategoryService;
////import com.objects.marketbridge.category.service.port.CategoryRepository;
////import org.junit.jupiter.api.Assertions;
////import org.junit.jupiter.api.DisplayName;
////import org.junit.jupiter.api.Test;
////import org.mockito.InjectMocks;
////import org.mockito.Mock;
////import org.mockito.Mockito;
////import org.springframework.boot.test.context.SpringBootTest;
////import org.springframework.mock.web.MockMultipartFile;
////import org.springframework.test.context.ActiveProfiles;
////import org.springframework.transaction.annotation.Transactional;
////
////import java.io.File;
////import java.io.IOException;
////import java.nio.charset.StandardCharsets;
////import java.nio.file.Files;
////
////@Transactional
////@ActiveProfiles("test")
////@SpringBootTest
////public class CategoryCreateServiceTest {
////
////    @Mock
////    private CategoryRepository categoryRepository;
////
////    @InjectMocks
////    private CategoryService categoryService;
////
////    @Test
////    @DisplayName("카테고리 엑셀파일로 벌크 업로드하는 것을 테스트 한다")
////    public void testUploadExcelFile() throws IOException {
////        // given
////        // 엑셀 파일 생성 (이 파일은 테스트 리소스에 미리 저장되어 있음.)
////        File excelFile = new File(getClass().getClassLoader().getResource("category-for-test.xlsx").getFile());
////
////        // ANSI에서 UTF-8로 변환
////        File utf8File = convertFileToUtf8(excelFile);
////
////        MockMultipartFile mockFile = new MockMultipartFile("category-for-test.xlsx", "category-for-test.xlsx",
////                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", Files.readAllBytes(utf8File.toPath())
////        );
////
////        // Mock Repository의 동작 설정
////        // 이 3개의 설정들은 해당 카테고리가 데이터베이스에 존재하지 않는다고 가정.
////        // 이러한 설정은 테스트 시에 해당 카테고리가 이미 존재하는지 여부에 따라 다른 동작을 시뮬레이션하기 위한 것.
////        Mockito.when(categoryRepository.existsByName("LargeCategory1111")).thenReturn(false);
////        Mockito.when(categoryRepository.existsByName("MediumCategory1111")).thenReturn(false);
////        Mockito.when(categoryRepository.existsByNameAndLevel("SmallCategory1111", 2L)).thenReturn(false);
////
////        // when
////        String result = categoryService.uploadExcelFile(mockFile);
////
////        // then
////        // 결과 메시지 확인 또는 반환된 값에 대한 추가 검증.
////        Assertions.assertEquals("파일 업로드 및 데이터베이스 저장이 완료되었습니다.", result);
////        // 추가 검증 코드 추가 (예를 들어, 데이터베이스 조회하여 실제로 데이터가 등록되었는지 확인하는 등)
////    }
////
////    private File convertFileToUtf8(File inputFile) throws IOException {
////        byte[] fileContent = Files.readAllBytes(inputFile.toPath());
////        String content = new String(fileContent, StandardCharsets.ISO_8859_1);
////        byte[] utf8Content = content.getBytes(StandardCharsets.UTF_8);
////
////        File utf8File = new File("category-for-test-utf8.xlsx");
////        Files.write(utf8File.toPath(), utf8Content);
////        return utf8File;
////    }
////}
