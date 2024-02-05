package com.objects.marketbridge.category.service;

import com.objects.marketbridge.category.controller.response.ReadCategoryResponseDto;
import com.objects.marketbridge.category.domain.Category;
import com.objects.marketbridge.category.service.port.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReadCategoryService {

    private final CategoryRepository categoryRepository;

    //전체(라지,미디엄,스몰). 라지가 해당 미디엄 전부를, 미디엄이 해당 스몰 전부를 포함하는 형태로 JSON형식.
    public List<ReadCategoryResponseDto> getTotalCategories() {
        List<Category> categories = categoryRepository.findAllByLevelAndParentIdIsNull(0L);
        return convertToDtoList(categories);
    }

    //특정부모카테고리(라지)의 미디엄(스몰 포함) 전체.
    public List<ReadCategoryResponseDto> get2DepthCategories(Long parentId) {
        List<Category> categories = categoryRepository.findAllByLevelAndParentId(1L, parentId);
        return convertToDtoList(categories);
    }

    //특정부모카테고리(미디엄)의 스몰 전체.
    public List<ReadCategoryResponseDto> get3DepthCategories(Long parentId) {
        List<Category> categories = categoryRepository.findAllByLevelAndParentId(2L, parentId);
        return convertToDtoList(categories);
    }



    //내부 이용 메서드
    private List<ReadCategoryResponseDto> convertToDtoList(List<Category> categories) {
        List<ReadCategoryResponseDto> readCategoryResponseDtos = new ArrayList<>();
        for (Category category : categories) {
            ReadCategoryResponseDto readCategoryResponseDto = new ReadCategoryResponseDto();
            readCategoryResponseDto.setId(category.getId());
            readCategoryResponseDto.setParentId(category.getParentId());
            readCategoryResponseDto.setLevel(category.getLevel());
            readCategoryResponseDto.setName(category.getName());

            // childCategories 필드는 재귀적으로 변환
            readCategoryResponseDto.setChildCategories(convertToDtoList(category.getChildCategories()));

            readCategoryResponseDtos.add(readCategoryResponseDto);
        }
        return readCategoryResponseDtos;
    }
}
