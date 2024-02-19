package com.objects.marketbridge.cart.service;

import com.objects.marketbridge.cart.service.port.CartCommendRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteCartService {

    private final CartCommendRepository cartCommendRepository;

    public void delete(List<Long> cartIds) {
        cartCommendRepository.deleteAllByIdInBatch(cartIds);
    }
}
