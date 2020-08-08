package com.project.nova.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RatingTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testAllArgsConstructor() throws JsonProcessingException {
        String actual = "{\"productId\":null,\"rating_1\":0,\"rating_2\":0,\"rating_3\":0,\"rating_4\":0,\"rating_5\":0,\"weightedSum\":1.0,\"numberOfReviews\":10}";
        Rating rating = new Rating(1.0, 10);
        String expected = objectMapper.writeValueAsString(rating);
        System.out.println(expected);
        assertEquals(expected, actual);
    }

    @Test
    public void testNoArgsConstructor() throws JsonProcessingException {
        String actual = "{\"productId\":null,\"rating_1\":0,\"rating_2\":0,\"rating_3\":0,\"rating_4\":0,\"rating_5\":0,\"weightedSum\":0.0,\"numberOfReviews\":null}";
        Rating rating = new Rating();
        String expected = objectMapper.writeValueAsString(rating);
        System.out.println(expected);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetterAndSetters() throws JsonProcessingException {
        String actual = "{\"productId\":null,\"rating_1\":0,\"rating_2\":0,\"rating_3\":0,\"rating_4\":0,\"rating_5\":0,\"weightedSum\":1.0,\"numberOfReviews\":10}";
        Rating rating = new Rating();
        rating.setWeightedSum(1.0);
        rating.setNumberOfReviews(10);
        String expected = objectMapper.writeValueAsString(rating);
        System.out.println(expected);
        assertEquals(expected, actual);
    }
}
