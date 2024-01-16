package com.objects.marketbridge.domain.product.repository.stock;

import com.objects.marketbridge.domain.model.*;
import com.objects.marketbridge.domain.order.entity.ProdOrder;
import com.objects.marketbridge.domain.order.entity.ProdOrderDetail;
import com.objects.marketbridge.domain.order.service.port.OrderDetailRepository;
import com.objects.marketbridge.domain.order.service.port.OrderRepository;
import com.objects.marketbridge.domain.product.repository.ProductRepository;
import com.objects.marketbridge.domain.product.repository.warehouse.WarehouseRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class StockRepositoryTest {

    @Autowired private StockRepository stockRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderDetailRepository orderDetailRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired private EntityManager em;

//    @BeforeEach
//    public void before() {
//        Product product1 = Product.builder()
//                .price(10000L)
//                .name("옷")
//                .build();
//        Product product2= Product.builder()
//                .price(20000L)
//                .name("신발")
//                .build();
//        em.persist(product1);
//        em.persist(product2);
//
//        OptionCategory optionCategory = OptionCategory.builder()
//                .name("사이즈")
//                .build();
//        em.persist(optionCategory);
//
//        Option option1 = Option.builder()
//                .optionCategory(optionCategory)
//                .name("M")
//                .build();
//        Option option2 = Option.builder()
//                .optionCategory(optionCategory)
//                .name("L")
//                .build();
//        Option option3 = Option.builder()
//                .optionCategory(optionCategory)
//                .name("250")
//                .build();
//        Option option4 = Option.builder()
//                .optionCategory(optionCategory)
//                .name("260")
//                .build();
//        em.persist(option1);
//        em.persist(option2);
//        em.persist(option3);
//        em.persist(option4);
//
//        ProdOption prodOption1 = ProdOption.builder()
//                .option(option1)
//                .product(product1)
//                .build();
//        ProdOption prodOption2 = ProdOption.builder()
//                .option(option2)
//                .product(product1)
//                .build();
//        ProdOption prodOption3 = ProdOption.builder()
//                .option(option3)
//                .product(product2)
//                .build();
//        ProdOption prodOption4 = ProdOption.builder()
//                .option(option4)
//                .product(product2)
//                .build();
//        em.persist(prodOption1);
//        em.persist(prodOption2);
//        em.persist(prodOption3);
//        em.persist(prodOption4);
//
//        AddressValue addressValue1 = AddressValue.builder()
//                .name("인천 창고 주소")
//                .build();
//        AddressValue addressValue2 = AddressValue.builder()
//                .name("대구 창고 주소")
//                .build();
//
//        Seller seller1 = Seller.builder()
//                .name("판매자1")
//                .build();
//        Seller seller2 = Seller.builder()
//                .name("판매자2")
//                .build();
//        em.persist(seller1);
//        em.persist(seller2);
//
//        Warehouse warehouse1 = Warehouse.builder()
//                .addressValue(addressValue1)
//                .seller(seller1)
//                .build();
//        Warehouse warehouse2 = Warehouse.builder()
//                .addressValue(addressValue2)
//                .seller(seller2)
//                .build();
//        em.persist(warehouse1);
//        em.persist(warehouse2);
//
//        Stock stock1 = Stock.builder()
//                .product(product1)
//                .warehouse(warehouse1)
//                .build();
//        Stock stock2 = Stock.builder()
//                .product(product2)
//                .warehouse(warehouse1)
//                .build();
//        Stock stock3 = Stock.builder()
//                .product(product1)
//                .warehouse(warehouse1)
//                .build();
//        Stock stock4 = Stock.builder()
//                .product(product2)
//                .warehouse(warehouse1)
//                .build();
//        em.persist(stock1);
//        em.persist(stock2);
//        em.persist(stock3);
//        em.persist(stock4);
//
//        Member member = Member.builder()
//                .name("화나게하지마")
//                .build();
//        em.persist(member);
//
//        Coupon coupon1 = Coupon.builder()
//                .price(1000L)
//                .product(product1)
//                .name("옷 할인 쿠폰")
//                .build();
//        Coupon coupon2 = Coupon.builder()
//                .price(2000L)
//                .product(product2)
//                .name("신발 할인 쿠폰")
//                .build();
//        em.persist(coupon1);
//        em.persist(coupon2);
//
//        MemberCoupon memberCoupon1 = MemberCoupon.builder()
//                .member(member)
//                .coupon(coupon1)
//                .isUsed(true)
//                .build();
//        MemberCoupon memberCoupon2 = MemberCoupon.builder()
//                .member(member)
//                .coupon(coupon2)
//                .isUsed(true)
//                .build();
//        em.persist(memberCoupon1);
//        em.persist(memberCoupon2);
//
//        Point point = Point.builder()
//                .outPoint(0L)
//                .balance(10000L)
//                .member(member)
//                .build();
//        em.persist(point);
//
//        // given
//        ProdOrder prodOrder = ProdOrder.builder()
//                .member(member)
//                .build();
//
//        // 상품 옵션 추가
//        ProdOrderDetail prodOrderDetail1 = ProdOrderDetail.builder()
//                .prodOrder(prodOrder)
//                .prodOption(prodOption1)
//                .quantity(10L)
//                .build();
//        ProdOrderDetail prodOrderDetail2 = ProdOrderDetail.builder()
//                .prodOrder(prodOrder)
//                .prodOption(prodOption4)
//                .quantity(20L)
//                .build();
//        em.persist(prodOrderDetail1);
//        em.persist(prodOrderDetail2);
//
//        Delivery delivery1 = Delivery.builder()
//                .prodOrderDetail(prodOrderDetail1)
//                .statusCode(StatusCodeType.DELIVERY_PENDING.getCode())
//                .build();
//        Delivery delivery2 = Delivery.builder()
//                .prodOrderDetail(prodOrderDetail2)
//                .statusCode(StatusCodeType.DELIVERY_PENDING.getCode())
//                .build();
//        em.persist(delivery1);
//        em.persist(delivery2);
//    }

    @Test
    @DisplayName("재고를 저장할 수 있다.")
    public void save() {
        // given
        Stock stock = Stock.builder().quantity(10L).build();

        // when
        Stock savedStock = stockRepository.save(stock);

        // then
        assertThat(stock.getQuantity()).isEqualTo(savedStock.getQuantity());
    }

    // TODO
    @Test
    @DisplayName("db락을 설정하고 아이디로 재고를 조회 할 수 있다.")
    public void findByIdWithLock() {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("아이디를 가지고 재고를 조회할 수 있다.")
    public void findById() {
        // given
        Stock stock = Stock.builder().build();
        Stock savedStock = stockRepository.save(stock);

        // when
        Optional<Stock> findStockOpt = stockRepository.findById(stock.getId());

        // then
        assertThat(findStockOpt.get()).isEqualTo(savedStock);
    }

    @Test
    @DisplayName("저장된 재고가 없으면 조회할 수 없다.")
    public void findById_Error() {
        // given
        Long noId = 1L;

        // when
        Optional<Stock> findStockOpt = stockRepository.findById(noId);

        // then
        assertThatThrownBy(findStockOpt::get)
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("주문 번호가 주어지면 재고 리스트를 조회 할 수 있다.")
    public void findStocksByProdOrderId() {
        // given
        ProdOrder prodOrder = ProdOrder.builder().build();

        Product product1 = Product.builder().build();
        Product product2= Product.builder().build();

        ProdOrderDetail prodOrderDetail1 = ProdOrderDetail.builder()
                .product(product1)
                .prodOrder(prodOrder)
                .quantity(10L)
                .build();
        ProdOrderDetail prodOrderDetail2 = ProdOrderDetail.builder()
                .product(product2)
                .prodOrder(prodOrder)
                .quantity(20L)
                .build();

        AddressValue addressValue1 = AddressValue.builder()
                .name("인천 창고 주소")
                .build();
        AddressValue addressValue2 = AddressValue.builder()
                .name("대구 창고 주소")
                .build();

        Warehouse warehouse1 = Warehouse.builder()
                .addressValue(addressValue1)
                .build();
        Warehouse warehouse2 = Warehouse.builder()
                .addressValue(addressValue2)
                .build();

        Stock stock1 = Stock.builder()
                .product(product1)
                .warehouse(warehouse1)
                .build();
        Stock stock2 = Stock.builder()
                .product(product2)
                .warehouse(warehouse1)
                .build();
        Stock stock3 = Stock.builder()
                .product(product1)
                .warehouse(warehouse1)
                .build();
        Stock stock4 = Stock.builder()
                .product(product2)
                .warehouse(warehouse1)
                .build();

        productRepository.saveAll(List.of(product1, product2));
        orderDetailRepository.saveAll(List.of(prodOrderDetail1, prodOrderDetail2));
        orderRepository.save(prodOrder);
        stockRepository.saveAll(List.of(stock1, stock2, stock3, stock4));
        Warehouse savedWarehouse = warehouseRepository.save(warehouse1);
        warehouseRepository.save(warehouse2);

        // when
//        List<Stock> stocks = stockRepository.findStocksByProdOrderIdAndWarehouseId(prodOrder.getId(), savedWarehouse.getId());

        // then
//        assertThat(stocks.size()).isEqualTo(2);
    }
}