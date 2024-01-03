package com.objects.marketbridge.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reward extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "reward_id")
    private Long id;

    private Integer rate;

    @Builder
    private Reward(Integer rate) {
        this.rate = rate;
    }
}
