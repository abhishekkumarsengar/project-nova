package com.project.nova.service.impl;

import com.project.nova.dto.AggregatedReviewsResponse;
import com.project.nova.dto.ReviewRequest;
import com.project.nova.dto.ReviewResponse;
import com.project.nova.entity.*;
import com.project.nova.exceptions.*;
import com.project.nova.repository.*;
import com.project.nova.service.ReviewsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.LockTimeoutException;
import javax.persistence.PessimisticLockException;
import java.sql.Timestamp;
import java.util.*;

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
            reviewResponseQueryResult = getReviewByRatings(productId, rating, sort, order, pageNumber, pageSize);
        }
        return new ReviewResponse(reviewResponseQueryResult.getContent(), pageNumber, pageSize, (int) reviewResponseQueryResult.getTotalElements());
    }

    private Page<Review> getReviewByRatings(UUID productId, Integer rating, String sort, String order, Integer pageNumber, Integer pageSize) {
        Page<Review> reviewResponseQueryResult = null;
        try {
            if (order.equalsIgnoreCase("desc")) {
                reviewResponseQueryResult = reviewsRepository.getReviewsByRatings(productId, rating, PageRequest.of(pageNumber, pageSize, Sort.by(sort).descending()));
            } else {
                reviewResponseQueryResult = reviewsRepository.getReviewsByRatings(productId, rating, PageRequest.of(pageNumber, pageSize, Sort.by(sort).ascending()));
            }
        } catch (PessimisticLockException | LockTimeoutException | PersistenceException lockingExceptions) {
            logger.error("Error while acquiring lock ", lockingExceptions);
            throw new PersistenceException("Error while acquiring lock");
        }
        return reviewResponseQueryResult;
    }

    @Override
    public Review getOneReview(UUID productId, UUID reviewId) {

        return Optional.ofNullable(reviewsRepository.getReview(productId, reviewId))
                .orElseThrow(() -> new NotFoundException("No review found with this id"));
    }

    @Transactional
    @Override
    public Review createReview(UUID productId, ReviewRequest reviewRequest) {
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
    public Review updateReview(UUID productId, UUID reviewId, ReviewRequest reviewRequest) {
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
        try {
            decrementRatings(productId, review.getRating());
            reviewsRepository.save(review);
        } catch (RuntimeException exception) {
            logger.error("Error while persisting data during update operation. Rolling back data" + exception);
            throw new PersistenceException("Error while persisting data. Rolling back data");
        }
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
        try {
            helpfulReviewsRepository.save(helpfulReview);
            reviewsRepository.updateHelpfulInReviews(productId, reviewId);
        } catch (RuntimeException exception) {
            logger.error("Error while persisting data during update operation. Rolling back data" + exception);
            throw new PersistenceException("Error while persisting data. Rolling back data");
        }
    }

    @Override
    public AggregatedReviewsResponse getAggregatedReviewsByRating(UUID productId) {
        Rating aggregatedReview = Optional.ofNullable(ratingRepository
                .getAggregatedReviewsByProductId(productId)).get();
        return new AggregatedReviewsResponse(aggregatedReview.getWeightedSum()/aggregatedReview.getNumberOfReviews(),
                aggregatedReview.getNumberOfReviews());
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
}
