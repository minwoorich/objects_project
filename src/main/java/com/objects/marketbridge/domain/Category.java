package com.objects.marketbridge.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;
    // TODO
    private Long prevId;
    // TODO
    private Long nextId;

    private String name;

    @Builder
    private Category(Long prevId, Long nextId, String name) {
        this.prevId = prevId;
        this.nextId = nextId;
        this.name = name;
    }
}
