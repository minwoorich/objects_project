package com.objects.marketbridge.product.service;

import com.objects.marketbridge.category.service.port.CategoryRepository;
import com.objects.marketbridge.common.domain.Category;
import com.objects.marketbridge.common.domain.Product;
import com.objects.marketbridge.common.domain.ProductImage;
import com.objects.marketbridge.product.controller.request.ProductCreateRequestDto;
import com.objects.marketbridge.product.infra.ProductRepository;
import com.objects.marketbridge.product.service.dto.CreateProductDto;
import com.objects.marketbridge.product.service.port.ImageRepository;
import com.objects.marketbridge.product.service.port.OptionRepository;
import com.objects.marketbridge.product.service.port.ProdOptionRepository;
import com.objects.marketbridge.product.service.port.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CreateProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ProductImageRepository productImageRepository;
    private final OptionRepository optionRepository;
    private final ProdOptionRepository prodOptionRepository;

    // 상품 생성
    @Transactional
    public Long create(ProductCreateRequestDto request){
        //1. 상품 생성
        Product product = productRepository.save(createProduct(CreateProductDto.fromRequest(request)));
        //2. image 추가
//        createProductImages(request.getItemImgUrls(),request.getDetailImgUrls(),product);
        //3. 옵션 추가

        //4. product id 반환
        return product.getId();
    }

    public Product createProduct(CreateProductDto createProductDto){
        Category category = categoryRepository.findById(createProductDto.getCategoryId());
        Boolean isOwn = createProductDto.getIsOwn();
        String name = createProductDto.getName();
        Long price = createProductDto.getPrice();
        Boolean isSubs = createProductDto.getIsSubs();
        Long stock = createProductDto.getStock();
        String thumbImg = createProductDto.getThumbImg();
        Long discountRate = createProductDto.getDiscountRate();
        String productNo = createProductDto.getProductNo();

        return Product.create(category,isOwn,name,price,isSubs,stock,thumbImg,discountRate,productNo);
    }

//    public List<ProductImage> createProductImages(){
//
//    }
}
