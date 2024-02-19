package com.objects.marketbridge.product.service;

import com.objects.marketbridge.product.domain.Product;
import com.objects.marketbridge.product.domain.ProductImage;
import com.objects.marketbridge.product.service.port.ProductRepository;
import com.objects.marketbridge.image.infra.ImageRepository;
import com.objects.marketbridge.product.service.port.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeleteProductService {

    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;
    private final ProductImageRepository productImageRepository;

//    //1. 상품 조회
//    Product findProduct = productRepository.findById(request.getProductId()).get();
//    //2. 상품 수정
//    UpdateProductDto updateProductDto = UpdateProductDto.fromRequest(request);
//    updateProduct(findProduct, updateProductDto);
//        productRepository.save(findProduct);

    // 상품삭제
    @Transactional
    public void delete(Long requestId){
        //1. 삭제할 상품 조회
        Product findProduct = productRepository.findById(requestId);
        //2. 연관된 image들과 productImage들 삭제
        deleteProductImages(requestId);
        //3. 상품 삭제
        productRepository.delete(findProduct);
    }

    private void deleteProductImages(Long requestId) {

        // 관련 images 및 productImages 삭제 로직
        List<ProductImage> findProductImages = productImageRepository.findAllByProductId(requestId);

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
    }
}




