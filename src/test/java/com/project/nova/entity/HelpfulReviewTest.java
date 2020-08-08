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
public class HelpfulReviewTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testAllArgsConstructor() throws JsonProcessingException {
        String actual = "{\"userId\":\"52ff0c61-fce2-43e0-b69d-096709af5bd9\",\"productId\":\"52ff0c61-fce2-43e0-b69d-096709af5bd9\",\"reviewId\":\"52ff0c61-fce2-43e0-b69d-096709af5bd9\",\"createdAt\":-19799000,\"updatedAt\":-19799000,\"deletedAt\":-19799000}";
        HelpfulReview helpfulReview = new HelpfulReview(UUID.fromString("52ff0c61-fce2-43e0-b69d-096709af5bd9"),
                UUID.fromString("52ff0c61-fce2-43e0-b69d-096709af5bd9"), UUID.fromString("52ff0c61-fce2-43e0-b69d-096709af5bd9"),
                Timestamp.valueOf("1970-01-01 00:00:01"), Timestamp.valueOf("1970-01-01 00:00:01"), Timestamp.valueOf("1970-01-01 00:00:01"));
        String expected = objectMapper.writeValueAsString(helpfulReview);
        System.out.println(expected);
        assertEquals(expected, actual);
    }

    @Test
    public void testNoArgsConstructor() throws JsonProcessingException {
        String actual = "{\"userId\":null,\"productId\":null,\"reviewId\":null,\"createdAt\":null,\"updatedAt\":null,\"deletedAt\":null}";
        HelpfulReview helpfulReview = new HelpfulReview();
        String expected = objectMapper.writeValueAsString(helpfulReview);
        System.out.println(expected);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetterAndSetters() throws JsonProcessingException {
        String actual = "{\"userId\":\"52ff0c61-fce2-43e0-b69d-096709af5bd9\",\"productId\":\"52ff0c61-fce2-43e0-b69d-096709af5bd9\",\"reviewId\":\"52ff0c61-fce2-43e0-b69d-096709af5bd9\",\"createdAt\":-19799000,\"updatedAt\":-19799000,\"deletedAt\":-19799000}";
        HelpfulReview helpfulReview = new HelpfulReview();
        helpfulReview.setUserId(UUID.fromString("52ff0c61-fce2-43e0-b69d-096709af5bd9"));
        helpfulReview.setProductId(UUID.fromString("52ff0c61-fce2-43e0-b69d-096709af5bd9"));
        helpfulReview.setReviewId(UUID.fromString("52ff0c61-fce2-43e0-b69d-096709af5bd9"));
        helpfulReview.setCreatedAt(Timestamp.valueOf("1970-01-01 00:00:01"));
        helpfulReview.setUpdatedAt(Timestamp.valueOf("1970-01-01 00:00:01"));
        helpfulReview.setDeletedAt(Timestamp.valueOf("1970-01-01 00:00:01"));

        String expected = objectMapper.writeValueAsString(helpfulReview);
        System.out.println(expected);
        assertEquals(expected, actual);
    }
}
