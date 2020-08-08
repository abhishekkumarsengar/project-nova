package com.project.nova.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The type Error responce test.
 */
@SpringBootTest
public class ErrorResponceTest {

    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.OK, "success");
    /**
     * Error responce test.
     *
     * @throws Exception the exception
     */
    @Test
    public void errorResponceTest() throws Exception {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.OK, "success");
        ObjectMapper objectMapper = new ObjectMapper();
        String testString = objectMapper.writeValueAsString(errorResponse);
        assertEquals("{\"code\":\"OK\",\"message\":\"success\"}", testString);
    }

    @Test
    public void errorResponceNoArgsTest() throws Exception {
        ErrorResponse errorResponse = new ErrorResponse();
        ObjectMapper objectMapper = new ObjectMapper();
        String testString = objectMapper.writeValueAsString(errorResponse);
        assertEquals("{\"code\":null,\"message\":null}", testString);
    }

    /**
     * Error responce test 2.
     *
     * @throws Exception the exception
     */
    @Test
    public void errorResponceTest2() throws Exception {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(HttpStatus.OK);
        errorResponse.setMessage("success");
        ObjectMapper objectMapper = new ObjectMapper();
        String testString = objectMapper.writeValueAsString(errorResponse);

        assertEquals("{\"code\":\"OK\",\"message\":\"success\"}", testString);
    }
}




