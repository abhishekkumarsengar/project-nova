package com.project.nova.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ValidationErrorResponseTest {
    ValidationError validationError = new ValidationError("success", "success");
    List<ValidationError> validationErrors = new ArrayList<>();

    @BeforeEach
    void init() {
        validationErrors.add(validationError);
    }

    @Test
    public void responseMessageAllArgsTest() throws Exception {
        ValidationErrorResponse validationErrorResponse = new ValidationErrorResponse(200, "success", validationErrors);
        ObjectMapper objectMapper = new ObjectMapper();
        String testString = objectMapper.writeValueAsString(validationErrorResponse);
        assertEquals("{\"code\":200,\"status\":\"success\",\"errors\":[{\"field\":\"success\",\"message\":\"success\"}]}", testString);
    }

    @Test
    public void responseMessageNoArgsTest() throws Exception {
        ValidationErrorResponse validationErrorResponse = new ValidationErrorResponse();
        ObjectMapper objectMapper = new ObjectMapper();
        String testString = objectMapper.writeValueAsString(validationErrorResponse);
        assertEquals("{\"code\":null,\"status\":null,\"errors\":null}", testString);
    }

    /**
     * Error responce test 2.
     *
     * @throws Exception the exception
     */
    @Test
    public void responseMessageSetters() throws Exception {
        ValidationErrorResponse validationErrorResponse = new ValidationErrorResponse();
        validationErrorResponse.setStatus("success");
        validationErrorResponse.setCode(200);
        validationErrorResponse.setErrors(validationErrors);
        ObjectMapper objectMapper = new ObjectMapper();
        String testString = objectMapper.writeValueAsString(validationErrorResponse);

        assertEquals("{\"code\":200,\"status\":\"success\",\"errors\":[{\"field\":\"success\",\"message\":\"success\"}]}", testString);
    }
}
