package com.project.nova.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity handleBadRequestException(BadRequestException e,
                                                    HttpServletResponse response)
            throws IOException {
        log.error(e.getMessage(), e);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(HttpStatus.BAD_REQUEST);
        errorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(new ResponseMessage("Error", errorResponse), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFoundException(NotFoundException e,
                                                    HttpServletResponse response)
            throws IOException {
        log.error(e.getMessage(), e);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(HttpStatus.NOT_FOUND);
        errorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(new ResponseMessage("Error", errorResponse), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ReviewExistsException.class)
    public ResponseEntity handleReviewExistsException(ReviewExistsException e,
                                                    HttpServletResponse response)
            throws IOException {
        log.error(e.getMessage(), e);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(HttpStatus.CONFLICT);
        errorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(new ResponseMessage("Error", errorResponse), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnProcessableEntitiesException.class)
    public ResponseEntity handleUnProcessableEntitiesException(UnProcessableEntitiesException e,
                                                               HttpServletResponse response)
            throws IOException {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ValidationErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(), e.getErrors()),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
