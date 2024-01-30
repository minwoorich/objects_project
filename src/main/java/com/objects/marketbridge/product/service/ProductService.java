package com.objects.marketbridge.product.service;

import com.objects.marketbridge.common.domain.*;
import com.objects.marketbridge.product.controller.request.ProductCreateRequestDto;
import com.objects.marketbridge.product.controller.request.ProductUpdateRequestDto;
import com.objects.marketbridge.product.controller.response.ProductReadResponseDto;
import com.objects.marketbridge.product.controller.response.ProductUpdateResponseDto;
import com.objects.marketbridge.product.service.port.ImageRepository;
import com.objects.marketbridge.product.service.port.ProductImageRepository;
import com.objects.marketbridge.product.infra.ProductRepository;
import com.objects.marketbridge.category.service.port.CategoryRepository;
import com.objects.marketbridge.product.service.port.OptionRepository;
import com.objects.marketbridge.product.service.port.ProdOptionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ProductImageRepository productImageRepository;
    private final OptionRepository optionRepository;
    private final ProdOptionRepository prodOptionRepository;

    //상품들 Excel파일로 벌크등록
    @Transactional
    public String uploadExcelFile(MultipartFile file) {

        // 엑셀 파일 읽기 : Workbook workbook = new XSSFWorkbook(file.getInputStream())
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {

            // 첫 번째 시트 선택
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {

                Row row = rowIterator.next();

                // 첫 번째 행은 건너뛰기
                if (row.getRowNum() == 0) {
                    continue;
                }

                //이하의 로직은 카테고리가 대량등록되어 있다고 가정함.

                // 필요한 열의 데이터 추출
                String largeCategoryName = row.getCell(0).getStringCellValue(); // 대분류 카테고리 이름
                String mediumCategoryName = row.getCell(1).getStringCellValue(); // 중분류 카테고리 이름
                String smallCategoryName = row.getCell(2).getStringCellValue(); // 소분류 카테고리 이름
                String productName = row.getCell(3).getStringCellValue(); // 상품 이름
                String imgType = row.getCell(4).getStringCellValue(); // 상품 이미지 타입
                Long imgSeqNo = (long) row.getCell(5).getNumericCellValue(); // 상품 이미지 시퀀스 번호
                Long dcRate = (long) row.getCell(6).getNumericCellValue(); // 상품 할인률
                Long price = (long) row.getCell(7).getNumericCellValue(); // 상품 가격
                String tagsObj = row.getCell(8).getStringCellValue(); // tags_obj
                String imgPath = row.getCell(9).getStringCellValue(); // img_path

                //상품에 해당하는 카테고리 찾기.
//                Category largeCategory;
//                Category mediumCategory;
                Category smallCategory = null;

                List<Category> smallCategoriesToBeCompared
                        = categoryRepository.findAllByNameAndLevel(smallCategoryName, 2L);
                for (Category smallCategoryToBeCompared : smallCategoriesToBeCompared) {

                    if(smallCategoriesToBeCompared.size() == 0){ //해당명칭의 소분류카테고리가 0개인경우
                        throw new EntityNotFoundException("해당 소분류 카테고리가 없습니다.");

                    } else if(smallCategoriesToBeCompared.size() == 1){ //해당명칭의 소분류카테고리가 1개인경우
                        smallCategory = smallCategoryToBeCompared;
                        break;

                    } else { //해당명칭의 소분류카테고리가 2개이상인 경우 //중분류나 대분류에서 중복된 것임.

                        List<Category> mediumCategoriesToBeCompared
                                = categoryRepository.findAllByNameAndLevel(mediumCategoryName, 1L);

                        for (Category mediumCategoryToBeCompared : mediumCategoriesToBeCompared) {
                            if (mediumCategoryToBeCompared.getName().equals(mediumCategoryName) &&
                                    categoryRepository.findById(mediumCategoryToBeCompared.getParentId()).get().getName()
                                            .equals(largeCategoryName)) {
                                smallCategory = smallCategoryToBeCompared;
//                                mediumCategory = mediumCategoryToBeCompared;
//                                largeCategory = categoryRepository.findById(mediumCategoryToBeCompared.getParentId());
                                break;
                            }
                        }
                    }
                }
                // 소분류 카테고리가 설정되어 있다면 상품 등록
                if (smallCategory != null) {
                    Product product = Product.builder()
                            .category(smallCategory)
                            .isOwn(false)
                            .name(productName)
                            .price(price)
                            .isSubs(false)
                            .stock(100L)
                            .thumbImg(imgPath)
                            .discountRate(dcRate)
                            .build();
                    productRepository.save(product);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "파일 업로드 및 처리 중 오류가 발생했습니다.";
        }
        return "파일 업로드 및 데이터베이스 저장이 완료되었습니다.";
    } //uploadExcelFile






    //상품등록
    @Transactional
    public Long createProduct(ProductCreateRequestDto productCreateRequestDto) {

        // category가 DB에 등록되어있다고 가정.
        Category category = categoryRepository.findById(productCreateRequestDto.getCategoryId()).get();

        // ProductRequestDto에서 필요한 정보 추출하여 Product 엔터티 생성
        Product product = Product.builder()
                .category(category)
                .isOwn(productCreateRequestDto.getIsOwn())
                .name(productCreateRequestDto.getName())
                .price(productCreateRequestDto.getPrice())
                .isSubs(productCreateRequestDto.getIsSubs())
                .stock(productCreateRequestDto.getStock())
                .thumbImg(productCreateRequestDto.getThumbImg())
                .discountRate(productCreateRequestDto.getDiscountRate())
                .build();

        // ProductRepository 통해 엔터티를 저장
        productRepository.save(product);

        // 관련 image, productImage 저장
        saveImageAndProductImage(productCreateRequestDto, product);


        // product ----- product_option ---- options ---- option_category 연결되어 있음.
        // 1. option_category테이블에 데이터가 등록되어있다고 가정. (색상, 사이즈 등)
        // 2. options테이블에도 데이터가 등록되어 있다고 가정. (색상-white, 사이즈-XL 등. 따로 api만들어야할듯.)
        // 하나의 상품에대한 재고는(stock)은 options테이블의 해당옵션의 stock(재고)수임.
        // 3. 등록 => ProductRequestDto에 optionNames(String배열)를 Json형식으로 받아와서
        // product(id)와 option(id)가 등록되게 prod_option테이블에 등록.
        List<String> optionNames = productCreateRequestDto.getOptionNames();
        for (String optionName : optionNames) {
            ProdOption prodOption = ProdOption.builder()
                    .product(productRepository.findById(product.getId()).get())
                    .option(optionRepository.findByName(optionName))
                    .build();

            prodOptionRepository.save(prodOption);
        }

        Long productId = product.getId();
        return productId;

    } //상품등록 끝




    //    상품조회
    @Transactional
    public ProductReadResponseDto readProduct(Long id){
        Product findProduct = productRepository.findById(id).get();
        ProductReadResponseDto productReadResponseDto = new ProductReadResponseDto(
                findProduct.getId(),
                findProduct.getCategory().getId(),
                findProduct.getIsOwn(),
                findProduct.getName(),
                findProduct.getPrice(),
                findProduct.getIsSubs(),
                findProduct.getStock(),
                findProduct.getThumbImg(),
                findProduct.getDiscountRate()
        );
        return productReadResponseDto;
    } // 상품조회 끝




    //상품수정
    @Transactional
    public ProductUpdateResponseDto updateProduct(Long id, ProductUpdateRequestDto productUpdateRequestDto) {

        // 상품 ID에 해당하는 상품을 찾음
        Product findProduct = productRepository.findById(id).get();
        Category category = categoryRepository.findById(productUpdateRequestDto.getCategoryId()).get();

        if(findProduct == null){
            throw new EntityNotFoundException("[상품수정] 해당 id에 해당하는 상품이 없습니다.");
        }

        // 업데이트 요청의 내용을 상품에 저장
        // 끝에 productRepository.save(findProduct);를 안해도 변경감지에 의해 update되는듯함.
        findProduct.updateProduct(
                category,
                productUpdateRequestDto.getIsOwn(),
                productUpdateRequestDto.getName(),
                productUpdateRequestDto.getPrice(),
                productUpdateRequestDto.getIsSubs(),
                productUpdateRequestDto.getStock(),
                productUpdateRequestDto.getThumbImg(),
                productUpdateRequestDto.getDiscountRate()
        );

        ProductUpdateResponseDto productUpdateResponseDto =
                new ProductUpdateResponseDto(
                        findProduct.getCategory().getId(),
                        findProduct.getIsOwn(),
                        findProduct.getName(),
                        findProduct.getPrice(),
                        findProduct.getIsSubs(),
                        findProduct.getStock(),
                        findProduct.getThumbImg(),
                        findProduct.getDiscountRate()
                );



        // 업데이트 요청의 내용 중 itemImg,detailImg에 관한 내용을 image와 productImage에서 변경해야하는데
        // 전부 삭제후 재등록하는 로직으로 작성함.

        // 관련 image및 productImage삭제로직
        List<ProductImage> findProductImages = productImageRepository.findAllByProductId(id);

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

        // 관련 image 및 productImage 등록
        updateImageAndProductImage(productUpdateRequestDto, findProduct);


        // product ----- product_option ---- options ---- option_category 관련은 수정하지 않음.


        return productUpdateResponseDto;
    } //상품수정 끝




    @Transactional
    // 상품삭제
    public void deleteProduct(Long id){
        Product findProduct = productRepository.findById(id).get();
        deleteImageAndProductImage(id);
        productRepository.delete(findProduct);
    } //상품삭제 끝





    //ProductService의 내부이용할 메서드들

    //상품등록시의 image와 productimage 저장메서드
    void saveImageAndProductImage(ProductCreateRequestDto productCreateRequestDto, Product product){
        // 상품등록시 image테이블에 아이템이미지url들 추가, product_image테이블에 추가.
        List<String> itemImgUrls = productCreateRequestDto.getItemImgUrls();
        for (String itemImgUrl : itemImgUrls) {
            Image itemImg = Image.builder()
                    .type(ImageType.ITEM_IMG.toString())
                    .url(itemImgUrl).build();
            imageRepository.save(itemImg);

            ProductImage productImage = ProductImage.builder()
                    .image(imageRepository.findById(itemImg.getId()).get())
                    .product(productRepository.findById(product.getId()).get())
                    .build();

            productImageRepository.save(productImage);
        }

        // 상품등록시 image테이블에 디테일이미지url들 추가, product_image테이블에 추가.
        List<String> detailImgUrls = productCreateRequestDto.getDetailImgUrls();
        for (String detailImgUrl : detailImgUrls) {
            Image detailImg = Image.builder()
                    .type(ImageType.DETAIL_IMG.toString())
                    .url(detailImgUrl).build();
            imageRepository.save(detailImg);

            ProductImage productImage = ProductImage.builder()
                    .image(imageRepository.findById(detailImg.getId()).get())
                    .product(productRepository.findById(product.getId()).get())
                    .build();

            productImageRepository.save(productImage);
        }
    }


    //상품수정시의 image와 productimage 업데이트 메서드
    void updateImageAndProductImage(ProductUpdateRequestDto productUpdateRequestDto, Product product){
        // 관련 image및 productImage 삭제 로직
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

        // 상품등록시 image테이블에 아이템이미지url들 추가, product_image테이블에 추가.
        List<String> itemImgUrls = productUpdateRequestDto.getItemImgUrls();
        for (String itemImgUrl : itemImgUrls) {
            Image itemImg = Image.builder()
                    .type(ImageType.ITEM_IMG.toString())
                    .url(itemImgUrl).build();
            imageRepository.save(itemImg);

            ProductImage productImage = ProductImage.builder()
                    .image(imageRepository.findById(itemImg.getId()).get())
                    .product(productRepository.findById(product.getId()).get())
                    .build();

            productImageRepository.save(productImage);
        }
        // image테이블에 디테일이미지url들 추가, product_image테이블에 추가.
        List<String> detailImgUrls = productUpdateRequestDto.getDetailImgUrls();
        for (String detailImgUrl : detailImgUrls) {
            Image detailImg = Image.builder()
                    .type(ImageType.DETAIL_IMG.toString())
                    .url(detailImgUrl).build();
            imageRepository.save(detailImg);

            ProductImage productImage = ProductImage.builder()
                    .image(imageRepository.findById(detailImg.getId()).get())
                    .product(productRepository.findById(product.getId()).get())
                    .build();

            productImageRepository.save(productImage);
        }
    }


    //상품삭제시의 image와 productImage 삭제 메서드
    void deleteImageAndProductImage(Long id) {

        // 관련 image및 productImage삭제로직
        List<ProductImage> findProductImages = productImageRepository.findAllByProductId(id);

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