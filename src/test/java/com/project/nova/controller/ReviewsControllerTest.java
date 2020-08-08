package com.project.nova.controller;

import com.project.nova.dto.ReviewRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.UUID;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
public class ReviewsControllerTest {

    @Autowired
    private ReviewsController reviewsController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        initMocks(this);
        mockMvc = standaloneSetup(reviewsController)
                .build();
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
                MockMvcRequestBuilders.get("/api/v1/products/b56c165b-34d7-47f5-94ea-2f02c177d10f/" +
                        "reviews/b56c165b-34d7-47f5-94ea-2f02c177d10f")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void testCreateReview() throws Exception {
        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setRating(1);
        reviewRequest.setUserId(UUID.randomUUID());
        reviewRequest.setReview("Good product");
        reviewRequest.setHeadline("Product review");
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/products/b56c165b-34d7-47f5-94ea-2f02c177d10f/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("reviewRequest", reviewRequest)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

}
