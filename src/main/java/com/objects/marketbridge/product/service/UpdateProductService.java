package com.objects.marketbridge.product.service;

import com.objects.marketbridge.category.domain.Category;
import com.objects.marketbridge.category.service.port.CategoryRepository;
import com.objects.marketbridge.image.domain.Image;
import com.objects.marketbridge.image.domain.ImageType;
import com.objects.marketbridge.product.controller.request.UpdateProductRequestDto;
import com.objects.marketbridge.product.controller.response.UpdateProductResponseDto;
import com.objects.marketbridge.product.domain.*;
import com.objects.marketbridge.product.infra.product.ProductRepository;
import com.objects.marketbridge.product.dto.UpdateProductDto;
import com.objects.marketbridge.image.infra.ImageRepository;
import com.objects.marketbridge.product.service.port.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UpdateProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ProductImageRepository productImageRepository;

    //상품 수정
    @Transactional
    public UpdateProductResponseDto update(UpdateProductRequestDto request){
        //1. 상품 조회
        Product findProduct = productRepository.findById(request.getProductId());
        //2. 상품 수정
        UpdateProductDto updateProductDto = UpdateProductDto.fromRequest(request);
        updateProduct(findProduct, updateProductDto);
        productRepository.save(findProduct);
        //3. image 및 productImage 수정(삭제후 재등록)
        deleteProductImages(findProduct);
        List<ProductImage> productImages = createProductImages(request.getDetailImgUrls(), ImageType.DETAIL_IMG.toString(),findProduct);
        productImages.addAll( createProductImages(request.getItemImgUrls(), ImageType.ITEM_IMG.toString(), findProduct) );
        productImageRepository.saveAll(productImages);

        //4. product ----- product_option ---- options ---- option_category 관련은 아직 수정하지 않음.

        //5. 응답Dto 반환
        UpdateProductResponseDto updateProductResponseDto
                = UpdateProductResponseDto.from(findProduct);
        return updateProductResponseDto;
    }

    protected Product updateProduct(Product product, UpdateProductDto updateProductDto){
        Category category = categoryRepository.findById(updateProductDto.getCategoryId());
        Boolean isOwn = updateProductDto.getIsOwn();
        String name = updateProductDto.getName();
        Long price = updateProductDto.getPrice();
        Boolean isSubs = updateProductDto.getIsSubs();
        Long stock = updateProductDto.getStock();
        String thumbImg = updateProductDto.getThumbImg();
        Long discountRate = updateProductDto.getDiscountRate();
        String productNo = updateProductDto.getProductNo();

        return product.update(category, isOwn, name, price, isSubs, stock, thumbImg, discountRate, productNo);
    }


    private void deleteProductImages(Product product){

        // 관련 images 및 productImages 삭제 로직
        List<ProductImage> findProductImages = productImageRepository.findAllByProductId(product.getId());

        for (ProductImage findProductImage : findProductImages) {
            productImageRepository.delete(findProductImage);
        }

        List<Long> findImageIds
                = findProductImages.stream()
                .map(productImage -> productImage.getImage().getId())
                .collect(Collectors.toList());

        for (Long findImageId : findImageIds) {
            imageRepository.deleteById(findImageId);
        }

//        // 상품등록시 image테이블에 아이템이미지url들 추가, product_image테이블에 추가.
//        List<String> itemImgUrls = updateProductRequestDto.getItemImgUrls();
//        for (String itemImgUrl : itemImgUrls) {
//            Image itemImg = Image.builder()
//                    .type(ImageType.ITEM_IMG.toString())
//                    .url(itemImgUrl).build();
//            imageRepository.save(itemImg);
//
//            ProductImage productImage = ProductImage.builder()
//                    .image(imageRepository.findById(itemImg.getId()).get())
//                    .product(productRepository.findById(product.getId()).get())
//                    .build();
//
//            productImageRepository.save(productImage);
//        }
//        // image테이블에 디테일이미지url들 추가, product_image테이블에 추가.
//        List<String> detailImgUrls = updateProductRequestDto.getDetailImgUrls();
//        for (String detailImgUrl : detailImgUrls) {
//            Image detailImg = Image.builder()
//                    .type(ImageType.DETAIL_IMG.toString())
//                    .url(detailImgUrl).build();
//            imageRepository.save(detailImg);
//
//            ProductImage productImage = ProductImage.builder()
//                    .image(imageRepository.findById(detailImg.getId()).get())
//                    .product(productRepository.findById(product.getId()).get())
//                    .build();
//
//            productImageRepository.save(productImage);
//        }
    }

    public List<ProductImage> createProductImages(List<String> imgUrls, String type, Product product){
        List<ProductImage> productImages = new ArrayList<>();
        for (int i = 0; i<imgUrls.size(); i++) {
            // 이미지 저장
            Image image = Image.builder()
//                    .type(type)
                    .url(imgUrls.get(i))
                    .build();
            imageRepository.save(image);

            //ProductImage 엔티티 생성 (정렬 순서대로 seqNo 할당)
            ProductImage productImage = ProductImage.create( product, imageRepository.findById(image.getId()), Long.valueOf(i) );
            productImages.add(productImage);

            // 연관관계 추가
            product.addProductImages(productImage);
        }
        return productImages;
    }
}
