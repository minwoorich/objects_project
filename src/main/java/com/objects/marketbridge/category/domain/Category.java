package com.objects.marketbridge.category.domain;

import com.objects.marketbridge.member.domain.BaseEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id",referencedColumnName = "category_id",insertable=false, updatable=false)
    private Category parent;

    @Column(name = "parent_id")
    private Long parentId;

    private Long level; //대분류 1L, 중분류 2L, 소분류 3L.
    private String name;

    @OneToMany(mappedBy = "parent",orphanRemoval = true)
    private List<Category> childCategories = new ArrayList<>();

    @Builder
    private Category(Long parentId, Long level, String name, Category parent) {
        this.parentId = parentId;
        this.level = level;
        this.name = name;
        this.parent = parent;
    }

    public void addChildCategories(Category category){
        childCategories.add(category);
        category.setParentId(this);
    }

    public void setParentId(Category category){
        this.parent = category;
    }
}

