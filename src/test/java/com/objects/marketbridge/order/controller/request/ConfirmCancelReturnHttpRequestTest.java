package com.objects.marketbridge.order.controller.request;

import com.objects.marketbridge.order.controller.dto.ConfirmCancelReturnHttp;
import com.objects.marketbridge.order.service.dto.ConfirmCancelReturnDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ConfirmCancelReturnHttpRequestTest {

    @Test
    @DisplayName("serviceDto로 변환할 수 있다.")
    public void toServiceRequest() {
        // given
        ConfirmCancelReturnHttp.Request request = ConfirmCancelReturnHttp.Request.builder()
                .orderNo("1")
                .cancelReason("옥지보단 빵빵이")
                .build();

        // when
        ConfirmCancelReturnDto.Request result = request.toServiceRequest();

        // then
        Assertions.assertThat(result).extracting("orderNo", "cancelReason")
                .contains("1", "옥지보단 빵빵이");
    }
}