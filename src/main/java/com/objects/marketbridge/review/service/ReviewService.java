package com.objects.marketbridge.review.service;

import com.objects.marketbridge.image.domain.Image;
import com.objects.marketbridge.image.infra.ImageRepository;
import com.objects.marketbridge.review.domain.*;
import com.objects.marketbridge.review.dto.*;
import com.objects.marketbridge.review.service.port.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;


    private final ImageRepository imageRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final ReviewSurveyRepository reviewSurveyRepository;
    private final ReviewSurveyCategoryRepository reviewSurveyCategoryRepository;
    private final SurveyContentRepository surveyContentRepository;
    private final ReviewLikeRepository reviewLikeRepository;



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
    public void createReview(CreateReviewDto request, Long memberId) {

        //로그인한 유저여야 하므로, memberId는 파라미터로 받아 사용.
        Integer rating = request.getRating();
        String content = request.getContent();
        String summary = request.getSummary();

        //이미지 저장 및 리뷰이미지 저장
        Review review = Review.builder()
                .memberId(memberId)
                .productId(request.getProductId())
                .rating(rating)
                .content(content)
                .summary(summary)
                .build();

        reviewRepository.save(review);

        createReviewImages(request.getReviewImgUrls(), review);

        //선택&입력된 리뷰 서베이들 등록
        createReviewSurveys(request, review);
    }

    private void createReviewSurveys(CreateReviewDto request, Review review) {
        request.getReviewSurveys().forEach(obj -> {
            ReviewSurvey reviewSurvey = ReviewSurvey.builder()
                    .reviewId(review.getId())
                    .reviewSurveyCategoryId(obj.getReviewSurveyCategoryId())
                    .surveyCategory(obj.getReviewSurveyCategoryName())
                    .content(obj.getContent())
                    .build();
            reviewSurveyRepository.save(reviewSurvey);
        });
    }

    @Transactional
    public void updateReview (UpdateReviewDto request){

        Long reviewId = request.getReviewId();

        Review review = reviewRepository.findById(reviewId);

        List<ReviewSurvey> reviewSurveys = reviewSurveyRepository.findAllByReviewId(reviewId);

        deleteReviewImages(reviewId);

        review.update(request.getRating(), request.getContent(), request.getSummary());
        updateReviewSurveys(request, reviewSurveys);
        createReviewImages(request.getReviewImgUrls(), review);
    }


    private void updateReviewSurveys(UpdateReviewDto request, List<ReviewSurvey> reviewSurveys) {
        reviewSurveys.forEach(reviewSurvey -> {
            UpdateReviewSurveyDto updateReviewSurveyDto =
                    request.getUpdateReviewSurveys().stream()
                            .filter(obj -> Objects.equals(obj.getReviewSurveyId(), reviewSurvey.getId()))
                            .findFirst()
                            .orElse(new UpdateReviewSurveyDto(reviewSurvey.getId(),reviewSurvey.getContent()));
            reviewSurvey.update(updateReviewSurveyDto.getContent());
        });
    }
    private void deleteReviewImages(Long reviewId) {
        List<ReviewImage> reviewImages = reviewImageRepository.findAllByReviewId(reviewId);
        List<Long> savedImgIds = reviewImages.stream().map(ReviewImage::getImageId).toList();
        imageRepository.deleteByIdIn(savedImgIds);
        reviewImageRepository.deleteByReviewId(reviewId);
    }

    private void createReviewImages(List<ReviewImageDto> request, Review review) {
        request.forEach(obj -> {
            Image image = Image.builder()
                    .url(obj.getImgUrl())
                    .build();
            imageRepository.save(image);
            ReviewImage reviewImage = ReviewImage.builder()
                    .reviewId(review.getId())
                    .imageId(image.getId())
                    .seqNo(obj.getSeqNo())
                    .description(obj.getDescription())
                    .build();
            reviewImageRepository.save(reviewImage);
        });
    }

    @Transactional
    public void deleteReview(Long reviewId){
        deleteReviewImages(reviewId);
        reviewSurveyRepository.deleteByReviewId(reviewId);
        reviewLikeRepository.deleteByReviewId(reviewId);
        reviewRepository.deleteById(reviewId);
    }


    //리뷰아이디로 리뷰상세 단건 조회
//    @Transactional
//    public ReviewSingleReadDto getReview(Long reviewId, Long memberId){
//        Review findReview = reviewRepository.findById(reviewId);
//        List<ReviewImage> reviewImages = findReview.getReviewImages();
//        List<String> reviewImgUrls = new ArrayList<>();
//        for (ReviewImage reviewImage : reviewImages) {
//            reviewImgUrls.add(reviewImage.getImage().getUrl());
//        }
//        ReviewSingleReadDto reviewSingleReadDto
//                = ReviewSingleReadDto.builder()
//                .reviewId(reviewId)
//                .memberId(memberId)
//                .productId(findReview.getProduct().getId())
//                .reviewImgUrls(reviewImgUrls)
//                .rating(findReview.getRating())
//                .content(findReview.getContent())
//                .build();
//        return reviewSingleReadDto;
//    }



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
