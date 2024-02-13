package com.objects.marketbridge.order.controller.dto;

import com.objects.marketbridge.order.service.dto.ConfirmRecantationDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ConfirmRecantationHttp {

    @Getter
    @NoArgsConstructor
    public static class Response {

        public static Response of(ConfirmRecantationDto.Response dto) {
            return null;
        }
    }
}
