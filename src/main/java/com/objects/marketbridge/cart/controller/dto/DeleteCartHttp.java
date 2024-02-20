package com.objects.marketbridge.cart.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class DeleteCartHttp {

    @Getter
    @NoArgsConstructor
    public static class Request {
        private List<Long> selectedCartIds;

        @Builder
        private Request(List<Long> selectedCartIds) {
            this.selectedCartIds = selectedCartIds;
        }
    }
}
