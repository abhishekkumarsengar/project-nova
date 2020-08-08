package com.project.nova.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.nova.entity.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ReviewResponseTest {

    private ObjectMapper objectMapper;
    private ReviewResponse reviewResponse;
    private Review review;
    List<Review> reviewResponseList = new ArrayList<>();

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper();
        reviewResponse = new ReviewResponse();
        review = new Review();
        reviewResponseList.add(review);
    }

    @Test
    public void testAllArgsConstructor() throws JsonProcessingException {
        String actual = "{\"reviews\":[{\"reviewId\":null,\"userId\":null,\"productId\":null,\"rating\":null,\"headline\":null,\"review\":null,\"helpful\":0,\"createdAt\":null,\"updatedAt\":null,\"deletedAt\":null}],\"pageNumber\":0,\"pageSize\":10,\"totalSize\":100}";
        ReviewResponse reviewResponse = new ReviewResponse(reviewResponseList, 0, 10, 100);
        String expected = objectMapper.writeValueAsString(reviewResponse);
        System.out.println(expected);
        assertEquals(expected, actual);
    }

    @Test
    public void testNoArgsConstructor() throws JsonProcessingException {
        String actual = "{\"reviews\":null,\"pageNumber\":0,\"pageSize\":0,\"totalSize\":0}";
        ReviewResponse reviewResponse = new ReviewResponse();
        String expected = objectMapper.writeValueAsString(reviewResponse);
        System.out.println(expected);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetterAndSetters() throws JsonProcessingException {
        String actual = "{\"reviews\":[{\"reviewId\":null,\"userId\":null,\"productId\":null,\"rating\":null,\"headline\":null,\"review\":null,\"helpful\":0,\"createdAt\":null,\"updatedAt\":null,\"deletedAt\":null}],\"pageNumber\":0,\"pageSize\":10,\"totalSize\":100}";
        ReviewResponse reviewResponse = new ReviewResponse();
        reviewResponse.setReviews(reviewResponseList);
        reviewResponse.setPageNumber(0);
        reviewResponse.setPageSize(10);
        reviewResponse.setTotalSize(100);
        String expected = objectMapper.writeValueAsString(reviewResponse);
        System.out.println(expected);
        assertEquals(expected, actual);
    }
}
