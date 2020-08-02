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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import javax.persistence.LockTimeoutException;
import javax.persistence.PessimisticLockException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

@Service
public class ReviewsServiceImpl implements ReviewsService {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ReviewsServiceImpl.class);

    private HelpfulReviewsRepository helpfulReviewsRepository;
    private RatingRepository ratingRepository;
    private ReviewsRepository reviewsRepository;

    @Autowired
    private RatingRepositoryImpl ratingRepositoryImpl;

    @Autowired
    ReviewsServiceImpl(ReviewsRepository reviewsRepository, HelpfulReviewsRepository helpfulReviewsRepository,
                       RatingRepository ratingRepository) {
        this.reviewsRepository = reviewsRepository;
        this.helpfulReviewsRepository = helpfulReviewsRepository;
        this.ratingRepository = ratingRepository;
    }

    @Override
    public ReviewResponse getAllReviews(UUID productId, Integer rating, String sort, String order, Integer pageNumber, Integer pageSize) {
        Page<Review> reviewResponseQueryResult = null;
        if (rating == null) {
            try {
                if (order.equalsIgnoreCase("desc")) {
                    reviewResponseQueryResult = reviewsRepository.getAllReviewsByProductId(productId, PageRequest.of(pageNumber, pageSize, Sort.by(sort).descending()));
                } else {
                    reviewResponseQueryResult = reviewsRepository.getAllReviewsByProductId(productId, PageRequest.of(pageNumber, pageSize, Sort.by(sort).ascending()));
                }
            } catch (PessimisticLockException | LockTimeoutException | PersistenceException lockingExceptions) {
                logger.error("Error while acquiring lock ", lockingExceptions);
                throw new PersistenceException("Error while acquiring lock");
            }
        } else {
            if (order.equalsIgnoreCase("desc")) {
                reviewResponseQueryResult = reviewsRepository.getReviewsByRatings(productId, rating, PageRequest.of(pageNumber, pageSize, Sort.by(order).descending()));
            } else {
                reviewResponseQueryResult = reviewsRepository.getReviewsByRatings(productId, rating, PageRequest.of(pageNumber, pageSize, Sort.by(order).ascending()));
            }
        }
        return new ReviewResponse(reviewResponseQueryResult.getContent(), pageNumber, pageSize, (int) reviewResponseQueryResult.getTotalElements());
    }

    @Override
    public Review getOneReview(UUID productId, UUID reviewId) {

        return Optional.ofNullable(reviewsRepository.getReview(productId, reviewId))
                .orElseThrow(() -> new NotFoundException("No review found with this id"));
    }

    @Transactional
    @Override
    public Review createReview(UUID productId, ReviewRequest reviewRequest, BindingResult bindingResult) throws Exception {
        requestBodyValidation(bindingResult);

        Integer doesReviewExists = reviewsRepository.checkReviewExists(productId, reviewRequest.getUserId()).get();
        if (doesReviewExists > 0) {
            logger.error("User has already submitted a review for this product");
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


    @Transactional
    @Override
    public Review updateReview(UUID productId, UUID reviewId, ReviewRequest reviewRequest, BindingResult bindingResult) {
        requestBodyValidation(bindingResult);
        Review review = Optional.ofNullable(reviewsRepository.getReview(productId, reviewId))
                .orElseThrow(() -> new NotFoundException("Review not found"));
        try {
            updateRatings(productId, reviewRequest.getRating());
            decrementRatings(productId, review.getRating());
            BeanUtils.copyProperties(reviewRequest, review);
            return reviewsRepository.save(review);
        } catch (RuntimeException exception) {
            logger.error("Error while persisting data during update operation. Rolling back data" + exception);
            throw new PersistenceException("Error while persisting data. Rolling back data");
        }
    }

    @Transactional
    @Override
    public void deleteReview(UUID productId, UUID reviewId) {
        Review review = Optional.ofNullable(reviewsRepository.getReview(productId, reviewId))
                .orElseThrow(() -> new NotFoundException("Review not found"));
        review.setDeletedAt(new Timestamp(new Date().getTime()));
        decrementRatings(productId, review.getRating());
        reviewsRepository.save(review);
    }


    @Transactional
    @Override
    public void isReviewHelpFull(UUID productId, UUID reviewId, UUID userId) {
        Integer userMarkedHelpFull = helpfulReviewsRepository.hasUserMarkedHelpFull(productId, reviewId, userId);

        if (userMarkedHelpFull > 0) {
            logger.error("User has already marked this review as helpful");
            throw new ReviewExistsException("User has already marked this review as helpful");
        }
        HelpfulReview helpfulReview = new HelpfulReview(userId, productId, reviewId);
        helpfulReviewsRepository.save(helpfulReview);
        reviewsRepository.updateHelpfulInReviews(productId, reviewId);
    }

    @Override
    public AggregatedReviewsResponse getAggregatedReviewsByRating(UUID productId) {
        Rating aggregatedReview = Optional.ofNullable(ratingRepository
                .getAggregatedReviewsByProductId(productId)).get();
        return new AggregatedReviewsResponse(aggregatedReview.getWeightedSum(), aggregatedReview.getNumberOfReviews());
    }

    @Override
    public BreakdownRating getBreakDownReviewsByRating(UUID productId) {
        BreakdownRating breakdownRating = new BreakdownRating();
        Rating rating = Optional.ofNullable(ratingRepository.getRatingByProductId(productId))
                .orElseThrow(() -> new NotFoundException("No ratings found for this product"));
        BeanUtils.copyProperties(rating, breakdownRating);
        return breakdownRating;
    }

    private void updateRatings(UUID productId, Integer rating) {
        Rating ratingCount = ratingRepository.getRatingByProductId(productId);
        if (ratingCount != null) {
            ratingRepositoryImpl.updateRatingByProductId(productId, rating, ratingCount);
        } else {
            Rating reviewRating = new Rating();
            ratingRepository.save(reviewRating.updateBreakDownReviews(productId, rating));
        }
    }

    private void decrementRatings(UUID productId, Integer rating) {
        Rating ratingCount = ratingRepository.getRatingByProductId(productId);
        if (ratingCount != null) {
            ratingRepositoryImpl.deleteRatingByProductId(productId, rating, ratingCount);
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
