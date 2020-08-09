package com.project.nova.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.nova.dto.ReviewRequest;
import com.project.nova.entity.Review;
import com.project.nova.service.ReviewsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.validation.BindingResult;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
public class ReviewsControllerTest {

    @Autowired
    private ReviewsController reviewsController;

    private MockMvc mockMvc;

    Review review;

    @MockBean
    private ReviewsService reviewsService;

    @BeforeEach
    public void setup() throws Exception {
        initMocks(this);
        mockMvc = standaloneSetup(reviewsController)
                .build();
        review = new Review();
        review.setRating(1);
        review.setHeadline("Product Review");
        review.setReview("Good product");
        review.setReviewId(UUID.randomUUID());
        review.setUserId(UUID.randomUUID());
        review.setProductId(UUID.randomUUID());
    }

    @Test
    public void testGetAllReviews() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/products/{productId}/reviews", "b56c165b-34d7-47f5-94ea-2f02c177d10f")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("rating","1")
                        .param("sort", "createdAt")
                        .param("order", "asc")
                        .param("pageNumber", "0")
                        .param("pageSize", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void testGetOneReview() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/products/{productId}/reviews/" +
                        "{reviewId}","b56c165b-34d7-47f5-94ea-2f02c177d10f", "b56c165b-34d7-47f5-94ea-2f02c177d10f")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void testCreateReview() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(review);

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/products/{id}/reviews", "eaa24095-29e9-40da-95dc-e23a78215c4b")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        Assertions.assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    public void testUpdateReview() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(review);

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.put("/api/v1/products/{id}/reviews/{reviewId}",
                        "eaa24095-29e9-40da-95dc-e23a78215c4b", "eaa24095-29e9-40da-95dc-e23a78215c4b")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void testDeleteReview() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/products/{id}/reviews/{reviewId}",
                        "eaa24095-29e9-40da-95dc-e23a78215c4b", "eaa24095-29e9-40da-95dc-e23a78215c4b")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

    @Test
    public void testIsHelpful() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.put("/api/v1/products/{id}/reviews/{reviewId}/helpful",
                        "eaa24095-29e9-40da-95dc-e23a78215c4b", "eaa24095-29e9-40da-95dc-e23a78215c4b")
                        .param("userId", "eaa24095-29e9-40da-95dc-e23a78215c4b")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void testGetAggregatedRatings() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/products/{id}/reviews/aggregated",
                        "eaa24095-29e9-40da-95dc-e23a78215c4b")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void testGetBreakDownRatings() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/products/{id}/reviews/breakdown",
                        "eaa24095-29e9-40da-95dc-e23a78215c4b")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

}
