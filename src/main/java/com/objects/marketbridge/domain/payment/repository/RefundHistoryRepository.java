package com.objects.marketbridge.domain.payment.repository;

import com.objects.marketbridge.domain.payment.domain.RefundHistory;
import org.springframework.stereotype.Repository;

@Repository
public interface RefundHistoryRepository {
    // TODO Spring Data JPA 로 구현해야함!
    void save(RefundHistory refundHistory);
}
