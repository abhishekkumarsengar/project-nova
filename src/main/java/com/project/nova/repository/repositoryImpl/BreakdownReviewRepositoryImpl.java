package com.project.nova.repository.repositoryImpl;

import com.project.nova.entity.BreakdownRating;
import com.project.nova.repository.BreakdownReviewRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.UUID;

@Repository
public class BreakdownReviewRepositoryImpl implements BreakdownReviewRepository {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(BreakdownReviewRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public void updateRatingByProductId(UUID productId, Integer rating) {

        switch (rating) {
            case 1:
                entityManager.createQuery("UPDATE BreakdownRating breakdownRating SET breakdownRating.rating_1 = breakdownRating.rating_1 + 1" +
                        " where breakdownRating.productId = :productId")
                        .setParameter("productId", productId).executeUpdate();
                break;

            case 2:
                entityManager.createQuery("UPDATE BreakdownRating breakdownRating SET breakdownRating.rating_2 = breakdownRating.rating_2 + 1 " +
                        "where breakdownRating.productId = :productId")
                        .setParameter("productId", productId).executeUpdate();
                break;

            case 3:
                entityManager.createQuery("UPDATE BreakdownRating breakdownRating SET breakdownRating.rating_3 = " +
                        "breakdownRating.rating_3 + 1 where breakdownRating.productId = :productId")
                        .setParameter("productId", productId).executeUpdate();
                break;

            case 4:
                entityManager.createQuery("UPDATE BreakdownRating breakdownRating SET breakdownRating.rating_4 = breakdownRating.rating_4 + 1 " +
                        "where breakdownRating.productId = :productId")
                        .setParameter("productId", productId).executeUpdate();
                break;

            case 5:
                entityManager.createQuery("UPDATE BreakdownRating breakdownRating SET breakdownRating.rating_5 = breakdownRating.rating_5 + 1 " +
                        "where breakdownRating.productId = :productId")
                        .setParameter("productId", productId)
                        .executeUpdate();
                break;
        }
    }

    @Transactional
    @Override
    public Integer getRatingCountByProductId(UUID productId) {
        return entityManager.createQuery("select review from BreakdownRating review where review.productId = :productId")
                .setParameter("productId", productId)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .getResultList().size();
    }

    @Transactional
    @Override
    public BreakdownRating getRatingByProductId(UUID productId) {
        Query query = entityManager.createQuery("select review from BreakdownRating review where review.productId = :productId")
                .setParameter("productId", productId)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE);
        return (BreakdownRating) query.getSingleResult();
    }

    @Transactional
    @Override
    public BreakdownRating save(BreakdownRating breakdownRating) {
        entityManager.merge(breakdownRating);
        return breakdownRating;
    }
}