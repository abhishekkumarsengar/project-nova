package com.project.nova.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UnProcessableEntityExceptionTest {

    ValidationError validationError = new ValidationError();
    List<ValidationError> validationErrors = new ArrayList<>();

    @BeforeEach
    void init() {
        validationError.setField("error");
        validationError.setMessage("error");
        validationErrors.add(validationError);
    }

    @Test
    public void unProcessableEntityExceptionTest() throws Exception {
        UnProcessableEntitiesException unProcessableEntitiesException = new UnProcessableEntitiesException(validationErrors);
        ObjectMapper objectMapper = new ObjectMapper();
        String testString = objectMapper.writeValueAsString(unProcessableEntitiesException.getErrors());
        assertEquals("[{\"field\":\"error\",\"message\":\"error\"}]", testString);
    }

    @Test
    public void unProcessableEntityExceptionNoArgsTest() throws Exception {
        UnProcessableEntitiesException unProcessableEntitiesException = new UnProcessableEntitiesException();
        ObjectMapper objectMapper = new ObjectMapper();
        String testString = objectMapper.writeValueAsString(unProcessableEntitiesException.getErrors());
        assertEquals("null", testString);
    }

    /**
     * Error responce test 2.
     *
     * @throws Exception the exception
     */
    @Test
    public void unProcessableEntityExceptionSetters() throws Exception {
        UnProcessableEntitiesException unProcessableEntitiesException = new UnProcessableEntitiesException();
        unProcessableEntitiesException.setErrors(validationErrors);

        ObjectMapper objectMapper = new ObjectMapper();
        String testString = objectMapper.writeValueAsString(unProcessableEntitiesException.getErrors());

        assertEquals("[{\"field\":\"error\",\"message\":\"error\"}]", testString);
    }
}
