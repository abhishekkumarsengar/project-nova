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
}

