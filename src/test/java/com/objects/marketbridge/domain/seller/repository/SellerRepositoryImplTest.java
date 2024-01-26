package com.objects.marketbridge.domain.seller.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class SellerRepositoryImplTest {

    @Test
    @DisplayName("seller_id 를 통해 SellerAccount 를 조회할 수 있다.")
    void findSellerAccountBySellerId() {
    }
}