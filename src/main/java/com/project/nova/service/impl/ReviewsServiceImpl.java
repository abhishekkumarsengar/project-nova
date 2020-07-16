package com.project.nova.service.impl;

import com.project.nova.dto.AggregatedReviewsResponse;
import com.project.nova.dto.ReviewRequest;
import com.project.nova.dto.ReviewResponse;
import com.project.nova.entity.*;
import com.project.nova.exceptions.*;
import com.project.nova.repository.*;
import com.project.nova.service.ReviewsService;
import com.project.nova.utils.Constants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import javax.persistence.LockTimeoutException;
import javax.persistence.PessimisticLockException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReviewsServiceImpl implements ReviewsService {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ReviewsServiceImpl.class);

    private AggregatedReviewsRepository aggregatedReviewsRepository;
    private HelpfulReviewsRepository helpfulReviewsRepository;
    private RatingRepository ratingRepository;
    private ReviewsRepository reviewsRepository;

    @Autowired
    ReviewsServiceImpl(ReviewsRepository reviewsRepository,
                               AggregatedReviewsRepository aggregatedReviewsRepository,
                               HelpfulReviewsRepository helpfulReviewsRepository,
                               RatingRepository ratingRepository,
                               ReviewsRepository reviews2Repository) {
        this.reviewsRepository = reviewsRepository;
        this.aggregatedReviewsRepository = aggregatedReviewsRepository;
        this.helpfulReviewsRepository = helpfulReviewsRepository;
        this.ratingRepository = ratingRepository;
        this.reviewsRepository = reviews2Repository;
    }

    @Override
    public ReviewResponse getAllReviews(UUID productId, Integer rating, String helpful, Integer pageNumber, Integer pageSize) {
        ReviewResponse reviewResponse = new ReviewResponse();
        Page<Review> reviewResponseQueryResult = null;

        if (rating == null) {
            try {
                reviewResponseQueryResult = reviewsRepository.getAllReviewsByProductId(productId, PageRequest.of(pageNumber, pageSize));
            } catch (PessimisticLockException | LockTimeoutException | PersistenceException lockingExceptions) {
                logger.error("", lockingExceptions);
            }
        }
        else {
            reviewResponseQueryResult = reviewsRepository.getReviewsByRatings(productId, rating, PageRequest.of(pageNumber, pageSize));

        }
        reviewResponse.setReviews(reviewResponseQueryResult.getContent());
        reviewResponse.setPageNumber(pageNumber);
        reviewResponse.setPageSize(pageSize);
        reviewResponse.setTotalSize((int) reviewResponseQueryResult.getTotalElements());
        return reviewResponse;
    }

    @Override
    public Review getOneReview(UUID productId, UUID reviewId) {

        return Optional.ofNullable(reviewsRepository.getReview(productId, reviewId))
                .orElseThrow(() -> new NotFoundException("No review found with this id"));
    }
