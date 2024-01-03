package com.objects.marketbridge.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "image_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ImageType type;

    private String url;

    @Builder
    private Image(ImageType type, String url) {
        this.type = type;
        this.url = url;
    }
}
