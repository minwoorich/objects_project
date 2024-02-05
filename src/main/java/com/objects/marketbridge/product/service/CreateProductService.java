package com.objects.marketbridge.product.service;

import com.objects.marketbridge.category.domain.Category;
import com.objects.marketbridge.category.service.port.CategoryRepository;
import com.objects.marketbridge.common.domain.Image;
import com.objects.marketbridge.common.domain.ImageType;
import com.objects.marketbridge.product.controller.request.CreateProductRequestDto;
import com.objects.marketbridge.product.domain.*;
import com.objects.marketbridge.product.infra.ProductRepository;
import com.objects.marketbridge.product.service.dto.CreateProductDto;
import com.objects.marketbridge.product.service.dto.ProductImageDto;
import com.objects.marketbridge.product.service.port.ImageRepository;
import com.objects.marketbridge.product.service.port.OptionRepository;
import com.objects.marketbridge.product.service.port.ProdOptionRepository;
import com.objects.marketbridge.product.service.port.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public Long create(CreateProductRequestDto request){
        CreateProductDto createProductDto =CreateProductDto.fromRequest(request);
        //1. 상품 생성
        Product product = productRepository.save(createProduct(createProductDto));

        //2. productImage 저장
        // 2-1. imageurl -> ProductImage 화
        List<ProductImage> productImages = createProductImages(createProductDto.getProductImageList(),product);
        productImageRepository.saveAll(productImages);

        //3. 옵션 추가
//        createProdOptions();

        //4. tag 추가

        //5. product id 반환
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

    public List<ProductImage> createProductImages(List<ProductImageDto> productImageDtoList, Product product){
        List<ProductImage> productImages = new ArrayList<>();

        for (ProductImageDto productImageDto: productImageDtoList) {
            // 이미지 저장
            Image image = Image.builder()
                    .type(productImageDto.getType())
                    .url(productImageDto.getImgUrl())
                    .build();
            imageRepository.save(image);

            //ProductImage 엔티티 생성
            ProductImage productImage = ProductImage.create( product, imageRepository.findById(image.getId()), productImageDto.getSeqNo());
            productImages.add(productImage);

            // 연관관계 추가
            product.addProductImages(productImage);
        }
        return productImages;
    }

    public List<ProdOption> createProdOptions(){
        List<ProdOption> prodOptions = new ArrayList<>();
        // option category 조회


        return prodOptions;
    }

}
