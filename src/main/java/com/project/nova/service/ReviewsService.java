package com.project.nova.service;

import com.project.nova.dto.AggregatedReviewsResponse;
import com.project.nova.dto.ReviewRequest;
import com.project.nova.dto.ReviewResponse;
import com.project.nova.entity.BreakdownReviews;
import com.project.nova.entity.Review;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import java.util.UUID;

@Service
public interface ReviewsService {

    ReviewResponse getAllReviews(UUID productId, Integer rating, String before, String after, String helpful, Integer pageNumber, Integer pageSize);

    Review getOneReview(UUID productId, UUID reviewId);

    Review createReview(UUID productId, ReviewRequest reviewRequest, BindingResult bindingResult);

    Review updateReview(UUID productId, UUID reviewId, ReviewRequest reviewRequest, BindingResult bindingResult);

    void deleteReview(UUID productId, UUID reviewId);

    void isReviewHelpFull(UUID productId, UUID reviewId, UUID userId);

    AggregatedReviewsResponse getAggregatedReviewsByRating(UUID productId);

    BreakdownReviews getBreakDownReviewsByRating(UUID productId);
}
