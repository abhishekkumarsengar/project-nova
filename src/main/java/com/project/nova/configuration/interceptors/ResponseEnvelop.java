package com.project.nova.configuration.interceptors;

import com.project.nova.dto.EntityResponse;
import com.project.nova.utils.Constants;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class ResponseEnvelop implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> converterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        if (((ServletServerHttpResponse) response).getServletResponse().getStatus() == HttpStatus.OK.value() ||
                ((ServletServerHttpResponse) response).getServletResponse().getStatus() == HttpStatus.CREATED.value()) {
            return new EntityResponse(Constants.SUCCESS, body);
        }
        return body;
    }
}
