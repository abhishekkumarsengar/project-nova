package com.project.nova.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AppContextTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void TestAllArgsConstructor() throws JsonProcessingException {
        String actual = "{\"postgresUserName\":\"admin\",\"postgresPassword\":\"password\",\"postgresDatabase\":\"nova\"}";
        AppContext appContext = new AppContext("admin", "password", "nova");
        String expected = objectMapper.writeValueAsString(appContext);
        System.out.println(expected);
        assertEquals(expected, actual);
    }

    @Test
    void TestNoArgsConstructor() throws JsonProcessingException {
        String actual = "{\"postgresUserName\":null,\"postgresPassword\":null,\"postgresDatabase\":null}";
        AppContext appContext = new AppContext();
        String expected = objectMapper.writeValueAsString(appContext);
        System.out.println(expected);
        assertEquals(expected, actual);
    }

    @Test
    void TestSetters() throws JsonProcessingException {
        String actual = "{\"postgresUserName\":\"admin\",\"postgresPassword\":\"password\",\"postgresDatabase\":\"nova\"}";
        AppContext appContext = new AppContext();
        appContext.setPostgresUserName("admin");
        appContext.setPostgresPassword("password");
        appContext.setPostgresDatabase("nova");
        String expected = objectMapper.writeValueAsString(appContext);
        System.out.println(expected);
        assertEquals(expected, actual);
    }
}
