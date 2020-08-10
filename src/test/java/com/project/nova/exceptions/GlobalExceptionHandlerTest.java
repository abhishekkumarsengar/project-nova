package com.project.nova.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.nova.controller.ReviewsController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
public class GlobalExceptionHandlerTest {

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @MockBean
    private ReviewsController reviewsController;

    @MockBean
    private HttpServletResponse httpServletResponse;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(reviewsController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testBadRequestException() throws Exception {
        BadRequestException badRequestException = new BadRequestException("bad request");
        ResponseEntity responseEntity = globalExceptionHandler.handleBadRequestException(badRequestException, httpServletResponse);
        assertEquals(responseEntity.getStatusCodeValue(), 400);
    }

    @Test
    public void testNotFoundException() throws Exception {
        NotFoundException notFoundException = new NotFoundException("not found");
        ResponseEntity responseEntity = globalExceptionHandler.handleNotFoundException(notFoundException, httpServletResponse);
        assertEquals(responseEntity.getStatusCodeValue(), 404);
    }

    @Test
    public void testPersistenceException() throws Exception {
        PersistenceException persistenceException = new PersistenceException("persistence exception");
        ResponseEntity responseEntity = globalExceptionHandler.handlePersistenceException(persistenceException, httpServletResponse);
        assertEquals(responseEntity.getStatusCodeValue(), 500);
    }

    @Test
    public void testHandleReviewExistsException() throws Exception {
        ReviewExistsException reviewExistsException = new ReviewExistsException("review exists exception");
        ResponseEntity responseEntity = globalExceptionHandler.handleReviewExistsException(reviewExistsException, httpServletResponse);
        assertEquals(responseEntity.getStatusCodeValue(), 409);
    }

    @Test
    public void testHandleUnProcessableEntitiesException() throws Exception {
        List<ValidationError> validationErrors = new ArrayList<>();
        UnProcessableEntitiesException unProcessableEntitiesException = new UnProcessableEntitiesException(new ArrayList<>(validationErrors));
        ResponseEntity responseEntity = globalExceptionHandler.handleUnProcessableEntitiesException(unProcessableEntitiesException, httpServletResponse);
        assertEquals(responseEntity.getStatusCodeValue(), 422);
    }
}
