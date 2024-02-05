package com.objects.marketbridge.order.service.port;

import com.objects.marketbridge.member.service.port.MemberRepository;
import com.objects.marketbridge.product.infra.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
@Slf4j
class OrderQueryRepositoryTest {

    @Autowired private OrderCommendRepository orderCommendRepository;
    @Autowired private OrderQueryRepository orderQueryRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private MemberRepository memberRepository;


}