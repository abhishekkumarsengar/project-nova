package com.project.nova.repository;

import com.project.nova.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface Reviews2Repository extends PagingAndSortingRepository<Review, UUID> {

    @Query(value = "select r from Review r where r.productId = :productId")
    Page<Review> getAllReviewsByProductId(@Param("productId") UUID productId, Pageable pageable);

    @Query(value = "select r from Review r where r.productId = ?1 and r.reviewId = ?2")
    Review getReview(UUID productId, UUID reviewId);

    @Query(value = "select r from Review r where r.productId = ?1 and r.rating = ?2")
    Page<Review> getReviewsByRatings(UUID productId, Integer rating, Pageable pageable);

    @Query(value = "select count(r) from Review r where r.productId = ?1 and r.userId = ?2")
    Integer checkReviewExists(UUID productId, UUID userId);

    @Modifying
    @Query(value = "UPDATE Review review SET review.helpful = review.helpful + 1 where review.productId = ?1 and review.reviewId = ?2")
    void updateHelpfulInReviews(UUID productId, UUID reviewId);
}
