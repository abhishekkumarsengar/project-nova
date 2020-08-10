package com.project.nova.configuration;

import com.project.nova.configuration.filters.ResponseModifyFilter;
import com.project.nova.configuration.filters.ResponseWrappers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

import static org.mockito.Mockito.doReturn;

@SpringBootTest
public class ResponseModifyFilterTest {

    @Autowired
    private ResponseModifyFilter responseModifyFilter;

    @MockBean
    private ByteArrayOutputStream byteArrayOutputStream;

    @MockBean
    private ServletRequest servletRequest;

    @MockBean
    private ServletResponse servletResponse;

    @MockBean
    private HttpServletResponse httpServletResponse;

    @MockBean
    private ResponseWrappers responseWrappers;

    @MockBean
    private FilterChain filterChain;

    @Test
    void testDoFilter() throws IOException, ServletException {

        HttpServletRequest mockReq = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse mockResp = Mockito.mock(HttpServletResponse.class);
        FilterChain mockFilterChain = Mockito.mock(FilterChain.class);
        FilterConfig mockFilterConfig = Mockito.mock(FilterConfig.class);
        mockResp.setCharacterEncoding("charset");
        // mock the getRequestURI() response
        Mockito.when(mockReq.getRequestURI()).thenReturn("/api/v1");

        BufferedReader br = new BufferedReader(new StringReader("test"));
        // mock the getReader() call
        Mockito.when(mockReq.getReader()).thenReturn(br);
        doReturn("success").when(responseWrappers).getCaptureAsString();

        responseModifyFilter.doFilter(mockReq, mockResp, mockFilterChain);
    }

    @Test
    void testDoFilterForSwagger() throws IOException, ServletException {

        HttpServletRequest mockReq = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse mockResp = Mockito.mock(HttpServletResponse.class);
        FilterChain mockFilterChain = Mockito.mock(FilterChain.class);
        FilterConfig mockFilterConfig = Mockito.mock(FilterConfig.class);
        // mock the getRequestURI() response
        Mockito.when(mockReq.getRequestURI()).thenReturn("swagger");

        BufferedReader br = new BufferedReader(new StringReader("test"));
        // mock the getReader() call
        Mockito.when(mockReq.getReader()).thenReturn(br);
        doReturn("success").when(responseWrappers).getCaptureAsString();

        responseModifyFilter.doFilter(mockReq, mockResp, mockFilterChain);
    }
}
