package com.project.nova.repository;

import com.project.nova.entity.AggregatedReviews;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AggregatedReviewsRepository extends CrudRepository<AggregatedReviews, UUID> {

    @Query(value = "select * from aggregated_rating where product_id = ?1", nativeQuery = true)
    List<AggregatedReviews> getAggregatedReviewsByProductId(UUID productId);

    @Query(value = "select * from aggregated_rating where product_id = ?1 and rating = ?2", nativeQuery = true)
    List<AggregatedReviews> getAggregatedReviewsByProductIdAndRating(UUID productId, Integer rating);
}
