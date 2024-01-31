package com.objects.marketbridge.order.controller.dto;

import com.objects.marketbridge.order.domain.Address;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateCheckoutHttp {

    @Getter
    @NoArgsConstructor
    public static class Response {

        private String phoneNo;
        private String name;
        private String city;
        private String street;
        private String zipcode;
        private String detail;
        private String alias;

        @Builder
        public Response(String phoneNo, String name, String city, String street, String zipcode, String detail, String alias) {
            this.phoneNo = phoneNo;
            this.name = name;
            this.city = city;
            this.street = street;
            this.zipcode = zipcode;
            this.detail = detail;
            this.alias = alias;
        }

        public static Response of(Address address) {

            return Response.builder()
                    .phoneNo(address.getAddressValue().getPhoneNo())
                    .name(address.getAddressValue().getName())
                    .city(address.getAddressValue().getCity())
                    .street(address.getAddressValue().getStreet())
                    .zipcode(address.getAddressValue().getZipcode())
                    .detail(address.getAddressValue().getDetail())
                    .alias(address.getAddressValue().getAlias())
                    .build();
        }
    }
}
