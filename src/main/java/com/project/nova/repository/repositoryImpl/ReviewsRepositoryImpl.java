package com.project.nova.repository.repositoryImpl;

import com.project.nova.dto.Cursor;
import com.project.nova.dto.ReviewResponse;
import com.project.nova.entity.Review;
import com.project.nova.exceptions.PersistenceException;
import com.project.nova.repository.ReviewsRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.*;

@Repository
public class ReviewsRepositoryImpl implements ReviewsRepository {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ReviewsRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public Optional<ReviewResponse> getAllReviewsByProductId(UUID productId, String before, String after, Optional helpful, Pageable pageable) {
        List reviewList = new ArrayList<>();
        Query query;
        String base64Decoded = "";

        if (helpful.isEmpty()) {
            if (after != null && !after.isEmpty()) {
                byte[] afterDateBytes = Base64.getDecoder().decode(after);
                base64Decoded = new String(afterDateBytes, StandardCharsets.UTF_8);
                query = entityManager.createQuery("select review from Review review " +
                        "where review.productId = :productId and review.createdAt > :createdAt order by createdAt ASC");
            } else {
                byte[] beforeDateBytes = Base64.getDecoder().decode(before);
                base64Decoded = new String(beforeDateBytes, StandardCharsets.UTF_8);
                query = entityManager.createQuery("select review from Review review " +
                        "where review.productId = :productId and review.createdAt < :createdAt order by createdAt DESC");
            }
        } else {
            query = entityManager.createQuery("select review from Review review " +
                    "where review.productId = :productId order by helpful desc");
        }

        try {
             reviewList = query
                     .setParameter("productId", productId)
                     .setParameter("createdAt", Timestamp.valueOf(base64Decoded))
                     .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                     .setMaxResults(pageable.getPageSize())
                     .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                     .getResultList();
        } catch (PessimisticLockException | LockTimeoutException | PersistenceException lockingExceptions) {
            logger.error("", lockingExceptions);
        }
        return Optional.of(setCursorAndPaginationValues(reviewList, pageable, after, before));
    }

    /**
     * Sets the Cursor values with the next Page and the previous page details.
     * Also sets the pagination value.
     * @param reviewList
     * @param pageable
     * @param after
     * @param before
     * @return
     */
    private ReviewResponse setCursorAndPaginationValues(List<Review> reviewList, Pageable pageable, String after, String before) {
        ReviewResponse reviewResponse = new ReviewResponse();
        Cursor cursor = new Cursor();
        if (!reviewList.isEmpty()){
            String cursorDate = String.valueOf(reviewList.get(reviewList.size() - 1).getCreatedAt());
            cursor.setNextPage(Base64.getEncoder().encodeToString(cursorDate.getBytes(StandardCharsets.UTF_8)));
//            String beforeDate = String.valueOf(reviewResponseQueryResult.get(reviewResponseQueryResult.size() - 1).getCreatedAt());
            cursor.setPreviousPage(Base64.getEncoder().encodeToString(cursorDate.getBytes(StandardCharsets.UTF_8)));
        } else {
            if (before != null && !before.isEmpty()) {
                cursor.setPreviousPage(before);
                cursor.setNextPage(before);
            } else {
                cursor.setPreviousPage(after);
                cursor.setNextPage(after);
            }
        }
        reviewResponse.setPageNumber(pageable.getPageNumber());
        reviewResponse.setCursors(cursor);
        reviewResponse.setPageSize(pageable.getPageSize());
        reviewResponse.setReviews(reviewList);
        reviewResponse.setTotalSize(reviewList.size());
        return reviewResponse;
    }

    @Transactional
    @Override
    public Review getReview(UUID productId, UUID reviewId) {
        Query query = entityManager.createQuery("select r from Review r where r.productId = :productId and r.reviewId = :reviewId")
                .setParameter("productId", productId)
                .setParameter("reviewId", reviewId)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE);
        return (Review) query.getSingleResult();
    }

    @Override
    public Optional<ReviewResponse> getReviewsByRatings(UUID productId, Integer rating, Pageable pageable) {
        return null;
//        ReviewResponse reviewResponse = new ReviewResponse();
//
//        Query query = entityManager.createQuery("select r from Review r where r.productId = :productId and r.rating = :rating")
//                .setParameter("productId", productId)
//                .setParameter("rating", rating);
//        return
    }

    @Transactional
    @Override
    public Integer checkReviewExists(UUID productId, UUID userId) {
        Query query = entityManager.createQuery("select r from Review r where r.productId = :productId and r.userId = :userId")
                .setParameter("productId", productId)
                .setParameter("userId", userId)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE);
        return query.getResultList().size();
    }

    @Transactional
    @Override
    public Review save(Review review) {
        entityManager.merge(review);
        return review;
    }

    @Transactional
    @Override
    public void updateHelpfulInReviews(UUID productId, UUID reviewId) {

        Query query = entityManager.createQuery("UPDATE Review review SET review.helpful = review.helpful + 1 where " +
                "review.productId = :productId and review.reviewId = :reviewId")
                .setParameter("productId", productId)
                .setParameter("reviewId", reviewId);
        query.getResultList();
    }
}
