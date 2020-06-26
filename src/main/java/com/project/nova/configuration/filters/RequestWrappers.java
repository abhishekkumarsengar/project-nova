package com.project.nova.configuration.filters;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

public class RequestWrappers extends HttpServletRequestWrapper {

    private ByteArrayInputStream byteArrayInputStream;
    private ServletInputStream servletInputStream;
    BufferedReader bufferedReader;
    StringBuilder stringBuilder = new StringBuilder();

    public RequestWrappers(HttpServletRequest request, ByteArrayInputStream byteArrayInputStream, ServletInputStream servletInputStream) {
        super(request);
        this.byteArrayInputStream = byteArrayInputStream;
        this.servletInputStream = servletInputStream;
    }

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public RequestWrappers(HttpServletRequest request) {
        super(request);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {

        servletInputStream = new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {

            }

            public int read ()
                    throws IOException {
                return byteArrayInputStream.read();
            }
        };
        return servletInputStream;
    }

    @Override
    public ServletRequest getRequest() {
        return super.getRequest();
    }

}
