package com.project.nova.repository;

import com.project.nova.entity.Review;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class ReviewsRepositoryTest {

    @Autowired
    private ReviewsRepository reviewsRepository;

    @Test
    void testGetAllReviewsByProductId() {
        Review review = new Review(UUID.fromString("52ff0c61-fce2-43e0-b69d-096709af5bd9"),
                UUID.fromString("52ff0c61-fce2-43e0-b69d-096709af5bd9"),UUID.fromString("52ff0c61-fce2-43e0-b69d-096709af5bd9"),
                1, "product review", "good product", 1, null, null, null);
        reviewsRepository.save(review);

        Page<Review> reviewFromMemory = reviewsRepository.getAllReviewsByProductId(UUID.fromString("52ff0c61-fce2-43e0-b69d-096709af5bd9"), PageRequest.of(0, 10));
        assertEquals(review.getRating(), reviewFromMemory.getContent().get(0).getRating());
    }
}
