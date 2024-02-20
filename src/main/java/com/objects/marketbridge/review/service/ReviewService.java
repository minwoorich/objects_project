package com.objects.marketbridge.review.service;

import com.objects.marketbridge.image.domain.Image;
import com.objects.marketbridge.member.domain.Member;
import com.objects.marketbridge.member.service.port.MemberRepository;
import com.objects.marketbridge.order.service.port.OrderDetailQueryRepository;
import com.objects.marketbridge.product.domain.Product;
import com.objects.marketbridge.product.service.port.ProductRepository;
import com.objects.marketbridge.image.infra.ImageRepository;
import com.objects.marketbridge.review.domain.*;
import com.objects.marketbridge.review.dto.*;
import com.objects.marketbridge.review.infra.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    private final ReviewSurveyRepository reviewSurveyRepository;
    private final ReviewSurveyCategoryRepository reviewSurveyCategoryRepository;
    private final SurveyContentRepository surveyContentRepository;
//    //LIKE관련//
//    private final ReviewLikesRepository reviewLikesRepository;



    //리뷰 서베이 선택창 조회
    @Transactional
    public List<ReviewSurveyQuestionAndOptionsDto> getReviewSurveyQuestionAndOptionsList(Long productId) {
        //해당 상품에 대한 reviewSurveyCategory을 가져옴(선택창+입력창)
        //해당 상품에 대한 SurveyContent를 가져옴(선택창)
        //해당 상품에 대한 입력창에 입력할 SurveyContent는 프론트에서 처리

        List<ReviewSurveyCategory> reviewSurveyCategoryList
                = reviewSurveyCategoryRepository.findAllByProductId(productId);

        ReviewSurveyQuestionAndOptionsDto reviewSurveyQuestionAndOptionsDto;

        List<ReviewSurveyQuestionAndOptionsDto> reviewSurveyQuestionAndOptionsDtoList = new ArrayList<>();
        //reviewSurveyQuestionsAndOptions을 담을 for문
        for (ReviewSurveyCategory reviewSurveyCategory : reviewSurveyCategoryList) {
            //리뷰작성자가 옵션 중에 선택하는 경우의 옵션들
            if (surveyContentRepository.existsBySurveyCategoryId(reviewSurveyCategory.getId())) {
                List<SurveyContent> surveyContentList
                        = surveyContentRepository.findAllBySurveyCategoryId(reviewSurveyCategory.getId());
                List<String> surveyContentContentListForDto = new ArrayList<>();

                for (SurveyContent surveyContent : surveyContentList) {
                    surveyContentContentListForDto.add(surveyContent.getContent());
                }
                reviewSurveyQuestionAndOptionsDto
                        = ReviewSurveyQuestionAndOptionsDto.builder()
                        .reviewSurveyQuestion(reviewSurveyCategory.getName())
                        .reviewSurveyOptionList(surveyContentContentListForDto)
                        .build();
                reviewSurveyQuestionAndOptionsDtoList.add(reviewSurveyQuestionAndOptionsDto);
                //리뷰작성자가 선택하지 않고 직접 입력하는 경우는 빈값(null)
            } else {
                reviewSurveyQuestionAndOptionsDto
                        = ReviewSurveyQuestionAndOptionsDto.builder()
                        .reviewSurveyQuestion(reviewSurveyCategory.getName())
                        .reviewSurveyOptionList(null)
                        .build();
                reviewSurveyQuestionAndOptionsDtoList.add(reviewSurveyQuestionAndOptionsDto);
            }
        }
        return reviewSurveyQuestionAndOptionsDtoList;
    }



    //리뷰 등록
    @Transactional
    public Long createReview(CreateReviewDto request, Long memberId) {

        //로그인한 유저여야 하므로, memberId는 파라미터로 받아 사용.
        Member member = memberRepository.findById(memberId);

        Product product = productRepository.findById(request.getProductId());
        Integer rating = request.getRating();
        List<ReviewSurveyDataDto> reviewSurveyDataDtoList = request.getReviewSurveyDataDtoList();
        String content = request.getContent();
        List<String> reviewImgUrls = request.getReviewImgUrls();
        String summary = request.getSummary();

        List<ReviewImage> reviewImages = new ArrayList<>();

        //이미지 저장 및 리뷰이미지 저장
        Review review = Review.builder()
                .member(member)
                .product(product)
                .rating(rating)
                .content(content)
                .reviewImages(reviewImages)
                .summary(summary)
                .build();

        for (int i = 0; i < reviewImgUrls.size(); i++) {
            String reviewImgUrl = reviewImgUrls.get(i);
            Image image = Image.builder()
//                    .type(ImageType.REVIEW_IMG.toString())
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
        }

        //바로 밑 부분은 없어도 됨. //리뷰 좋아요 누를시 review-likes가 생성되게 바꿈.
//        ReviewLikes reviewLikes = ReviewLikes.builder()
//                .review(review)
//                .member(member)
//                //.product(product)
//                .liked(false)
//                .build();
//        reviewLikesRepository.save(reviewLikes);

        //선택&입력된 리뷰 서베이들 등록
        for(int i = 0; i< reviewSurveyDataDtoList.size(); i++){
            String surveyCategory = reviewSurveyDataDtoList.get(i).getReviewSurveyCategoryData();
            ReviewSurvey reviewSurvey = ReviewSurvey.builder()
                    .review(review)
                    .reviewSurveyCategory(reviewSurveyCategoryRepository.findByName(surveyCategory))
                    .surveyCategory(surveyCategory)
                    .content(reviewSurveyDataDtoList.get(i).getWrittenOrSelectedSurveyContentData())
                    .build();
            reviewSurveyRepository.save(reviewSurvey);
        }

        reviewRepository.save(review);

            //리뷰id 반환
        return review.getId();
    }



    //리뷰아이디로 리뷰상세 단건 조회
    @Transactional
    public ReviewSingleReadDto getReview(Long reviewId, Long memberId){
        Review findReview = reviewRepository.findById(reviewId);
        List<ReviewImage> reviewImages = findReview.getReviewImages();
        List<String> reviewImgUrls = new ArrayList<>();
        for (ReviewImage reviewImage : reviewImages) {
            reviewImgUrls.add(reviewImage.getImage().getUrl());
        }
        ReviewSingleReadDto reviewSingleReadDto
                = ReviewSingleReadDto.builder()
                .reviewId(reviewId)
                .memberId(memberId)
                .productId(findReview.getProduct().getId())
                .reviewImgUrls(reviewImgUrls)
                .rating(findReview.getRating())
                .content(findReview.getContent())
                .build();
        return reviewSingleReadDto;
    }



