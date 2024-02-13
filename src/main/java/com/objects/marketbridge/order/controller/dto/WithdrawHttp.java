package com.objects.marketbridge.order.controller.dto;

import com.objects.marketbridge.order.service.dto.WithdrawDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class WithdrawHttp {

    @Getter
    @NoArgsConstructor
    public static class Response {

        public static Response of(WithdrawDto.Response dto) {
            return null;
        }
    }
}
