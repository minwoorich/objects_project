package com.objects.marketbridge.category.service;

//@SpringBootTest
//@ActiveProfiles("test")
//@Transactional
//class CategoryServiceTest {
//    @Autowired CategoryService categoryService;
//    @Autowired CategoryRepository categoryRepository;
//
//
//    @Test
//    @DisplayName("하위_카테고리를_가져올_수_있다")
//    void getLowerCategoriesTest(){
//        List<CategoryDto> categoryDtos = categoryService.getLowerCategories(320L);
//
////        System.out.println("categoryDtos.get(0).getName() = " + categoryDtos.get(0).getName());
//        for (CategoryDto category : categoryDtos) {
//            System.out.println("category.getName() = " + category.getName());
//            for (CategoryDto innerDto: category.getChidCategories()) {
//                System.out.println("innerDto.getName() = " + innerDto.getName());
//            }
//        }
//        Assertions.assertThat(categoryDtos.get(0).getCategoryId()).isEqualTo(1L);
//    }
//
//}