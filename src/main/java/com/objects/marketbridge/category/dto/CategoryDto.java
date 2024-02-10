//package com.objects.marketbridge.category.dto;
//
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Getter
//@NoArgsConstructor
//public class CategoryDto {
//    private Long categoryId;
//    private Long parentId;
//    private Long level;
//    private String name;
//    private List<CategoryDto> childCategories = new ArrayList<>();
//
//    @Builder
//    public CategoryDto(Long categoryId, Long parentId, Long level, String name) {
//        this.categoryId = categoryId;
//        this.parentId = parentId;
//        this.level = level;
//        this.name = name;
//    }
//
//    public void addChildCategories(CategoryDto categoryDto){
//        childCategories.add(categoryDto);
//    }
//
//    public void addChildCategories(List<CategoryDto> categoryDto){
//        childCategories.addAll(categoryDto);
//    }
//}






package com.objects.marketbridge.category.dto;

import com.objects.marketbridge.category.domain.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class CategoryDto {

    private Long id;
    private Long parentId;
    private Long level;
    private String name;
    private List<CategoryDto> childCategories;

    @Builder
    public CategoryDto(Long id, Long parentId, Long level, String name, List<CategoryDto> childCategories) {
        this.id = id;
        this.parentId = parentId != null ? parentId : 0L;
        this.level = level;
        this.name = name;
        this.childCategories = childCategories;
    }
}