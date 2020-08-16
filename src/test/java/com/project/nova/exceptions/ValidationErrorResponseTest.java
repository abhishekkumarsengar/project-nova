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
    ValidationError validationError = new ValidationError("success", "code", "success");
    List<ValidationError> validationErrors = new ArrayList<>();

    @BeforeEach
    void init() {
        validationErrors.add(validationError);
    }

    @Test
    public void responseMessageAllArgsTest() throws Exception {
        ValidationErrorResponse validationErrorResponse = new ValidationErrorResponse("success", validationErrors);
        ObjectMapper objectMapper = new ObjectMapper();
        String testString = objectMapper.writeValueAsString(validationErrorResponse);
        assertEquals("{\"status\":\"success\",\"errors\":[{\"field\":\"success\",\"code\":\"code\",\"message\":\"success\"}]}", testString);
    }

    @Test
    public void responseMessageNoArgsTest() throws Exception {
        ValidationErrorResponse validationErrorResponse = new ValidationErrorResponse();
        ObjectMapper objectMapper = new ObjectMapper();
        String testString = objectMapper.writeValueAsString(validationErrorResponse);
        assertEquals("{\"status\":null,\"errors\":null}", testString);
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
        validationErrorResponse.setErrors(validationErrors);
        ObjectMapper objectMapper = new ObjectMapper();
        String testString = objectMapper.writeValueAsString(validationErrorResponse);

        assertEquals("{\"status\":\"success\",\"errors\":[{\"field\":\"success\",\"code\":\"code\",\"message\":\"success\"}]}", testString);
    }
}
