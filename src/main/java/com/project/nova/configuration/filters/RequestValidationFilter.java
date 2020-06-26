package com.project.nova.configuration.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.nova.dto.ReviewRequest;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;


//@Component
public class RequestValidationFilter implements Filter {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(RequestValidationFilter.class);
    BufferedReader bufferedReader;
    StringBuilder stringBuilder = new StringBuilder();
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        RequestWrappers capturingResponseWrapper = new RequestWrappers((HttpServletRequest) request);

//        InputStream inputStream = request.getInputStream();
//        if (inputStream != null) {
//            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//            char[] charBuffer = new char[128];
//            int bytesRead = -1;
//            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
//                stringBuilder.append(charBuffer, 0, bytesRead);
//            }
//        }
        ServletRequest abc = capturingResponseWrapper.getRequest();
        ReviewRequest reviewRequest = objectMapper.readValue(stringBuilder.toString(), ReviewRequest.class);
        chain.doFilter(capturingResponseWrapper, response);

    }
}
