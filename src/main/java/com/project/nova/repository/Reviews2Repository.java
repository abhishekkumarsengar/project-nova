package com.project.nova.repository;

import com.project.nova.dto.ReviewResponse;
import com.project.nova.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.UUID;

public interface Reviews2Repository {

        Optional<ReviewResponse> getAllReviewsByProductId(@Param("productId") UUID productId, String before, String after, Optional helpful, Pageable pageable);

        Review getReview(UUID productId, UUID reviewId);

        Page<Review> getReviewsByRatings(UUID productId, Integer rating, Pageable pageable);

        Integer checkReviewExists(UUID productId, UUID userId);

        Review save(Review review);

        void updateHelpfulInReviews(UUID productId, UUID reviewId);
    }


