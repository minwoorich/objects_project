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
public class OptionCategory extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "option_category_id")
    private Long id;

    private String name;

    @Builder
    private OptionCategory(String name) {
        this.name = name;
    }
}
