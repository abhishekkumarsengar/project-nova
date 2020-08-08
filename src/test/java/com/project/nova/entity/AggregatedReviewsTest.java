package com.project.nova.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.nova.dto.ReviewResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AggregatedReviewsTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testAllArgsConstructor() throws JsonProcessingException {
        String actual = "{\"ratingId\":\"52ff0c61-fce2-43e0-b69d-096709af5bd9\",\"productId\":\"52ff0c61-fce2-43e0-b69d-096709af5bd9\",\"rating\":1,\"numberOfReviews\":10}";
        AggregatedReviews aggregatedReviews = new AggregatedReviews(UUID.fromString("52ff0c61-fce2-43e0-b69d-096709af5bd9"),
                UUID.fromString("52ff0c61-fce2-43e0-b69d-096709af5bd9"), 1, 10);
        String expected = objectMapper.writeValueAsString(aggregatedReviews);
        System.out.println(expected);
        assertEquals(expected, actual);
    }

    @Test
    public void testNoArgsConstructor() throws JsonProcessingException {
        String actual = "{\"ratingId\":null,\"productId\":null,\"rating\":null,\"numberOfReviews\":null}";
        AggregatedReviews aggregatedReviews = new AggregatedReviews();
        String expected = objectMapper.writeValueAsString(aggregatedReviews);
        System.out.println(expected);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetterAndSetters() throws JsonProcessingException {
        String actual = "{\"ratingId\":\"52ff0c61-fce2-43e0-b69d-096709af5bd9\",\"productId\":\"52ff0c61-fce2-43e0-b69d-096709af5bd9\",\"rating\":1,\"numberOfReviews\":10}";
        AggregatedReviews aggregatedReviews = new AggregatedReviews();
        aggregatedReviews.setNumberOfReviews(10);
        aggregatedReviews.setRating(1);
        aggregatedReviews.setProductId(UUID.fromString("52ff0c61-fce2-43e0-b69d-096709af5bd9"));
        aggregatedReviews.setRatingId(UUID.fromString("52ff0c61-fce2-43e0-b69d-096709af5bd9"));
        String expected = objectMapper.writeValueAsString(aggregatedReviews);
        System.out.println(expected);
        assertEquals(expected, actual);
    }
}
