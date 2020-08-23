package com.project.nova.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ResponseMessageTest {

    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.OK, "success");

    @Test
    public void responseMessageAllArgsTest() throws Exception {
        ResponseMessage responseMessage = new ResponseMessage("success", errorResponse);
        ObjectMapper objectMapper = new ObjectMapper();
        String testString = objectMapper.writeValueAsString(responseMessage.getStatus());
        assertEquals("\"success\"", testString);
    }

    @Test
    public void responseMessageNoArgsTest() throws Exception {
        ResponseMessage responseMessage = new ResponseMessage();
        ObjectMapper objectMapper = new ObjectMapper();
        String testString = objectMapper.writeValueAsString(responseMessage.getStatus());
        assertEquals("null", testString);
    }

    /**
     * Error responce test 2.
     *
     * @throws Exception the exception
     */
    @Test
    public void responseMessageSetters() throws Exception {
        ResponseMessage responseMessage = new ResponseMessage("success", errorResponse);
        responseMessage.setStatus("success");
        ObjectMapper objectMapper = new ObjectMapper();
        String testString = objectMapper.writeValueAsString(responseMessage);

        assertEquals("{\"status\":\"success\",\"errorResponse\":{\"code\":\"OK\",\"message\":\"success\"}}", testString);
    }
}
