package com.project.nova.repository;

import com.project.nova.entity.HelpfulReview;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.util.UUID;

@Repository
public interface HelpfulReviewsRepository {

    @Lock(value = LockModeType.PESSIMISTIC_READ)
    @Query(value = "select COUNT(review) from HelpfulReview review where review.productId = :productId and " +
            "review.reviewId = :reviewId and review.userId = :userId")
    Integer hasUserMarkedHelpFull(UUID productId, UUID reviewId, UUID userId);

    public void save(HelpfulReview helpfulReview);
}
