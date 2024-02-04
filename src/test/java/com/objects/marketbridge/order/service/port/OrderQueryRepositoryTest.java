package com.objects.marketbridge.order.service.port;

import com.objects.marketbridge.common.domain.Member;
import com.objects.marketbridge.common.domain.Product;
import com.objects.marketbridge.member.service.port.MemberRepository;
import com.objects.marketbridge.order.controller.dto.GetOrderHttp;
import com.objects.marketbridge.order.domain.Order;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.product.infra.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.objects.marketbridge.order.controller.dto.GetOrderHttp.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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