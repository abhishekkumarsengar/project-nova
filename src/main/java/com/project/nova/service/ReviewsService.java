package com.project.nova.service;

import com.project.nova.dto.AggregatedReviewsResponse;
import com.project.nova.dto.ReviewRequest;
import com.project.nova.dto.ReviewResponse;
import com.project.nova.entity.BreakdownRating;
import com.project.nova.entity.Rating;
import com.project.nova.entity.Review;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import java.util.UUID;

@Service
public interface ReviewsService {

    ReviewResponse getAllReviews(UUID productId, Integer rating, String helpful, Integer pageNumber, Integer pageSize);

    Review getOneReview(UUID productId, UUID reviewId);

    Review createReview(UUID productId, ReviewRequest reviewRequest, BindingResult bindingResult) throws Exception;

    Review updateReview(UUID productId, UUID reviewId, ReviewRequest reviewRequest, BindingResult bindingResult);

    void deleteReview(UUID productId, UUID reviewId);

    void isReviewHelpFull(UUID productId, UUID reviewId, UUID userId);

    AggregatedReviewsResponse getAggregatedReviewsByRating(UUID productId);

    BreakdownRating getBreakDownReviewsByRating(UUID productId);
}
