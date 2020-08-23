package com.project.nova.configuration;

import com.project.nova.configuration.interceptors.ResponseEnvelop;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;

import java.io.IOException;

@SpringBootTest
public class ResponseEnvelopTest {

    @MockBean
    private ResponseEnvelop responseEnvelop;

    @MockBean
    private Object object;

    @MockBean
    private MethodParameter methodParameter;

    @MockBean
    private MediaType mediaType;

    @MockBean
    private Class<? extends HttpMessageConverter<?>> httpMessageConverter;

    @MockBean
    private ServerHttpRequest httpRequest;

    @MockBean
    private ServerHttpResponse serverHttpResponse;

    @BeforeEach
    void init() throws IOException {

    }

    @Test
    void testBeforeWriteBody() {
        responseEnvelop.beforeBodyWrite(object, methodParameter, mediaType, httpMessageConverter, httpRequest, serverHttpResponse);
    }
}
