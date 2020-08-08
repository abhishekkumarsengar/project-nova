package com.project.nova.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AggregatedReviewsResponseTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testAllArgsConstructor() throws JsonProcessingException {
        String actual = "{\"rating\":1.0,\"numberOfReviews\":1}";
        AggregatedReviewsResponse aggregatedReviewsResponse =
                new AggregatedReviewsResponse(1.0, 1);
        String expected = objectMapper.writeValueAsString(aggregatedReviewsResponse);
        System.out.println(expected);
        assertEquals(expected, actual);
    }

    @Test
    public void testNoArgsConstructor() throws JsonProcessingException {
        String actual = "{\"rating\":null,\"numberOfReviews\":null}";
        AggregatedReviewsResponse aggregatedReviewsResponse = new AggregatedReviewsResponse();
        String expected = objectMapper.writeValueAsString(aggregatedReviewsResponse);
        System.out.println(expected);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetterAndSetters() throws JsonProcessingException {
        String actual = "{\"rating\":1.0,\"numberOfReviews\":1}";
        AggregatedReviewsResponse aggregatedReviewsResponse = new AggregatedReviewsResponse();
        aggregatedReviewsResponse.setRating(1.0);
        aggregatedReviewsResponse.setNumberOfReviews(1);
        String expected = objectMapper.writeValueAsString(aggregatedReviewsResponse);
        System.out.println(expected);
        assertEquals(expected, actual);
    }
}
