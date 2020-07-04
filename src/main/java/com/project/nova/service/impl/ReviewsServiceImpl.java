package com.project.nova.service.impl;

import com.project.nova.dto.AggregatedReviewsResponse;
import com.project.nova.dto.ReviewRequest;
import com.project.nova.dto.ReviewResponse;
import com.project.nova.entity.AggregatedReviews;
import com.project.nova.entity.BreakdownRating;
import com.project.nova.entity.HelpfulReview;
import com.project.nova.entity.Review;
import com.project.nova.exceptions.*;
import com.project.nova.repository.*;
import com.project.nova.service.ReviewsService;
import com.project.nova.utils.Constants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
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
    private BreakdownReviewRepository breakdownReviewRepository;
    private ReviewsRepository reviewsRepository;

    @Autowired
    private ReviewsServiceImpl(ReviewsRepository reviewsRepository,
                               AggregatedReviewsRepository aggregatedReviewsRepository,
                               HelpfulReviewsRepository helpfulReviewsRepository,
                               BreakdownReviewRepository breakdownReviewRepository) {
        this.reviewsRepository = reviewsRepository;
        this.aggregatedReviewsRepository = aggregatedReviewsRepository;
        this.helpfulReviewsRepository = helpfulReviewsRepository;
        this.breakdownReviewRepository = breakdownReviewRepository;
    }

    @Override
    public ReviewResponse getAllReviews(UUID productId, Integer rating, String before, String after, String helpful, Integer pageNumber, Integer pageSize) {
        ReviewResponse reviewResponse = new ReviewResponse();
        if (rating == null) {
            try {
                reviewResponse = reviewsRepository.
                        getAllReviewsByProductId(productId, before, after, Optional.ofNullable(helpful), PageRequest.of(pageNumber, pageSize)).get();
            } catch (PessimisticLockException | LockTimeoutException | PersistenceException lockingExceptions) {
                logger.error("", lockingExceptions);
            }
        }
        else {
            reviewResponse = getReviewByRating(productId, rating, before, after, pageNumber, pageSize);
        }
        return reviewResponse;
    }

    @Override
    public Review getOneReview(UUID productId, UUID reviewId) {

        return Optional.ofNullable(reviewsRepository.getReview(productId, reviewId))
                .orElseThrow(() -> new NotFoundException("No review found with this id"));
    }

    private ReviewResponse getReviewByRating(UUID productId, Integer rating, String before, String after,
                                             Integer pageNumber, Integer pageSize) {
        return reviewsRepository
                .getReviewsByRatings(productId, rating, before, after, PageRequest.of(pageNumber, pageSize)).get();
    }

    @Override
    public Review createReview(UUID productId, ReviewRequest reviewRequest, BindingResult bindingResult) {
        requestBodyValidation(bindingResult);

        Integer doesReviewExists = reviewsRepository.checkReviewExists(productId, reviewRequest.getUserId());
        if (doesReviewExists > 0) {
            throw new ReviewExistsException("User has already submitted a review for this product");
        }

        Review review = new Review();
        BeanUtils.copyProperties(reviewRequest, review);
        review.setReviewId(UUID.randomUUID());
        review.setProductId(productId);

        List<AggregatedReviews> aggregatedReviewsList = aggregatedReviewsRepository.getAggregatedReviewsByProductIdAndRating(productId, reviewRequest.getRating());

        AggregatedReviews aggregatedReviews = new AggregatedReviews();
        if (aggregatedReviewsList.isEmpty()) {
            aggregatedReviews.setRatingId(UUID.randomUUID());
            aggregatedReviews.setNumberOfReviews(1);
        } else {

            aggregatedReviewsList.forEach(aggregatedReviewsObject -> {
                if (aggregatedReviewsObject.getRating().equals(review.getRating())) {
                    aggregatedReviews.setRatingId(aggregatedReviewsObject.getRatingId());
                    aggregatedReviews.setNumberOfReviews(aggregatedReviewsObject.getNumberOfReviews() + 1);
                } else {
                    aggregatedReviews.setRatingId(UUID.randomUUID());
                }
            });
        }
        aggregatedReviews.setProductId(productId);
        aggregatedReviews.setRating(reviewRequest.getRating());

        updateBreakDownReviews(productId, reviewRequest.getRating());
        aggregatedReviewsRepository.save(aggregatedReviews);
        return reviewsRepository.save(review);
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

    @Override
    public void isReviewHelpFull(UUID productId, UUID reviewId, UUID userId) {
        Integer userMarkedHelpFull = helpfulReviewsRepository.hasUserMarkedHelpFull(productId, reviewId, userId);

        if (userMarkedHelpFull > 0) {
            throw new ReviewExistsException("User has already marked this review as helpful");
        }

        HelpfulReview helpfulReview = new HelpfulReview(userId, productId, reviewId);

        // Atomic
        helpfulReviewsRepository.save(helpfulReview);
        reviewsRepository.updateHelpfulInReviews(productId, reviewId);
    }

    @Override
    public AggregatedReviewsResponse getAggregatedReviewsByRating(UUID productId) {
        List<AggregatedReviews> aggregatedReviewsList = aggregatedReviewsRepository.getAggregatedReviewsByProductId(productId);
        AggregatedReviewsResponse aggregatedReviewsResponse = new AggregatedReviewsResponse();
        Double rating  = aggregatedReviewsList.stream()
                .filter(aggregatedReviews -> aggregatedReviews.getRating() > 0).mapToInt(AggregatedReviews::getRating).average().getAsDouble();
        int numberOfReviews  = aggregatedReviewsList.stream()
                .filter(aggregatedReviews -> aggregatedReviews.getNumberOfReviews() > 0).mapToInt(AggregatedReviews::getNumberOfReviews).sum();
        aggregatedReviewsResponse.setRating(rating);
        aggregatedReviewsResponse.setNumberOfReviews(numberOfReviews);

        return aggregatedReviewsResponse;
    }

    @Override
    public BreakdownRating getBreakDownReviewsByRating(UUID productId) {
        return Optional.ofNullable(breakdownReviewRepository.getRatingByProductId(productId))
                .orElseThrow(() -> new NotFoundException("No ratings found for this product"));
    }

    private void updateBreakDownReviews(UUID productId, Integer rating) {
        Integer ratingCount = breakdownReviewRepository.getRatingCountByProductId(productId);
        if (ratingCount > 0) {
            breakdownReviewRepository.updateRatingByProductId(productId, rating);
        } else {
            BreakdownRating breakdownRating = new BreakdownRating();
            breakdownReviewRepository.save(breakdownRating.updateBreakDownReviews(productId, rating));
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
