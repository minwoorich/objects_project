package com.objects.marketbridge.common.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Table(name = "options")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Option extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_category_id")
    private OptionCategory optionCategory;

    @OneToMany(mappedBy = "option")
    private List<ProdOption> prodOptions;

    private String name;

    @Builder
    private Option(OptionCategory optionCategory, String name) {
        this.optionCategory = optionCategory;
        this.name = name;
    }

    public void setOptionCategory(OptionCategory optionCategory){
        this.optionCategory = optionCategory;
    }

    public void addProdOptions(ProdOption prodOption){
        prodOptions.add(prodOption);
        prodOption.setOption(this);
    }
}
