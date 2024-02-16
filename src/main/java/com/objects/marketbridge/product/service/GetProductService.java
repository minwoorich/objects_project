package com.objects.marketbridge.product.service;

import com.objects.marketbridge.category.dto.CategoryDto;
import com.objects.marketbridge.category.service.CategoryService;
import com.objects.marketbridge.product.dto.ProductSearchConditionDto;
import com.objects.marketbridge.product.infra.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    @Transactional
    public void getProductByCategory(Pageable pageable, String categoryId){
        // 1. 요청 카테고리의 하위 카테고리까지 싹다 가져오기
        List<CategoryDto> categoryDtos = categoryService.getLowerCategories(Long.valueOf(categoryId));
        // 2. 3뎁스 카테고리 기준 상품 데이터 싹다 가져오기
        ProductSearchConditionDto productSearchConditionDto = ProductSearchConditionDto.builder().build();
        filterTo3Depth(categoryDtos,productSearchConditionDto);

        // 3. 페이지네이션
    }

    public void filterTo3Depth(List<CategoryDto> categoryDtoList,ProductSearchConditionDto resultDto){
        for (CategoryDto categoryDto: categoryDtoList) {
            if (categoryDto.getLevel() == 3L){
                resultDto.addLeafDepthInfoList(categoryDto.getLevel());
            }else {
                filterTo3Depth(categoryDto.getChildCategories(),resultDto);
            }
        }
    }

//    @Transactional
//    public ReadProductResponseDto read(Long productId) {
//        Product findProduct = productRepository.findById(productId);
//
//        Long categoryId = findProduct.getCategory().getId();
//        Boolean isOwn = findProduct.getIsOwn();
//        String name = findProduct.getName();
//        Long price = findProduct.getPrice();
//        Boolean isSubs = findProduct.getIsSubs();
//        Long stock = findProduct.getStock();
//        String thumbImg = findProduct.getThumbImg();
//        Long discountRate = findProduct.getDiscountRate();
//
//        ReadProductResponseDto response = ReadProductResponseDto.builder()
//                .productId(productId)
//                .categoryId(categoryId)
//                .isOwn(isOwn)
//                .name(name)
//                .price(price)
//                .isSubs(isSubs)
//                .stock(stock)
//                .thumbImg(thumbImg)
//                .discountRate(discountRate)
//                .build();
//        return response;
//    }
}
