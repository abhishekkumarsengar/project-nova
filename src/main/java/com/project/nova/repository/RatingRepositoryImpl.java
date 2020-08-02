package com.project.nova.repository;

import com.project.nova.entity.Rating;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.UUID;

@Repository
public class RatingRepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;

    public void updateRatingByProductId(UUID productId, Integer rating, Rating ratingCount) {
        String query = "UPDATE Rating rating SET rating.rating_" + rating + "= :rating, " +
                "rating.numberOfReviews = rating.numberOfReviews + 1, rating.weightedSum = :weightedSum " +
                "where rating.productId = :productId";

        double weightedSum = 0;
        Integer ratingValue = 0;
        switch (rating) {
                case 1:
                    weightedSum = (double) (5 * ratingCount.getRating_5() + 4 * ratingCount.getRating_4() +
                            3 * ratingCount.getRating_3() + 2 * ratingCount.getRating_2() + (ratingCount.getRating_1() + 1))
                            / (ratingCount.getNumberOfReviews() + 1);
                    ratingValue = ratingCount.getRating_1() + 1;
                    break;

                case 2:
                    weightedSum = (double) (5 * ratingCount.getRating_5() + 4 * ratingCount.getRating_4() +
                            3 * ratingCount.getRating_3() + 2 * (ratingCount.getRating_2() + 1) + ratingCount.getRating_1())
                            / (ratingCount.getNumberOfReviews() + 1);
                    ratingValue = ratingCount.getRating_2() + 1;
                    break;

                case 3:
                    weightedSum = (double) (5 * ratingCount.getRating_5() + 4 * ratingCount.getRating_4() +
                            3 * (ratingCount.getRating_3() + 1) + 2 * ratingCount.getRating_2() + ratingCount.getRating_1())
                            / (ratingCount.getNumberOfReviews() + 1);
                    ratingValue = ratingCount.getRating_3() + 1;
                    break;

                case 4:
                    weightedSum = (double) (5 * ratingCount.getRating_5() + 4 * (ratingCount.getRating_4() + 1) +
                            3 * ratingCount.getRating_3() + 2 * ratingCount.getRating_2() + ratingCount.getRating_1())
                            / (ratingCount.getNumberOfReviews() + 1);
                    ratingValue = ratingCount.getRating_4() + 1;
                    break;

                case 5:
                    weightedSum = (double) (5 * (ratingCount.getRating_5() + 1) + 4 * ratingCount.getRating_4() +
                            3 * ratingCount.getRating_3() + 2 * ratingCount.getRating_2() + ratingCount.getRating_1())
                            / (ratingCount.getNumberOfReviews() + 1);
                    ratingValue = ratingCount.getRating_5() + 1;
                    break;
            }
        entityManager.createQuery(query)
                .setParameter("productId", productId)
                .setParameter("rating", ratingValue)
                .setParameter("weightedSum", weightedSum).executeUpdate();
    }


    public void deleteRatingByProductId(UUID productId, Integer rating, Rating ratingCount) {
        String query = "UPDATE Rating rating SET rating.rating_" + rating + "= :rating, " +
                "rating.numberOfReviews = rating.numberOfReviews - 1, rating.weightedSum = :weightedSum " +
                "where rating.productId = :productId";

        double weightedSum = 0;
        Integer ratingValue = 0;
        switch (rating) {
            case 1:
                weightedSum = (double) (5 * ratingCount.getRating_5() + 4 * ratingCount.getRating_4() +
                        3 * ratingCount.getRating_3() + 2 * ratingCount.getRating_2() + (ratingCount.getRating_1() - 1))
                        / (ratingCount.getNumberOfReviews() - 1);
                ratingValue = ratingCount.getRating_1() - 1;
                break;

            case 2:
                weightedSum = (double) (5 * ratingCount.getRating_5() + 4 * ratingCount.getRating_4() +
                        3 * ratingCount.getRating_3() + 2 * (ratingCount.getRating_2() - 1) + ratingCount.getRating_1())
                        / (ratingCount.getNumberOfReviews() - 1);
                ratingValue = ratingCount.getRating_2() - 1;
                break;

            case 3:
                weightedSum = (double) (5 * ratingCount.getRating_5() + 4 * ratingCount.getRating_4() +
                        3 * (ratingCount.getRating_3() - 1) + 2 * ratingCount.getRating_2() + ratingCount.getRating_1())
                        / (ratingCount.getNumberOfReviews() - 1);
                ratingValue = ratingCount.getRating_3() - 1;
                break;

            case 4:
                weightedSum = (double) (5 * ratingCount.getRating_5() + 4 * (ratingCount.getRating_4() - 1) +
                        3 * ratingCount.getRating_3() + 2 * ratingCount.getRating_2() + ratingCount.getRating_1())
                        / (ratingCount.getNumberOfReviews() - 1);
                ratingValue = ratingCount.getRating_4() - 1;
                break;

            case 5:
                weightedSum = (double) (5 * (ratingCount.getRating_5() - 1) + 4 * ratingCount.getRating_4() +
                        3 * ratingCount.getRating_3() + 2 * ratingCount.getRating_2() + ratingCount.getRating_1())
                        / (ratingCount.getNumberOfReviews() - 1);
                ratingValue = ratingCount.getRating_5() - 1;
                break;
        }
        entityManager.createQuery(query)
                .setParameter("productId", productId)
                .setParameter("rating", ratingValue)
                .setParameter("weightedSum", weightedSum).executeUpdate();
    }
}
