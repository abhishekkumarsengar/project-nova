package com.project.nova.repository;

import com.project.nova.entity.Rating;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.util.UUID;

@Repository
public interface RatingRepository extends PagingAndSortingRepository<Rating, UUID> {

    @Query(value = "select count(rating) from Rating rating where rating.productId = ?1")
    Integer getRatingCountByProductId(UUID productId);

    @Transactional
    @Lock(value = LockModeType.PESSIMISTIC_READ)
    @Query(value = "select rating from Rating rating where rating.productId = :productId")
    Rating getAggregatedReviewsByProductId(UUID productId);

    @Transactional
    @Lock(value = LockModeType.PESSIMISTIC_READ)
    @Query(value = "select rating from Rating rating where rating.productId = :productId")
    Rating getRatingByProductId(UUID productId);

    @Modifying
    @Query(value = "UPDATE Rating rating SET rating.rating_1 = rating.rating_1 + 1, " +
            "rating.numberOfReviews = rating.numberOfReviews + 1, rating.weightedSum = ?2 " +
            "where rating.productId = ?1")
    void updateRating_1ByProductId(UUID productId, Double weightedSum);

    @Modifying
    @Query(value = "UPDATE Rating rating SET rating.rating_2 = rating.rating_2 + 1, " +
            "rating.numberOfReviews = rating.numberOfReviews + 1, rating.weightedSum = ?2 " +
            "where rating.productId = ?1")
    void updateRating_2ByProductId(UUID productId, Double weightedSum);

    @Modifying
    @Query(value = "UPDATE Rating rating SET rating.rating_3 = rating.rating_3 + 1, " +
            "rating.numberOfReviews = rating.numberOfReviews + 1, rating.weightedSum = ?2 " +
            "where rating.productId = ?1")
    void updateRating_3ByProductId(UUID productId, Double weightedSum);

    @Modifying
    @Query(value = "UPDATE Rating rating SET rating.rating_4 = rating.rating_4 + 1, " +
            "rating.numberOfReviews = rating.numberOfReviews + 1, rating.weightedSum = ?2 " +
            "where rating.productId = ?1")
    void updateRating_4ByProductId(UUID productId, Double weightedSum);

    @Modifying
    @Query(value = "UPDATE Rating rating SET rating.rating_5 = rating.rating_5 + 1, " +
            "rating.numberOfReviews = rating.numberOfReviews + 1, rating.weightedSum = ?2 " +
            "where rating.productId = ?1")
    void updateRating_5ByProductId(UUID productId, Double weightedSum);


    @Modifying
    @Query(value = "UPDATE Rating rating SET rating.rating_1 = rating.rating_1 - 1, " +
            "rating.numberOfReviews = rating.numberOfReviews - 1 " +
            "where rating.productId = ?1")
    void deleteRating_1ByProductId(UUID productId, Integer rating);

    @Modifying
    @Query(value = "UPDATE Rating rating SET rating.rating_2 = rating.rating_2 - 1, " +
            "rating.numberOfReviews = rating.numberOfReviews - 1 " +
            "where rating.productId = ?1")
    void deleteRating_2ByProductId(UUID productId, Integer rating);

    @Modifying
    @Query(value = "UPDATE Rating rating SET rating.rating_3 = rating.rating_3 - 1, " +
            "rating.numberOfReviews = rating.numberOfReviews - 1 " +
            "where rating.productId = ?1")
    void deleteRating_3ByProductId(UUID productId, Integer rating);

    @Modifying
    @Query(value = "UPDATE Rating rating SET rating.rating_4 = rating.rating_4 - 1, " +
            "rating.numberOfReviews = rating.numberOfReviews - 1 " +
            "where rating.productId = ?1")
    void deleteRating_4ByProductId(UUID productId, Integer rating);

    @Modifying
    @Query(value = "UPDATE Rating rating SET rating.rating_5 = rating.rating_5 - 1, " +
            "rating.numberOfReviews = rating.numberOfReviews - 1 " +
            "where rating.productId = ?1")
    void deleteRating_5ByProductId(UUID productId, Integer rating);
}

