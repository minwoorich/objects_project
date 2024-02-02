package com.objects.marketbridge.payment.service;

import com.objects.marketbridge.common.dto.KakaoPayApproveResponse;
import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.order.service.port.OrderQueryRepository;
import com.objects.marketbridge.payment.controller.dto.CompleteOrderHttp;
import com.objects.marketbridge.payment.domain.Amount;
import com.objects.marketbridge.payment.domain.CardInfo;
import com.objects.marketbridge.payment.domain.Payment;
import com.objects.marketbridge.payment.service.port.PaymentRepository;
import com.objects.marketbridge.seller.domain.Seller;
import com.objects.marketbridge.seller.domain.SellerAccount;
import com.objects.marketbridge.seller.service.port.SellerAccountRepository;
import com.objects.marketbridge.seller.service.port.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.objects.marketbridge.order.domain.StatusCodeType.PAYMENT_COMPLETED;

@Service
@RequiredArgsConstructor
public class CreatePaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderQueryRepository orderQueryRepository;
    private final SellerRepository sellerRepository;
    private final SellerAccountRepository sellerAccountRepository;

    @Transactional
    public CompleteOrderHttp.Response create(KakaoPayApproveResponse response) {

        // 1. Payment 엔티티 생성
        Payment payment = createPayment(response);
        paymentRepository.save(payment);

        // 2. Order - Payment 연관관계 매핑
        Order order = orderQueryRepository.findByOrderNoWithOrderDetailsAndProduct(response.getPartnerOrderId());
        order.linkPayment(payment);

        // 3. orderDetail 의 statusCode 업데이트
        payment.changeStatusCode(PAYMENT_COMPLETED.getCode());

        //TODO
        // 4. 판매자 계좌 내역 생성
        List<SellerAccount> sellerAccounts = createSellerAccounts(order);
        sellerAccountRepository.saveAll(sellerAccounts);

//        Map<Long, List<OrderDetail>> orderDetailMap = order.orderDetailsGroupedBySellerId();
//        orderDetailMap.entrySet().stream()
//                .map(entry -> {
//                    Seller seller = sellerRepository.findById(entry.getKey());
//                    seller.linkSellerAccounts();
//                })

        // 5. delivery 생성

        // TODO : payment 대신 order?
        return CompleteOrderHttp.Response.of(payment);
    }

    private Payment createPayment(KakaoPayApproveResponse response) {

        String orderNo = response.getPartnerOrderId();
        String paymentMethod = response.getPaymentMethodType();
        String tid = response.getTid();
        CardInfo cardInfo = response.getCardInfo();
        Amount amount = response.getAmount();
        LocalDateTime approvedAt = response.getApprovedAt();

        return Payment.create(orderNo, paymentMethod, tid, cardInfo, amount, approvedAt);
    }
    private List<SellerAccount> createSellerAccounts(Order order) {

        return order.totalAmountGroupedBySellerId().entrySet().stream()
                .map(entry ->{
                    sellerRepository.findById(entry.getKey()).updateBalance(entry.getValue());
                    return createSellerAccount(entry.getValue(), entry.getValue(), "입금");
                }
        ).collect(Collectors.toList());
    }
    private SellerAccount createSellerAccount(Long amount, Long balance, String detail) {

        Long incoming = amount >=0 ? amount : 0L;
        Long outgoing = amount <0 ? -amount : 0L;

        return SellerAccount.create(incoming, outgoing, balance, detail);
    }

    private Long getBalance(Long sellerId) {
        return sellerRepository.findById(sellerId).getBalance();
    }
}
