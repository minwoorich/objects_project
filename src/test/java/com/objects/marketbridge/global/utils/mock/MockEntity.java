package com.objects.marketbridge.global.utils.mock;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MockEntity {
    private Long id;
    private String email;
    private String name;

    @Builder
    public MockEntity(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }
}
