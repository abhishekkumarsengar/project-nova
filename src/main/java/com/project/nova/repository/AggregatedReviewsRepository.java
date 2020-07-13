package com.project.nova.repository;

import com.project.nova.entity.AggregatedReviews;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Repository
public interface AggregatedReviewsRepository extends CrudRepository<AggregatedReviews, UUID> {

    @Lock(value = LockModeType.PESSIMISTIC_READ)
    @Query(value = "select reviews from AggregatedReviews reviews where reviews.productId = :productId")
    List<AggregatedReviews> getAggregatedReviewsByProductId(UUID productId);

    @Lock(value = LockModeType.PESSIMISTIC_READ)
    @Query(value = "select reviews from AggregatedReviews reviews where reviews.productId = :productId and reviews.rating = :rating")
    AggregatedReviews getAggregatedReviewsByProductIdAndRating(UUID productId, Integer rating);
}
