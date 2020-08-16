package com.project.nova.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ValidationErrorTest {

    @Test
    public void responseMessageAllArgsTest() throws Exception {
        ValidationError validationError = new ValidationError("success","code", "success");
        ObjectMapper objectMapper = new ObjectMapper();
        String testString = objectMapper.writeValueAsString(validationError);
        assertEquals("{\"field\":\"success\",\"code\":\"code\",\"message\":\"success\"}", testString);
    }

    @Test
    public void responseMessageNoArgsTest() throws Exception {
        ValidationError validationError = new ValidationError();
        ObjectMapper objectMapper = new ObjectMapper();
        String testString = objectMapper.writeValueAsString(validationError.getMessage());
        assertEquals("null", testString);
    }

    /**
     * Error responce test 2.
     *
     * @throws Exception the exception
     */
    @Test
    public void responseMessageSetters() throws Exception {
        ValidationError validationError = new ValidationError();
        validationError.setMessage("success");
        validationError.setCode("success");
        validationError.setField("success");
        ObjectMapper objectMapper = new ObjectMapper();
        String testString = objectMapper.writeValueAsString(validationError);

        assertEquals("{\"field\":\"success\",\"code\":\"success\",\"message\":\"success\"}", testString);
    }
}
