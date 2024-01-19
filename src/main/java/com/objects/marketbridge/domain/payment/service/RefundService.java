package com.objects.marketbridge.domain.payment.service;

import com.objects.marketbridge.domain.payment.client.RefundClient;
import com.objects.marketbridge.domain.payment.domain.RefundHistory;
import com.objects.marketbridge.domain.payment.dto.RefundDto;
import com.objects.marketbridge.domain.payment.dto.RefundInfoDto;
import com.objects.marketbridge.domain.payment.repository.RefundHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefundService {

    private final RefundClient refundClient;
    private final RefundHistoryRepository refundHistoryRepository;

    public RefundDto refund(String accountNo, Long refundPrice) {
        Optional<RefundInfoDto> refundInfo = refundClient.refund(accountNo, refundPrice);

        if (refundInfo.isPresent()) {
            refundHistoryRepository.save(RefundHistory.builder()
                    .accountNo(accountNo)
                    .build());
        }
        return refundInfo.orElseThrow().toRefundDto();
    }
}
