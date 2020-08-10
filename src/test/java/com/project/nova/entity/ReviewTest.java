package com.project.nova.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ReviewTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testAllArgsConstructor() throws JsonProcessingException {
        String actual = "{\"reviewId\":\"52ff0c61-fce2-43e0-b69d-096709af5bd9\",\"userId\":\"52ff0c61-fce2-43e0-b69d-096709af5bd9\",\"productId\":\"52ff0c61-fce2-43e0-b69d-096709af5bd9\",\"rating\":1,\"headline\":\"product review\",\"review\":\"good product\",\"helpful\":1,\"createdAt\":null,\"updatedAt\":null,\"deletedAt\":null}";
        Review review = new Review(UUID.fromString("52ff0c61-fce2-43e0-b69d-096709af5bd9"),
                UUID.fromString("52ff0c61-fce2-43e0-b69d-096709af5bd9"),UUID.fromString("52ff0c61-fce2-43e0-b69d-096709af5bd9"),
                1, "product review", "good product", 1, null, null, null);
        String expected = objectMapper.writeValueAsString(review);
        assertEquals(expected, actual);
    }
}
