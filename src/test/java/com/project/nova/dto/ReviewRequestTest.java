package com.project.nova.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ReviewRequestTest {

    private ObjectMapper objectMapper;
    private EntityResponse entityResponse;

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper();
        entityResponse = new EntityResponse();
    }

    @Test
    public void testAllArgsConstructor() throws JsonProcessingException {
        String actual = "{\"userId\":\"bab8cd93-80b5-45a1-8af8-7ff947095d62\",\"rating\":1,\"headline\":\"product review\",\"review\":\"good product\"}";
        ReviewRequest reviewRequest =
                new ReviewRequest(UUID.fromString("bab8cd93-80b5-45a1-8af8-7ff947095d62"),
                        1, "product review", "good product");
        String expected = objectMapper.writeValueAsString(reviewRequest);
        System.out.println(expected);
        assertEquals(expected, actual);
    }

    @Test
    public void testNoArgsConstructor() throws JsonProcessingException {
        String actual = "{\"userId\":null,\"rating\":null,\"headline\":null,\"review\":null}";
        ReviewRequest reviewRequest = new ReviewRequest();
        String expected = objectMapper.writeValueAsString(reviewRequest);
        System.out.println(expected);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetterAndSetters() throws JsonProcessingException {
        String actual = "{\"userId\":\"bab8cd93-80b5-45a1-8af8-7ff947095d62\",\"rating\":1,\"headline\":\"product review\",\"review\":\"good product\"}";
        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setUserId(UUID.fromString("bab8cd93-80b5-45a1-8af8-7ff947095d62"));
        reviewRequest.setHeadline("product review");
        reviewRequest.setReview("good product");
        reviewRequest.setRating(1);
        String expected = objectMapper.writeValueAsString(reviewRequest);
        System.out.println(expected);
        assertEquals(expected, actual);
    }
}
