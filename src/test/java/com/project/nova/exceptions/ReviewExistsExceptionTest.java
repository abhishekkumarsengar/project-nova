package com.project.nova.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ReviewExistsExceptionTest {

    @Test
    public void reviewExistsExceptionTest() throws Exception {
        ReviewExistsException reviewExistsException = new ReviewExistsException("success");
        ObjectMapper objectMapper = new ObjectMapper();
        String testString = objectMapper.writeValueAsString(reviewExistsException.getMessage());
        assertEquals("\"success\"", testString);
    }

    @Test
    public void reviewExistsExceptionNoArgsTest() throws Exception {
        ReviewExistsException reviewExistsException = new ReviewExistsException();
        ObjectMapper objectMapper = new ObjectMapper();
        String testString = objectMapper.writeValueAsString(reviewExistsException.getMessage());
        assertEquals("null", testString);
    }

    /**
     * Error responce test 2.
     *
     * @throws Exception the exception
     */
    @Test
    public void reviewExistsExceptionSetters() throws Exception {
        ReviewExistsException reviewExistsException = new ReviewExistsException();
        reviewExistsException.setMessage("success");

        ObjectMapper objectMapper = new ObjectMapper();
        String testString = objectMapper.writeValueAsString(reviewExistsException.getMessage());

        assertEquals("\"success\"", testString);
    }
}
