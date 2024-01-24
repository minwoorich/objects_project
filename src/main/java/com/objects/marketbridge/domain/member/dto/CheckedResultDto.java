package com.objects.marketbridge.domain.member.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CheckedResultDto {
    private Boolean checked;

    @Builder
    public CheckedResultDto(Boolean checked) {
        this.checked = checked;
    }

    public Boolean isChecked() {
        return checked;
    }
}
