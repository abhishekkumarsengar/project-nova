package com.project.nova.repository;

import com.project.nova.dto.ReviewResponse;
import com.project.nova.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewsRepository {

        Optional<ReviewResponse> getAllReviewsByProductId(@Param("productId") UUID productId, String before, String after, Optional helpful, Pageable pageable);

        Review getReview(UUID productId, UUID reviewId);

        Optional<ReviewResponse> getReviewsByRatings(UUID productId, Integer rating, String before, String after, Pageable pageable);

        Integer checkReviewExists(UUID productId, UUID userId);

        Review save(Review review);

        void updateHelpfulInReviews(UUID productId, UUID reviewId);

}


