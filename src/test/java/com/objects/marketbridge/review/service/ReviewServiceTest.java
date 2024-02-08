package com.objects.marketbridge.review.service;

import com.objects.marketbridge.member.domain.Member;
import com.objects.marketbridge.member.service.port.MemberRepository;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.order.domain.StatusCodeType;
import com.objects.marketbridge.order.service.port.OrderDetailCommendRepository;
import com.objects.marketbridge.product.domain.Product;
import com.objects.marketbridge.product.infra.ProductRepository;
import com.objects.marketbridge.review.domain.Review;
import com.objects.marketbridge.review.dto.CreateReviewDto;
import com.objects.marketbridge.review.dto.ReviewAllValuesDto;
import com.objects.marketbridge.review.dto.ReviewIdDto;
import com.objects.marketbridge.review.dto.ReviewModifiableValuesDto;
import com.objects.marketbridge.review.infra.ReviewRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ReviewServiceTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    OrderDetailCommendRepository orderDetailCommendRepository;
    @Autowired
    ReviewService reviewService;
    @Autowired
    ReviewRepository reviewRepository;

    @Test
    @DisplayName("리뷰를_등록하면_등록된다.")
    void createReviewServiceTest() {

        //given
        Member testMember = Member.builder().build();
        memberRepository.save(testMember);
        Long testMemberId = testMember.getId();

        Product testProduct = Product.builder().build();
        productRepository.save(testProduct);
        Long testProductId = testProduct.getId();

        OrderDetail testOrderDetail = OrderDetail.builder()
                .statusCode(StatusCodeType.DELIVERY_COMPLETED.toString())
                .build();
        orderDetailCommendRepository.save(testOrderDetail);
        Long testOrderDetailId = testOrderDetail.getId();

        List<String> testReviewImgUrls = new ArrayList<>();
        testReviewImgUrls.add("0001.jpg");
        testReviewImgUrls.add("0002.jpg");
        testReviewImgUrls.add("0003.jpg");

        CreateReviewDto testRequest = CreateReviewDto.builder()
                .productId(testProductId)
                .orderDetailId(testOrderDetailId)
                .rating(5)
                .content("리뷰내용")
                .reviewImgUrls(testReviewImgUrls)
                .build();

        //when
        Long testReviewId = reviewService.createReview(testRequest, testMemberId);
        Review testReview = reviewRepository.findById(testReviewId);

        //then
        Assertions.assertThat(testRequest.getProductId()).isEqualTo(testReview.getProduct().getId());
        Assertions.assertThat(testReview.getReviewImages().get(0).getImage().getUrl()).isEqualTo(testReviewImgUrls.get(0));
    }




    @Test
    @DisplayName("리뷰를_조회하면_조회된다.")
    void getReviewServiceTest() {

        //given
        Member testMember = Member.builder().build();
        memberRepository.save(testMember);
        Long testMemberId = testMember.getId();

        Product testProduct = Product.builder().build();
        productRepository.save(testProduct);
        Long testProductId = testProduct.getId();

        OrderDetail testOrderDetail = OrderDetail.builder()
                .statusCode(StatusCodeType.DELIVERY_COMPLETED.toString())
                .build();
        orderDetailCommendRepository.save(testOrderDetail);
        Long testOrderDetailId = testOrderDetail.getId();

        List<String> testReviewImgUrls = new ArrayList<>();
        testReviewImgUrls.add("0001.jpg");
        testReviewImgUrls.add("0002.jpg");
        testReviewImgUrls.add("0003.jpg");

        CreateReviewDto testRequest = CreateReviewDto.builder()
                .productId(testProductId)
                .orderDetailId(testOrderDetailId)
                .rating(5)
                .content("리뷰내용")
                .reviewImgUrls(testReviewImgUrls)
                .build();

        Long testReviewId = reviewService.createReview(testRequest, testMemberId);
        Review testReview = reviewRepository.findById(testReviewId);

        //when
        ReviewAllValuesDto response = reviewService.getReview(testReviewId, testMemberId);

        //then
        Assertions.assertThat(testReview.getReviewImages().get(0).getImage().getUrl())
                .isEqualTo(response.getReviewImgUrls().get(0));
    }



    @Test
    @DisplayName("리뷰를_수정하면_수정된다.")
    void updateReviewServiceTest() {

        //given
        Member testMember = Member.builder().build();
        memberRepository.save(testMember);
        Long testMemberId = testMember.getId();

        Product testProduct = Product.builder().build();
        productRepository.save(testProduct);
        Long testProductId = testProduct.getId();

        OrderDetail testOrderDetail = OrderDetail.builder()
                .statusCode(StatusCodeType.DELIVERY_COMPLETED.toString())
                .build();
        orderDetailCommendRepository.save(testOrderDetail);
        Long testOrderDetailId = testOrderDetail.getId();

        List<String> testReviewImgUrls = new ArrayList<>();
        testReviewImgUrls.add("0001.jpg");
        testReviewImgUrls.add("0002.jpg");
        testReviewImgUrls.add("0003.jpg");

        CreateReviewDto testRequest = CreateReviewDto.builder()
                .productId(testProductId)
                .orderDetailId(testOrderDetailId)
                .rating(5)
                .content("리뷰내용")
                .reviewImgUrls(testReviewImgUrls)
                .build();

        Long testReviewId = reviewService.createReview(testRequest, testMemberId);
        Review testReview = reviewRepository.findById(testReviewId);

        List<String> updatedReviewImgUrls = new ArrayList<>();
        updatedReviewImgUrls.add("0004.jpg");
        updatedReviewImgUrls.add("0005.jpg");
        updatedReviewImgUrls.add("0006.jpg");
        updatedReviewImgUrls.add("0007.jpg");
        updatedReviewImgUrls.add("0008.jpg");

        ReviewModifiableValuesDto testUpdateRequest = ReviewModifiableValuesDto.builder()
                .rating(4)
                .content("리뷰수정된내용")
                .reviewImgUrls(updatedReviewImgUrls)
                .build();

        //when
        ReviewIdDto resultDto = reviewService.updateReview(testUpdateRequest, testReviewId, testMemberId);

        //then
        Review result = reviewRepository.findById(resultDto.getReviewId());
        Assertions.assertThat(result.getRating()).isEqualTo(4);
        Assertions.assertThat(result.getContent()).isEqualTo("리뷰수정된내용");
        Assertions.assertThat(result.getReviewImages().get(0).getImage().getUrl()).isEqualTo(updatedReviewImgUrls.get(0));
    }



    @Test
    @DisplayName("리뷰를_삭제하면_삭제된다.")
    void deleteReviewServiceTest(){

        //given
        Member testMember = Member.builder().build();
        memberRepository.save(testMember);
        Long testMemberId = testMember.getId();

        Product testProduct = Product.builder().build();
        productRepository.save(testProduct);
        Long testProductId = testProduct.getId();

        OrderDetail testOrderDetail = OrderDetail.builder()
                .statusCode(StatusCodeType.DELIVERY_COMPLETED.toString())
                .build();
        orderDetailCommendRepository.save(testOrderDetail);
        Long testOrderDetailId = testOrderDetail.getId();

        List<String> testReviewImgUrls = new ArrayList<>();
        testReviewImgUrls.add("0001.jpg");
        testReviewImgUrls.add("0002.jpg");
        testReviewImgUrls.add("0003.jpg");

        CreateReviewDto testRequest = CreateReviewDto.builder()
                .productId(testProductId)
                .orderDetailId(testOrderDetailId)
                .rating(5)
                .content("리뷰내용")
                .reviewImgUrls(testReviewImgUrls)
                .build();

        Long testReviewId = reviewService.createReview(testRequest, testMemberId);
        Review testReview = reviewRepository.findById(testReviewId);

        //when
        reviewService.deleteReview(testReviewId, testMemberId);

        //then
        Assertions.assertThatThrownBy(() -> reviewRepository.findById(testReviewId))
                .isInstanceOf(JpaObjectRetrievalFailureException.class);
    }
}
