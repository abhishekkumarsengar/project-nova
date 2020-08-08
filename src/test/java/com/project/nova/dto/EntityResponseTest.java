package com.project.nova.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class EntityResponseTest {

    private ObjectMapper objectMapper;
    private EntityResponse entityResponse;
    AggregatedReviewsResponse aggregatedReviewsResponse;


    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper();
        entityResponse = new EntityResponse();
        aggregatedReviewsResponse = new AggregatedReviewsResponse(1.0, 1);
    }

    @Test
    public void testAllArgsConstructor() throws JsonProcessingException {
        String actual = "{\"status\":\"success\",\"data\":{\"rating\":1.0,\"numberOfReviews\":1}}";
        EntityResponse entityResponse =
                new EntityResponse("success", aggregatedReviewsResponse);
        String expected = objectMapper.writeValueAsString(entityResponse);
        System.out.println(expected);
        assertEquals(expected, actual);
    }

    @Test
    public void testNoArgsConstructor() throws JsonProcessingException {
        String actual = "{\"status\":null,\"data\":null}";
        EntityResponse entityResponse =
                new EntityResponse();
        String expected = objectMapper.writeValueAsString(entityResponse);
        System.out.println(expected);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetterAndSetters() throws JsonProcessingException {
        String actual = "{\"status\":\"success\",\"data\":{\"rating\":1.0,\"numberOfReviews\":1}}";
        EntityResponse entityResponse = new EntityResponse();
        entityResponse.setStatus("success");
        entityResponse.setData(aggregatedReviewsResponse);
        String expected = objectMapper.writeValueAsString(entityResponse);
        System.out.println(expected);
        assertEquals(expected, actual);
    }
}
