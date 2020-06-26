package com.project.nova.repository;

import com.project.nova.entity.HelpfulReview;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface HelpfulReviewsRepository extends CrudRepository<HelpfulReview, UUID> {

    @Query(value = "select count(*) from helpful_review where product_id = ?1 and review_id = ?2 and user_id = ?3", nativeQuery = true)
    Integer hasUserMarkedHelpFull(UUID productId, UUID reviewId, UUID userId);
}