//    //LIKE관련//    //상품별 리뷰 리스트 조회(createdAt 최신순 내림차순 정렬 또는 liked 많은순 내림차순 정렬)
//    @Transactional
//    public Page<ReviewWholeInfoDto> getProductReviews(Long productId, Pageable pageable, String sortBy) {
//        Page<Review> reviews;
//        if (sortBy.equals("likes")) {
//            reviews = reviewRepository.findAllByProductIdOrderByLikesDesc(productId, pageable);
//        } else {
//            reviews = reviewRepository.findAllByProductId(productId, pageable);
//        }
//
//        List<ReviewWholeInfoDto> reviewWholeInfoDtoList = reviews.getContent().stream().map(
//                        review -> ReviewWholeInfoDto.builder()
//                                .productName(review.getProduct().getName())
//                                .memberName(review.getMember().getName())
//                                .rating(review.getRating())
//                                .reviewSurveyList(reviewSurveyRepository.findAllByReviewId(review.getId()))
//                                .content(review.getContent())
//                                .createdAt(review.getCreatedAt())
//                                .reviewImgUrls(review.getReviewImages().stream()
//                                        .map(reviewImage -> reviewImage.getImage().getUrl()).collect(Collectors.toList()))
//                                .sellerName("MarketBridge")
////                                //LIKE관련//
////                                .likes(review.getLikes()) // 변경된 부분: Review 엔티티의 likes 필드 사용.
//                                .build())
//                .collect(Collectors.toList());
//        return new PageImpl<>(reviewWholeInfoDtoList, pageable, reviews.getTotalElements());
//    }




