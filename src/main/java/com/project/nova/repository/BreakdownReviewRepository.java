package com.project.nova.repository;

import com.project.nova.entity.BreakdownRating;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BreakdownReviewRepository {

    BreakdownRating save(BreakdownRating breakdownRating);

    Integer getRatingCountByProductId(UUID productId);

    BreakdownRating getRatingByProductId(UUID productId);

    void updateRatingByProductId(UUID productId, Integer rating);
}
