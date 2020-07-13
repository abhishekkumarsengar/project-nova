package com.project.nova.repository;

import com.project.nova.entity.HelpfulReview;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import javax.persistence.LockModeType;
import java.util.UUID;

@Repository
public interface HelpfulReviewsRepository extends CrudRepository<HelpfulReview, UUID> {

    @Query(value = "select COUNT(review) from HelpfulReview review where review.productId = :productId and " +
            "review.reviewId = :reviewId and review.userId = :userId")
    Integer hasUserMarkedHelpFull(UUID productId, UUID reviewId, UUID userId);
}