//    //LIKE관련//
//        //회원별 리뷰 리스트 조회(createdAt 최신순 내림차순 정렬 또는 liked 많은순 내림차순 정렬)
//    @Transactional
//    public Page<ReviewWholeInfoDto> getMemberReviews(Long memberId, Pageable pageable, String sortBy) {
//        Page<Review> reviews;
//        if (sortBy.equals("likes")) {
//            reviews = reviewRepository.findAllByMemberIdOrderByLikesDesc(memberId, pageable);
//        } else {
//            reviews = reviewRepository.findAllByMemberId(memberId, pageable);
//        }
//
//        List<ReviewWholeInfoDto> reviewWholeInfoDtoList = reviews.getContent().stream().map(
//                        review -> ReviewWholeInfoDto.builder()
//                                .productName(review.getProduct().getName())
//                                .memberName(review.getMember().getName())
//                                .rating(review.getRating())
//                                .createdAt(review.getCreatedAt())
//                                .sellerName("MarketBridge")
//                                .reviewImgUrls(review.getReviewImages().stream()
//                                        .map(reviewImage -> reviewImage.getImage().getUrl()).collect(Collectors.toList()))
//                                .content(review.getContent())
////                                //LIKE관련//
////                                .likes(reviewLikesRepository.countByReviewIdAndLikedIsTrue(review.getId()))
//                                .build())
//                .collect(Collectors.toList());
//        return new PageImpl<>(reviewWholeInfoDtoList, pageable, reviews.getTotalElements());
//    }



    //상품별 리뷰 총갯수 조회
    @Transactional
    public ReviewsCountDto getProductReviewsCount(Long productId) {
        Long count = reviewRepository.countByProductId(productId);
        return ReviewsCountDto.builder().count(count).build();
    }



    //회원별 리뷰 총갯수 조회
    @Transactional
    public ReviewsCountDto getMemberReviewsCount(Long memberId) {
        Long count = reviewRepository.countByMemberId(memberId);
        return ReviewsCountDto.builder().count(count).build();
    }



    //리뷰 수정
    @Transactional
    public ReviewIdDto updateReview (ReviewModifiableValuesDto request, Long reviewId, Long memberId){

        //리뷰 수정 부분
        Review findReview = reviewRepository.findById(reviewId);
        List<String> updatedReviewImgUrls = request.getReviewImgUrls();
        Integer updatedRating = request.getRating();
        String updatedContent = request.getContent();
        String updatedSummary = request.getSummary();

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
//                    .type(ImageType.REVIEW_IMG.toString())
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

        findReview.update(reviewImages, updatedRating, updatedContent, updatedSummary);
        reviewRepository.save(findReview);

        ReviewIdDto reviewIdDto = ReviewIdDto.builder().reviewId(reviewId).build();
        return reviewIdDto;
    }



    //리뷰 삭제
    @Transactional
    public void deleteReview(Long reviewId, Long memberId){
        Review findReview = reviewRepository.findById(reviewId);

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

        reviewSurveyRepository.deleteAllByReviewId(reviewId);

//        //LIKE관련//
//        reviewLikesRepository.deleteAllByReviewId(reviewId);

        reviewRepository.delete(findReview);
    }



//    //LIKE관련//
//    //리뷰 좋아요 등록 또는 변경(True화/False화)
//    @Transactional
//    public ReviewLikeDto addOrChangeReviewLike(Long reviewId, Long memberId) {
//        if (reviewLikesRepository.existsByReviewIdAndMemberId(reviewId, memberId)) {
//            // 이미 좋아요가 있는 경우에는 좋아요 상태 변경 로직을 수행
//            return changeReviewLike(reviewId, memberId);
//        } else {
//            // 좋아요가 없는 경우에는 새로운 좋아요 추가
//            return addReviewLike(reviewId, memberId);
//        }
//    }
//    @Transactional
//    public ReviewLikeDto addReviewLike(Long reviewId, Long memberId) {
//        // 좋아요 추가 로직 추가
//
//        Review findReview = reviewRepository.findById(reviewId);
//        ReviewLikes reviewLikes = ReviewLikes.builder()
//                .review(findReview)
//                .member(memberRepository.findById(memberId))
////                .product(findReview.getProduct())
//                .liked(true) // 처음에는 좋아요 상태를 true로 설정(등록자체가 좋아요를 누른것이므로)
//                .build();
//
//        findReview.increaseLikes();
//        reviewLikesRepository.save(reviewLikes);
//
//        return ReviewLikeDto.builder()
//                .reviewId(reviewId)
//                .memberId(memberId)
//                .liked(true)
//                .build();
//    }
//    @Transactional
//    public ReviewLikeDto changeReviewLike(Long reviewId, Long memberId) {
//        ReviewLikes findReviewLikes = reviewLikesRepository.findByReviewIdAndMemberId(reviewId, memberId);
//
//        System.out.println("findReviewLikes.getLiked() == " + findReviewLikes.getLiked());
//        Boolean changedLiked = findReviewLikes.changeLiked();
//        System.out.println("changedLiked == " + changedLiked);
//
//        Review findReview = reviewRepository.findById(reviewId);
//        if(changedLiked == true){
//            findReview.increaseLikes();
//        } else {
//            findReview.decreaseLikes();
//        }
//
//        reviewLikesRepository.save(findReviewLikes);
//        System.out.println("after Change:findLiked == " + findReviewLikes.getLiked());
//        reviewRepository.save(findReview);
//
//        ReviewLikeDto reviewLikeDto = ReviewLikeDto.builder().reviewId(reviewId).memberId(memberId).liked(changedLiked).build();
//        return reviewLikeDto;
//    }



//    //LIKE관련//
//    //리뷰 좋아요 총갯수 조회
//    @Transactional
//    public ReviewLikesCountDto countReviewLikes(Long reviewId) {
//        Review findReview = reviewRepository.findById(reviewId);
//        Long count = reviewLikesRepository.countByReviewIdAndLikedIsTrue(reviewId);
//        ReviewLikesCountDto reviewLikesCountDto
//                = ReviewLikesCountDto.builder().reviewId(reviewId).count(count).build();
//        return reviewLikesCountDto;
//    }
}
