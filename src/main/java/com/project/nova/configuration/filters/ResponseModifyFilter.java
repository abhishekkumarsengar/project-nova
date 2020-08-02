package com.project.nova.configuration.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.nova.dto.EntityResponse;
import com.project.nova.utils.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Component
public class ResponseModifyFilter implements Filter {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ResponseModifyFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseWrappers capturingResponseWrapper = new ResponseWrappers(
                (HttpServletResponse) response);

        chain.doFilter(request, capturingResponseWrapper);

        try {
            String apiResponse = capturingResponseWrapper.getCaptureAsString();
            if (((((HttpServletResponse) response).getStatus() == HttpStatus.OK.value() ||
                    ((HttpServletResponse) response).getStatus() == HttpStatus.CREATED.value())) &&
                    ((HttpServletResponse) response).getContentType().equalsIgnoreCase("application/json")) {
                EntityResponse entityResponse = new EntityResponse();
                entityResponse.setData(objectMapper.readValue(apiResponse, Object.class));
                entityResponse.setStatus(Constants.SUCCESS);
                String str = objectMapper.writeValueAsString(entityResponse);
                response.getOutputStream().write(str.getBytes());
            } else {
                response.getOutputStream().write(apiResponse.getBytes());
            }
        } catch (Exception e) {
            logger.error("");
        }
    }
}
