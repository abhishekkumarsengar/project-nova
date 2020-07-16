package com.project.nova.repository;

import com.project.nova.entity.AggregatedReviews;
import com.project.nova.entity.BreakdownRating;
import com.project.nova.entity.Rating;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AggregatedReviewsRepository extends CrudRepository<Rating, UUID> {

    @Transactional
    @Lock(value = LockModeType.PESSIMISTIC_READ)
    @Query(value = "select rating from Rating rating where rating.productId = :productId")
    Rating getAggregatedReviewsByProductId(UUID productId);

    @Transactional
    @Lock(value = LockModeType.PESSIMISTIC_READ)
    @Query(value = "select rating from Rating rating where rating.productId = :productId")
    Rating getRatingByProductId(UUID productId);

    @Transactional
    @Lock(value = LockModeType.PESSIMISTIC_READ)
    @Query(value = "select reviews from AggregatedReviews reviews where reviews.productId = :productId and reviews.rating = :rating")
    AggregatedReviews getAggregatedReviewsByProductIdAndRating(UUID productId, Integer rating);
}
