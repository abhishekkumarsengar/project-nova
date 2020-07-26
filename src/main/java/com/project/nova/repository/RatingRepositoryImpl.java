package com.project.nova.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.UUID;

public abstract class RatingRepositoryImpl implements RatingRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void updateRatingByProductId(UUID productId, Integer rating) {
        String query = "UPDATE BreakdownRating breakdownRating SET breakdownRating.rating_" + rating + " = breakdownRating.rating_" + rating + " + 1" +
                " where breakdownRating.productId = :productId";

        entityManager.createQuery(query)
                .setParameter("productId", productId).executeUpdate();
    }

    @Override
    public void deleteRatingByProductId(UUID productId, Integer rating) {
        String query = "UPDATE BreakdownRating breakdownRating SET breakdownRating.rating_" + rating + " = breakdownRating.rating_" + rating + " - 1" +
                " where breakdownRating.productId = :productId";

        entityManager.createQuery(query)
                .setParameter("productId", productId).executeUpdate();
    }
}
