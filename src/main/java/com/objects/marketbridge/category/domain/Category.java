package com.objects.marketbridge.category.domain;

import com.objects.marketbridge.member.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    private Long parentId;

    private Long level; //대분류 0L, 중분류 1L, 소분류 2L.

    private String name;

    @OneToMany(mappedBy = "parentId")
    private List<Category> childCategories;

    @Builder
    public Category(Long parentId, Long level, String name) {
        this.parentId = parentId;
        this.level = level;
        this.name = name;
    }

    @Builder
    public Category(Long parentId, Long level, String name, List<Category> childCategories) {
        this.parentId = parentId;
        this.level = level;
        this.name = name;
        this.childCategories = childCategories;
    }
}

