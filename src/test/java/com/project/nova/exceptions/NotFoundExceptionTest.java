package com.project.nova.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class NotFoundExceptionTest {

    @Test
    public void notFoundTest() throws Exception {
        NotFoundException notFoundException = new NotFoundException("success");
        ObjectMapper objectMapper = new ObjectMapper();
        String testString = objectMapper.writeValueAsString(notFoundException.getMessage());
        assertEquals("\"success\"", testString);
    }

    @Test
    public void notFoundNoArgsTest() throws Exception {
        NotFoundException notFoundException = new NotFoundException();
        ObjectMapper objectMapper = new ObjectMapper();
        String testString = objectMapper.writeValueAsString(notFoundException.getMessage());
        assertEquals("null", testString);
    }

    /**
     * Error responce test 2.
     *
     * @throws Exception the exception
     */
    @Test
    public void notFoundSetters() throws Exception {
        NotFoundException notFoundException = new NotFoundException();
        notFoundException.setMessage("success");

        ObjectMapper objectMapper = new ObjectMapper();
        String testString = objectMapper.writeValueAsString(notFoundException.getMessage());

        assertEquals("\"success\"", testString);
    }
}
