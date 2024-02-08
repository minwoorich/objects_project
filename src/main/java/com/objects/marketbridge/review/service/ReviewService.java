package com.objects.marketbridge.review.service;

import com.objects.marketbridge.product.domain.Image;
import com.objects.marketbridge.product.domain.ImageType;
import com.objects.marketbridge.member.domain.Member;
import com.objects.marketbridge.member.service.port.MemberRepository;
import com.objects.marketbridge.order.domain.OrderDetail;
import com.objects.marketbridge.order.domain.StatusCodeType;
import com.objects.marketbridge.order.service.port.OrderDetailQueryRepository;
import com.objects.marketbridge.product.domain.Product;
import com.objects.marketbridge.product.infra.ProductRepository;
import com.objects.marketbridge.product.service.port.ImageRepository;
import com.objects.marketbridge.review.domain.Review;
import com.objects.marketbridge.review.domain.ReviewImage;
import com.objects.marketbridge.review.dto.CreateReviewDto;
import com.objects.marketbridge.review.dto.ReviewAllValuesDto;
import com.objects.marketbridge.review.dto.ReviewIdDto;
import com.objects.marketbridge.review.dto.ReviewModifiableValuesDto;
import com.objects.marketbridge.review.infra.ReviewImageRepository;
import com.objects.marketbridge.review.infra.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final OrderDetailQueryRepository orderDetailQueryRepository;
    private final ImageRepository imageRepository;
    private final ReviewImageRepository reviewImageRepository;

    //리뷰 등록
    @Transactional
    public Long createReview(CreateReviewDto request, Long memberId) {

        //로그인한 유저여야 하므로, memberId는 파라미터로 받아 사용.
        Member member = memberRepository.findById(memberId);

        Product product = productRepository.findById(request.getProductId());
        OrderDetail orderDetail = orderDetailQueryRepository.findById(request.getOrderDetailId());
        Integer rating = request.getRating();
        String content = request.getContent();
        List<String> reviewImgUrls = request.getReviewImgUrls();

        //  리뷰는 상품결제후 배송완료/또는수취확인 된 상태에서만 작성가능하게 한다. TODO
        if (!orderDetail.getStatusCode().equals(StatusCodeType.DELIVERY_COMPLETED.toString())) {
            // 추후 customized exception 작성 필요. TODO
            throw new RuntimeException("리뷰는 배송완료된 상태에서만 작성가능합니다.");
        } else {

            List<ReviewImage> reviewImages = new ArrayList<>();

            //이미지 저장 및 리뷰이미지 저장
            Review review = Review.builder()
                    .member(member)
                    .product(product)
                    .orderDetailId(orderDetail.getId())
                    .reviewImages(reviewImages)
                    .rating(rating)
                    .content(content)
                    .build();

            for (int i = 0; i < reviewImgUrls.size(); i++) {
                String reviewImgUrl = reviewImgUrls.get(i);
                Image image = Image.builder()
                        .type(ImageType.REVIEW_IMG.toString())
                        .url(reviewImgUrl)
                        .build();
                ReviewImage reviewImage = ReviewImage.builder()
                        .review(review)
                        .image(image)
                        .seqNo(Long.valueOf(i))
                        .build();

                imageRepository.save(image);
                reviewImageRepository.save(reviewImage);
                reviewImages.add(reviewImage);
                reviewRepository.save(review);
            }

            //리뷰id 반환
            return review.getId();
        }
    }



    //리뷰 조회
    @Transactional
    public ReviewAllValuesDto getReview(Long reviewId, Long memberId){
        Review findReview = reviewRepository.findById(reviewId);
        List<ReviewImage> reviewImages = findReview.getReviewImages();
        List<String> reviewImgUrls = new ArrayList<>();
        for (ReviewImage reviewImage : reviewImages) {
            reviewImgUrls.add(reviewImage.getImage().getUrl());
        }
        ReviewAllValuesDto reviewAllValuesDto
                = ReviewAllValuesDto.builder()
                .reviewId(reviewId)
                .memberId(memberId)
                .productId(findReview.getProduct().getId())
                .orderDetailId(findReview.getOrderDetailId())
                .reviewImgUrls(reviewImgUrls)
                .rating(findReview.getRating())
                .content(findReview.getContent())
                .build();
        return reviewAllValuesDto;
    }



    //리뷰 수정
    @Transactional
    public ReviewIdDto updateReview (ReviewModifiableValuesDto request, Long reviewId, Long memberId){

        //리뷰 부분 수정
        Review findReview = reviewRepository.findById(reviewId);
        List<String> updatedReviewImgUrls = request.getReviewImgUrls();
        Integer updatedRating = request.getRating();
        String updatedContent = request.getContent();

        //이미지 및 리뷰이미지 수정
        List<ReviewImage> findReviewImages = reviewImageRepository.findAllByReviewId(reviewId);
        for (ReviewImage findReviewImage : findReviewImages) {
            reviewImageRepository.delete(findReviewImage);
        }

        List<Long> findImageIds
                = findReviewImages.stream()
                .map(reviewImage -> reviewImage.getImage().getId())
                .collect(Collectors.toList());
        for (Long findImageId : findImageIds) {
            imageRepository.deleteById(findImageId);
        }

        List<ReviewImage> reviewImages = new ArrayList<>();

        for (int i = 0; i < updatedReviewImgUrls.size(); i++) {
            String reviewImgUrl = updatedReviewImgUrls.get(i);
            Image image = Image.builder()
                    .type(ImageType.REVIEW_IMG.toString())
                    .url(reviewImgUrl)
                    .build();
            ReviewImage reviewImage = ReviewImage.builder()
                    .review(findReview)
                    .image(image)
                    .seqNo(Long.valueOf(i))
                    .build();

            imageRepository.save(image);
            reviewImageRepository.save(reviewImage);
            reviewImages.add(reviewImage);
        }

        findReview.update(reviewImages, updatedRating, updatedContent);
        reviewRepository.save(findReview);

        ReviewIdDto reviewIdDto = ReviewIdDto.builder().reviewId(reviewId).build();
        return reviewIdDto;
    }



    //리뷰 삭제
    @Transactional
    public void deleteReview(Long reviewId, Long memberId){
        Review findReview = reviewRepository.findById(reviewId);
        reviewRepository.delete(findReview);

        List<ReviewImage> findReviewImages = reviewImageRepository.findAllByReviewId(reviewId);
        for (ReviewImage findReviewImage : findReviewImages) {
            reviewImageRepository.delete(findReviewImage);
        }

        List<Long> findImageIds
                = findReviewImages.stream()
                .map(reviewImage -> reviewImage.getImage().getId())
                .collect(Collectors.toList());
        for (Long findImageId : findImageIds) {
            imageRepository.deleteById(findImageId);
        }
    }
}
