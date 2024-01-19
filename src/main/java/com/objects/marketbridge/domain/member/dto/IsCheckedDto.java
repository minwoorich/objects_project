package com.objects.marketbridge.domain.member.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IsCheckedDto {
    private boolean isChecked;

    @Builder
    public IsCheckedDto(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public boolean isChecked() {
        return isChecked;
    }
}
