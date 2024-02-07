package com.objects.marketbridge.common.exception.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_PAYMENT_AMOUNT, //유효하지 않은 액수.
    MISMATCHED_PAYMENT_DATA, //결제정보 검증에 실패하였습니다
    SHIPPING_ADDRESS_NOT_REGISTERED, //등록된 배송지 정보가 없습니다.
    PAYMENT_NOT_FOUND,//결제 정보를 찾을 수 없습니다.
    OUT_OF_STOCK, //재고가 없습니다.
    SESSION_EXPIRED, //세션값이 만료되었습니다
    BALANCE_INSUFFICIENT,//잔액이 부족합니다
    INVALID_INPUT_VALUE,// 유효하지 않은 입력값
    METHOD_NOT_ALLOWED, // 지원하지않는 HTTP 메서드 호출
    ORDERDETAIL_NOT_FOUND, // 주문 상세 정보를 찾을 수 없습니다.
    ORDER_NOT_FOUND, // 주문 정보를 찾을 수 없습니다.
    QUANTITY_EXCEEDED, // 수량이 초과 되었습니다.
    NON_CANCELLABLE_PRODUCT, // 취소가 불가능한 상품입니다.
    NON_RETURNABLE_PRODUCT, // 반품이 불가능한 상품입니다.
    NON_CANCELLABLE_RETURNABLE_PRODUCT // 취소/반품이 불가능한 상품입니다.
}
