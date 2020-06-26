package com.project.nova.repository;

import com.project.nova.entity.BreakdownReviews;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.UUID;

public interface BreakdownReviewRepository extends CrudRepository<BreakdownReviews, UUID> {

    @Query(value = "select * from breakdown_rating where product_id = ?1", nativeQuery = true)
    BreakdownReviews getBreakdownReviewsByProductId(UUID productId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE breakdown_rating SET rating_1 = rating_1 + 1 where product_id = ?1", nativeQuery = true)
    void updateRating_1_ByProductId(UUID productId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE breakdown_rating SET rating_2 = rating_ + 1 where product_id = ?1", nativeQuery = true)
    void updateRating_2_ByProductId(UUID productId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE breakdown_rating SET rating_3 = rating_3 + 1 where product_id = ?1", nativeQuery = true)
    void updateRating_3_ByProductId(UUID productId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE breakdown_rating SET rating_4 = rating_4 + 1 where product_id = ?1", nativeQuery = true)
    void updateRating_4_ByProductId(UUID productId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE breakdown_rating SET rating_5 = rating_5 + 1 where product_id = ?1", nativeQuery = true)
    void updateRating_5_ByProductId(UUID productId);
}
