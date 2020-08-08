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
public class BreakDownReviewResponseTest {

    private ObjectMapper objectMapper;
    private AggregatedReviewsResponse aggregatedReviewsResponse;
    private List<AggregatedReviewsResponse> aggregatedReviewsResponseList;

    @BeforeEach
    void init() {
        this.objectMapper = new ObjectMapper();
        aggregatedReviewsResponse = new AggregatedReviewsResponse();
        aggregatedReviewsResponse.setRating(1.0);
        aggregatedReviewsResponse.setNumberOfReviews(1);
        aggregatedReviewsResponseList = new ArrayList<>();
        aggregatedReviewsResponseList.add(aggregatedReviewsResponse);
    }

    @Test
    public void testAllArgsConstructor() throws JsonProcessingException {
        String actual = "{\"aggregatedReviewsResponseList\":[{\"rating\":1.0,\"numberOfReviews\":1}]}";
        BreakDownReviewResponse breakDownReviewResponse =
                new BreakDownReviewResponse(aggregatedReviewsResponseList);
        String expected = objectMapper.writeValueAsString(breakDownReviewResponse);
        System.out.println(expected);
        assertEquals(expected, actual);
    }

    @Test
    public void testNoArgsConstructor() throws JsonProcessingException {
        String actual = "{\"aggregatedReviewsResponseList\":null}";
        BreakDownReviewResponse breakDownReviewResponse =
                new BreakDownReviewResponse();
        String expected = objectMapper.writeValueAsString(breakDownReviewResponse);
        System.out.println(expected);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetterAndSetters() throws JsonProcessingException {
        String actual = "{\"aggregatedReviewsResponseList\":[{\"rating\":1.0,\"numberOfReviews\":1}]}";
        BreakDownReviewResponse breakDownReviewResponse =
                new BreakDownReviewResponse();
        breakDownReviewResponse.setAggregatedReviewsResponseList(aggregatedReviewsResponseList);
        String expected = objectMapper.writeValueAsString(breakDownReviewResponse);
        System.out.println(expected);
        assertEquals(expected, actual);
    }
}
