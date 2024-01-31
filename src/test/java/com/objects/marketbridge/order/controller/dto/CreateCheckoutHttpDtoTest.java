package com.objects.marketbridge.order.controller.dto;

import com.objects.marketbridge.common.domain.AddressValue;
import com.objects.marketbridge.order.domain.Address;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class CreateCheckoutHttpDtoTest {

    @DisplayName("of 테스트, address 엔티티를 전달 받으면 CreateCheckout.Response를 반환한다")
    @Test
    void of_one_param() {

        // given
        AddressValue addressValue = createAddressValue();
        Address address = Address.builder().addressValue(addressValue).build();

        // when
        CreateCheckoutHttpDto.Response response = CreateCheckoutHttpDto.Response.of(address);

        //then
        assertThat(response).extracting(
                "phoneNo",
                "name",
                "city",
                "street",
                "zipcode",
                "detail",
                "alias")
                .containsExactlyInAnyOrder(
                        "01012341414",
                        "홍길동",
                        "서울",
                        "세종대로",
                        "12313",
                        "xxx아파트 110동 431호",
                        "우리집");

    }

    private  AddressValue createAddressValue() {
        return AddressValue.builder()
                .phoneNo("01012341414")
                .name("홍길동")
                .city("서울")
                .street("세종대로")
                .zipcode("12313")
                .detail("xxx아파트 110동 431호")
                .alias("우리집")
                .build();
    }

}