//
//    private ReviewResponse getReviewByRating(UUID productId, Integer rating,
//                                             Integer pageNumber, Integer pageSize) {
//        return reviews2Repository.getReviewsByRatings(productId, rating, PageRequest.of(pageNumber, pageSize));
//    }

    @Transactional
    @Override
    public Review createReview(UUID productId, ReviewRequest reviewRequest, BindingResult bindingResult) throws Exception {
        requestBodyValidation(bindingResult);

        Integer doesReviewExists = reviewsRepository.checkReviewExists(productId, reviewRequest.getUserId()).get();
        if (doesReviewExists > 0) {
            throw new ReviewExistsException("User has already submitted a review for this product");
        }

        Review review = new Review(UUID.randomUUID(), productId);
        BeanUtils.copyProperties(reviewRequest, review);
        try {
            reviewsRepository.save(review);
            updateRatings(productId, reviewRequest.getRating());
        } catch (RuntimeException exception) {
            logger.error("Error while persisting data. Rolling back data.");
            throw new PersistenceException("Error while persisting data. Rolling back data");
        }
        return review;
    }


    @Override
    public Review updateReview(UUID productId, UUID reviewId, ReviewRequest reviewRequest, BindingResult bindingResult) {
        Review review = Optional.ofNullable(reviewsRepository.getReview(productId, reviewId))
                .orElseThrow(() -> new NotFoundException("Review not found"));
        BeanUtils.copyProperties(reviewRequest, review);
        return reviewsRepository.save(review);
    }

    @Override
    public void deleteReview(UUID productId, UUID reviewId) {
        Review review = Optional.ofNullable(reviewsRepository.getReview(productId, reviewId))
                .orElseThrow(() -> new NotFoundException("Review not found"));
        review.setDeletedAt(new Timestamp(new Date().getTime()));
        reviewsRepository.save(review);
    }

    @Transactional
    @Override
    public void isReviewHelpFull(UUID productId, UUID reviewId, UUID userId) {
        Integer userMarkedHelpFull = helpfulReviewsRepository.hasUserMarkedHelpFull(productId, reviewId, userId);

        if (userMarkedHelpFull > 0) {
            throw new ReviewExistsException("User has already marked this review as helpful");
        }

        HelpfulReview helpfulReview = new HelpfulReview(userId, productId, reviewId);

        helpfulReviewsRepository.save(helpfulReview);
        reviewsRepository.updateHelpfulInReviews(productId, reviewId);
    }

    @Override
    public AggregatedReviewsResponse getAggregatedReviewsByRating(UUID productId) {
        Optional<Rating> aggregatedReview = Optional.ofNullable(aggregatedReviewsRepository
                .getAggregatedReviewsByProductId(productId));
        double rating = 0;
        Integer numberOfReviews = 0;
        if (aggregatedReview.isPresent()) {
            rating = Arrays.stream(new int[]{aggregatedReview.get().getRating_1() + aggregatedReview.get().getRating_2()
                    + aggregatedReview.get().getRating_3() + aggregatedReview.get().getRating_4()
                    + aggregatedReview.get().getRating_5()}).average().getAsDouble();
            numberOfReviews = aggregatedReview.get().getNumberOfReviews();
        }
        return new AggregatedReviewsResponse(rating, numberOfReviews);
    }

    @Override
    public BreakdownRating getBreakDownReviewsByRating(UUID productId) {
        BreakdownRating breakdownRating = new BreakdownRating();
        Rating rating = Optional.ofNullable(aggregatedReviewsRepository.getRatingByProductId(productId))
                .orElseThrow(() -> new NotFoundException("No ratings found for this product"));
        BeanUtils.copyProperties(rating, breakdownRating);
        return breakdownRating;
    }

    private void updateRatings(UUID productId, Integer rating) {
        Integer ratingCount = ratingRepository.getRatingCountByProductId(productId);
        if (ratingCount > 0) {
            switch (rating) {
                case 1:
                    ratingRepository.updateRating_1ByProductId(productId, rating);
                    break;

                case 2:
                    ratingRepository.updateRating_2ByProductId(productId, rating);
                    break;

                case 3:
                    ratingRepository.updateRating_3ByProductId(productId, rating);
                    break;

                case 4:
                    ratingRepository.updateRating_4ByProductId(productId, rating);
                    break;

                case 5:
                    ratingRepository.updateRating_5ByProductId(productId, rating);
                    break;
            }
        } else {
            Rating reviewRating = new Rating();
            ratingRepository.save(reviewRating.updateBreakDownReviews(productId, rating));
        }
    }


    private void requestBodyValidation(BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            Boolean badRequestExceptionExists = false;
            for (FieldError error : bindingResult.getFieldErrors()) {
                if (error.getRejectedValue() == null) {
                    badRequestExceptionExists = true;
                }
            }
            if (badRequestExceptionExists) {
                throw new BadRequestException("adfa");
            } else {
                throw new UnProcessableEntitiesException(removeDuplicateFieldError(resolveUnProcessableEntitiesExceptionList(bindingResult)));
            }
        }
    }

    public List<ValidationError> removeDuplicateFieldError(List<ValidationError> errors) {
        Map<String, ValidationError> errorMap = new HashMap<>();
        for (ValidationError error : errors) {
            if (!errorMap.containsKey(error.getField())) {
                errorMap.put(error.getField(), error);
            }
        }
        errors.clear();
        errors.addAll(errorMap.values());
        return errors;
    }


    public List<ValidationError> resolveUnProcessableEntitiesExceptionList(BindingResult result) {
        List<ValidationError> validationErrorList = new ArrayList<>();
        List<FieldError> errorList = new ArrayList<>();
        for (FieldError error : result.getFieldErrors()) {
            logger.error("error.getRejected() value: " + error.getRejectedValue().toString());
            if (error.getRejectedValue().toString().trim().isEmpty() || error.getRejectedValue().toString().equals("") || error.getRejectedValue().toString().equals("[]") || !error.getDefaultMessage().trim().isEmpty()) {
                errorList.add(error);
            }
        }
        validationErrorList = getValidationErrors(errorList);
        return validationErrorList;
    }

    public List<ValidationError> getValidationErrors(List<FieldError> errorList) {
        return errorList.stream().map((FieldError error) -> {
            if (Constants.REQUIRED.equalsIgnoreCase(error.getDefaultMessage())) {

                logger.info(Constants.IS_REQUIRED);
                return new ValidationError(error.getField(),
                        error.getField() + Constants.IS_REQUIRED);
            } else if (Constants.FIELD_INVALID.equalsIgnoreCase(error.getDefaultMessage())) {

                logger.info(Constants.FIELD_INVALID);
                return new ValidationError(error.getField(), Constants.FIELD_INVALID);
            } else {
                return new ValidationError(error.getField(),
                        error.getDefaultMessage());
            }
        }).collect(Collectors.toList());
    }


}
