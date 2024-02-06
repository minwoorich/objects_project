package com.objects.marketbridge.product.service;

import com.objects.marketbridge.category.domain.Category;
import com.objects.marketbridge.category.service.port.CategoryRepository;
import com.objects.marketbridge.member.domain.Image;
import com.objects.marketbridge.member.domain.ImageType;
import com.objects.marketbridge.product.controller.request.CreateProductRequestDto;
import com.objects.marketbridge.product.domain.*;
import com.objects.marketbridge.product.infra.ProductRepository;
import com.objects.marketbridge.product.service.dto.CreateProductDto;
import com.objects.marketbridge.product.service.dto.OptionDto;
import com.objects.marketbridge.product.service.dto.ProdTagDto;
import com.objects.marketbridge.product.service.dto.ProductImageDto;
import com.objects.marketbridge.product.service.port.*;
import jakarta.annotation.Nullable;
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
    private final OptionCategoryRepository optionCategoryRepository;
    private final TagCategoryRepository tagCategoryRepository;
    private final TagRepository tagRepository;
    private final ProdTagRepository prodTagRepository;

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
        List<ProdOption> prodOptions = createProdOptions(createProductDto.getOptionInfoList(),product);
        prodOptionRepository.saveAll(prodOptions);
        //4. tag 추가
        List<ProdTag> prodTags = createTags(createProductDto.getProdTagList(), product);
        prodTagRepository.saveAll(prodTags);

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

    public List<ProdOption> createProdOptions(List<OptionDto> optionDtos, Product product){
        List<ProdOption> prodOptions = new ArrayList<>();
        // option category 조회
        for (int i = 0; i < optionDtos.size(); i++) {
            OptionCategory optionCategory = optionCategoryRepository.findByName(optionDtos.get(i).getOptionCategory());
            Option option = optionRepository.findByName(optionDtos.get(i).getName());
            Long optionId = option.getId();
            // 옵션 카테고리 없는 경우 만들어주기
            if (optionCategory.getName().equals("EMPTY")){
                OptionCategory newCategory = createOptionCategory(optionDtos.get(i));
                optionId = createOption(optionDtos.get(i), newCategory);
            } else if (option.getName().equals("EMPTY")) {
                optionId = createOption(optionDtos.get(i),optionCategory);
            }

            ProdOption prodOption = ProdOption.builder()
                    .option(optionRepository.findById(optionId))
                    .build();

            prodOptions.add(prodOption);
            // 연관관계 추가
            product.addProdOptions(prodOption);

        }
        return prodOptions;
    }

    public List<ProdTag> createTags(List<ProdTagDto> prodTagDtos, Product product){
        List<ProdTag> prodTags = new ArrayList<>();

        for (int i = 0; i < prodTagDtos.size(); i++) {
            TagCategory tagCategory = tagCategoryRepository.findByName(prodTagDtos.get(i).getTagKey());
            Tag tag = tagRepository.findByName(prodTagDtos.get(i).getTagValue());
            Long tagId = tag.getId();
            // 태그 카테고리 없는 경우 만들어주기
            if (tagCategory.getName().equals("EMPTY")){
                TagCategory newCategory = createTagCategory(prodTagDtos.get(i));
                tagId = createTag(prodTagDtos.get(i), newCategory);
            } else if (tag.getName().equals("EMPTY")) {
                tagId = createTag(prodTagDtos.get(i),tagCategory);
            }

            ProdTag prodTag = ProdTag.builder()
                    .tag(tagRepository.findById(tagId))
                    .build();
            prodTags.add(prodTag);
            // 연관관계 추가
            product.addProdTags(prodTag);
        }
        return prodTags;
    }

    private OptionCategory createOptionCategory(OptionDto optionDto){
        OptionCategory newCategory = OptionCategory.builder()
                .name(optionDto.getOptionCategory())
                .build();
        optionCategoryRepository.save(newCategory);
        return newCategory;
    }

    private Long createOption(OptionDto optionDto,OptionCategory optionCategory){
        Option newOption = Option.builder()
                        .optionCategory(optionCategory)
                        .name(optionDto.getName())
                        .build();
        optionRepository.save(newOption);
        // 연관관계 추가
        optionCategory.addOptions(newOption);

        return newOption.getId();
    }

    private TagCategory createTagCategory(ProdTagDto prodTagDto){
        TagCategory newCategory = TagCategory.builder()
                .name(prodTagDto.getTagKey())
                .build();
        tagCategoryRepository.save(newCategory);
        return newCategory;
    }

    private Long createTag(ProdTagDto prodTagDto,TagCategory tagCategory){
        Tag newTag = Tag.builder()
                .tagCategory(tagCategory)
                .name(prodTagDto.getTagValue())
                .build();
        tagRepository.save(newTag);
        // 연관관계 추가
        tagCategory.addTags(newTag);

        return newTag.getId();
    }


}
