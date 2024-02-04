package com.objects.marketbridge.product.service;

import com.objects.marketbridge.product.domain.Product;
import com.objects.marketbridge.product.controller.response.ReadProductResponseDto;
import com.objects.marketbridge.product.infra.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReadProductService {

    private final ProductRepository productRepository;

    @Transactional
    public ReadProductResponseDto read(Long productId) {
        Product findProduct = productRepository.findById(productId);

        Long categoryId = findProduct.getCategory().getId();
        Boolean isOwn = findProduct.getIsOwn();
        String name = findProduct.getName();
        Long price = findProduct.getPrice();
        Boolean isSubs = findProduct.getIsSubs();
        Long stock = findProduct.getStock();
        String thumbImg = findProduct.getThumbImg();
        Long discountRate = findProduct.getDiscountRate();

        ReadProductResponseDto response = ReadProductResponseDto.builder()
                .productId(productId)
                .categoryId(categoryId)
                .isOwn(isOwn)
                .name(name)
                .price(price)
                .isSubs(isSubs)
                .stock(stock)
                .thumbImg(thumbImg)
                .discountRate(discountRate)
                .build();
        return response;
    }
}
