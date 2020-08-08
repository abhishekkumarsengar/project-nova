package com.project.nova.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class BreakdownRatingTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testAllArgsConstructor() throws JsonProcessingException {
        String actual = "{\"productId\":\"52ff0c61-fce2-43e0-b69d-096709af5bd9\",\"rating_1\":1,\"rating_2\":2,\"rating_3\":3,\"rating_4\":4,\"rating_5\":5}";
        BreakdownRating breakdownRating = new BreakdownRating(UUID.fromString("52ff0c61-fce2-43e0-b69d-096709af5bd9"),
                1, 2, 3, 4, 5);
        String expected = objectMapper.writeValueAsString(breakdownRating);
        System.out.println(expected);
        assertEquals(expected, actual);
    }

    @Test
    public void testNoArgsConstructor() throws JsonProcessingException {
        String actual = "{\"productId\":null,\"rating_1\":0,\"rating_2\":0,\"rating_3\":0,\"rating_4\":0,\"rating_5\":0}";
        BreakdownRating breakdownRating = new BreakdownRating();
        String expected = objectMapper.writeValueAsString(breakdownRating);
        System.out.println(expected);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetterAndSetters() throws JsonProcessingException {
        String actual = "{\"productId\":\"52ff0c61-fce2-43e0-b69d-096709af5bd9\",\"rating_1\":1,\"rating_2\":2,\"rating_3\":3,\"rating_4\":4,\"rating_5\":5}";
        BreakdownRating breakdownRating = new BreakdownRating();
        breakdownRating.setRating_1(1);
        breakdownRating.setProductId(UUID.fromString("52ff0c61-fce2-43e0-b69d-096709af5bd9"));
        breakdownRating.setRating_2(2);
        breakdownRating.setRating_3(3);
        breakdownRating.setRating_4(4);
        breakdownRating.setRating_5(5);
        String expected = objectMapper.writeValueAsString(breakdownRating);
        System.out.println(expected);
        assertEquals(expected, actual);
    }
}
