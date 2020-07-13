package com.project.nova.repository.repositoryImpl;

import com.project.nova.entity.HelpfulReview;
import com.project.nova.repository.HelpfulReviewsRepository;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.UUID;

@Repository
public class HelpfulReviewRepositoryImpl implements HelpfulReviewsRepository {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(HelpfulReviewRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public Integer hasUserMarkedHelpFull(UUID productId, UUID reviewId, UUID userId) {
        return entityManager.createQuery("select review from HelpfulReview review where review.productId = :productId and " +
        "review.reviewId = :reviewId and review.userId = :userId")
                .setParameter("productId", productId)
                .setParameter("reviewId", reviewId)
                .setParameter("userId", userId)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE).getResultList().size();

    }

    @Transactional
    @Override
    public void save(HelpfulReview helpfulReview) {
        entityManager.merge(helpfulReview);
    }
}
