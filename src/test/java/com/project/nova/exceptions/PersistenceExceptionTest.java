package com.project.nova.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PersistenceExceptionTest {

    @Test
    public void persistenceExceptionTest() throws Exception {
        PersistenceException persistenceException = new PersistenceException("success");
        ObjectMapper objectMapper = new ObjectMapper();
        String testString = objectMapper.writeValueAsString(persistenceException.getMessage());
        assertEquals("\"success\"", testString);
    }

    @Test
    public void persistenceExceptionNoArgsTest() throws Exception {
        PersistenceException persistenceException = new PersistenceException();
        ObjectMapper objectMapper = new ObjectMapper();
        String testString = objectMapper.writeValueAsString(persistenceException.getMessage());
        assertEquals("null", testString);
    }

    /**
     * Error responce test 2.
     *
     * @throws Exception the exception
     */
    @Test
    public void persistenceExceptionSetters() throws Exception {
        PersistenceException persistenceException = new PersistenceException();
        persistenceException.setMessage("success");

        ObjectMapper objectMapper = new ObjectMapper();
        String testString = objectMapper.writeValueAsString(persistenceException.getMessage());

        assertEquals("\"success\"", testString);
    }
}
