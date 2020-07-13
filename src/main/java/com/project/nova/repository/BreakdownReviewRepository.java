package com.project.nova.repository;

import com.project.nova.entity.BreakdownRating;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.UUID;

@Repository
public interface BreakdownReviewRepository extends PagingAndSortingRepository<BreakdownRating, UUID> {

    @Query(value = "select count(review) from BreakdownRating review where review.productId = ?1")
    Integer getRatingCountByProductId(UUID productId);

    @Lock(value = LockModeType.PESSIMISTIC_READ)
    @Query(value = "select review from BreakdownRating review where review.productId = ?1")
    BreakdownRating getRatingByProductId(UUID productId);

    @Modifying
    @Query(value = "UPDATE BreakdownRating breakdownRating SET breakdownRating.rating_1 = breakdownRating.rating_1 + 1" +
            "where breakdownRating.productId = ?1")
    void updateRating_1ByProductId(UUID productId, Integer rating);

    @Modifying
    @Query(value = "UPDATE BreakdownRating breakdownRating SET breakdownRating.rating_2 = breakdownRating.rating_2 + 1" +
            "where breakdownRating.productId = ?1")
    void updateRating_2ByProductId(UUID productId, Integer rating);

    @Modifying
    @Query(value = "UPDATE BreakdownRating breakdownRating SET breakdownRating.rating_3 = breakdownRating.rating_3 + 1" +
            "where breakdownRating.productId = ?1")
    void updateRating_3ByProductId(UUID productId, Integer rating);

    @Modifying
    @Query(value = "UPDATE BreakdownRating breakdownRating SET breakdownRating.rating_4 = breakdownRating.rating_4 + 1" +
            "where breakdownRating.productId = ?1")
    void updateRating_4ByProductId(UUID productId, Integer rating);

    @Modifying
    @Query(value = "UPDATE BreakdownRating breakdownRating SET breakdownRating.rating_5 = breakdownRating.rating_5 + 1" +
            "where breakdownRating.productId = ?1")
    void updateRating_5ByProductId(UUID productId, Integer rating);
}
