package com.project.nova.service.impl;

import com.project.nova.dto.AggregatedReviewsResponse;
import com.project.nova.dto.Cursor;
import com.project.nova.dto.ReviewRequest;
import com.project.nova.dto.ReviewResponse;
import com.project.nova.entity.AggregatedReviews;
import com.project.nova.entity.BreakdownReviews;
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
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReviewsServiceImpl implements ReviewsService {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ReviewsServiceImpl.class);

    private ReviewsRepository reviewsRepository;
    private AggregatedReviewsRepository aggregatedReviewsRepository;
    private HelpfulReviewsRepository helpfulReviewsRepository;
    private BreakdownReviewRepository breakdownReviewRepository;

    @Autowired
    private Reviews2Repository reviews2Repository;

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
        List<Review> reviewResponseQueryResult = new ArrayList<>();
        if (rating == null) {
            try {
                reviewResponse = reviews2Repository.
                        getAllReviewsByProductId(productId, before, after, Optional.ofNullable(helpful), PageRequest.of(pageNumber, pageSize)).get();
            } catch (PessimisticLockException | LockTimeoutException | PersistenceException lockingExceptions) {
                logger.error("", lockingExceptions);
            }
        } else {
            reviewResponseQueryResult = getReviewByRating(productId, rating, pageNumber, pageSize);
        }
        return reviewResponse;
    }

    @Override
    public Review getOneReview(UUID productId, UUID reviewId) {

        return Optional.ofNullable(reviewsRepository.getReview(productId, reviewId))
                .orElseThrow(() -> new NotFoundException("No review found with this id"));
    }

    private List<Review> getReviewByRating(UUID productId, Integer rating, Integer pageNumber, Integer pageSize) {
        return Optional.of(reviewsRepository
                .getReviewsByRatings(productId, rating,  PageRequest.of(pageNumber, pageSize)).getContent()).get();
    }

    @Override
    public Review createReview(UUID productId, ReviewRequest reviewRequest, BindingResult bindingResult) {

        Integer doesReviewExists = reviewsRepository.checkReviewExists(productId, reviewRequest.getUserId());
        if (doesReviewExists > 0) {
            throw new ReviewExistsException("User has already submitted a review for this product");
        }
        requestBodyValidation(bindingResult);

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

//        updateBreakDownReviews(productId, reviewRequest.getRating());
        aggregatedReviewsRepository.save(aggregatedReviews);
        return reviewsRepository.save(review);
    }


    @Override
    public Review updateReview(UUID productId, UUID reviewId, ReviewRequest reviewRequest, BindingResult bindingResult) {
        Review review = reviewsRepository.findById(reviewId).orElseThrow(() -> new NotFoundException("Review not found"));
        BeanUtils.copyProperties(reviewRequest, review);
        reviewsRepository.save(review);
        return review;
    }

    @Override
    public void deleteReview(UUID productId, UUID reviewId) {
        Review review = reviewsRepository.findById(reviewId).orElseThrow(() -> new NotFoundException("Review not found"));
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
    public BreakdownReviews getBreakDownReviewsByRating(UUID productId) {
        return breakdownReviewRepository.getBreakdownReviewsByProductId(productId);
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

    private void updateBreakDownReviews(UUID productId, Integer rating) {
        BreakdownReviews breakdownReviews = breakdownReviewRepository.getBreakdownReviewsByProductId(productId);
        if (breakdownReviews != null) {
            switch (rating) {
                case 1:
                    breakdownReviewRepository.updateRating_1_ByProductId(productId);
                    break;

                case 2:
                    breakdownReviewRepository.updateRating_2_ByProductId(productId);
                    break;

                case 3:
                    breakdownReviewRepository.updateRating_3_ByProductId(productId);
                    break;

                case 4:
                    breakdownReviewRepository.updateRating_4_ByProductId(productId);
                    break;

                case 5:
                    breakdownReviewRepository.updateRating_5_ByProductId(productId);
                    break;
            }
        } else {
            breakdownReviews = new BreakdownReviews();
            switch (rating) {
                case 1:
                    breakdownReviews.setRating_1(1);
                    break;

                case 2:
                    breakdownReviews.setRating_2(1);
                    break;

                case 3:
                    breakdownReviews.setRating_3(1);
                    break;

                case 4:
                    breakdownReviews.setRating_4(1);
                    break;

                case 5:
                    breakdownReviews.setRating_5(1);
                    break;
            }
        }
        breakdownReviews.setProductId(productId);
        breakdownReviewRepository.save(breakdownReviews);
    }
}
