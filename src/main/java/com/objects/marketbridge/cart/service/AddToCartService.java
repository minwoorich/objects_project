package com.objects.marketbridge.cart.service;

import com.objects.marketbridge.cart.domain.Cart;
import com.objects.marketbridge.cart.service.dto.CreateCartDto;
import com.objects.marketbridge.cart.service.port.CartCommendRepository;
import com.objects.marketbridge.cart.service.port.CartQueryRepository;
import com.objects.marketbridge.common.exception.exceptions.CustomLogicException;
import com.objects.marketbridge.member.domain.Member;
import com.objects.marketbridge.member.service.port.MemberRepository;
import com.objects.marketbridge.product.domain.Product;
import com.objects.marketbridge.product.infra.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.objects.marketbridge.common.exception.exceptions.ErrorCode.DUPLICATE_OPERATION;

@Service
@Slf4j
@RequiredArgsConstructor
public class AddToCartService {

    private final CartCommendRepository cartCommendRepository;
    private final CartQueryRepository cartQueryRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public void add(CreateCartDto createCartDto) {
        // 1. 이미 장바구니에 담긴 상품인지 아닌지 검증
        validDuplicate(createCartDto.getProductNo());
        cartCommendRepository.save(create(createCartDto));
    }

    private Cart create(CreateCartDto createCartDto) {
        Product product = productRepository.findByProductNo(createCartDto.getProductNo());
        Long quantity = createCartDto.getQuantity();
        product.verifyStockAvailable(quantity); // 2. 재고 검사
        Member member = memberRepository.findById(createCartDto.getMemberId());
        Boolean isSubs = createCartDto.getIsSubs();

        return Cart.create(member, product, isSubs, quantity);
    }
    private void validDuplicate(String productNo) {
        if (cartQueryRepository.findByProductNo(productNo).isPresent()) {
            throw CustomLogicException.createBadRequestError(DUPLICATE_OPERATION, "이미 장바구니에 담긴 상품입니다", LocalDateTime.now());
        }
    }
}
