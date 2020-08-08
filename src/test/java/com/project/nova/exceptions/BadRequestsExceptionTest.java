package com.project.nova.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class BadRequestsExceptionTest {
    private ObjectMapper objectMapper = new ObjectMapper();

    List<ValidationError> errors =  new ArrayList<>();

    @Test
    public void testBadRequest() throws Exception {
        BadRequestException badRequestsException = new BadRequestException("success");
        String actualString = objectMapper.writeValueAsString(badRequestsException.getMessage());
        assertEquals("\"success\"", actualString);
    }
}
