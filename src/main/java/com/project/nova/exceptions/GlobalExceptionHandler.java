package com.project.nova.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.project.nova.utils.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.project.nova.utils.Constants.ERROR;

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
        return new ResponseEntity<>(new ResponseMessage(ERROR, errorResponse), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFoundException(NotFoundException e,
                                                    HttpServletResponse response)
            throws IOException {
        log.error(e.getMessage(), e);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(HttpStatus.NOT_FOUND);
        errorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(new ResponseMessage(ERROR, errorResponse), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity handlePersistenceException(PersistenceException e,
                                                  HttpServletResponse response)
            throws IOException {
        log.error(e.getMessage(), e);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
        errorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(new ResponseMessage(ERROR, errorResponse), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ReviewExistsException.class)
    public ResponseEntity handleReviewExistsException(ReviewExistsException e,
                                                    HttpServletResponse response)
            throws IOException {
        log.error(e.getMessage(), e);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(HttpStatus.CONFLICT);
        errorResponse.setMessage(e.getMessage());
        return new ResponseEntity<>(new ResponseMessage(ERROR, errorResponse), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnProcessableEntitiesException.class)
    public ResponseEntity handleUnProcessableEntitiesException(UnProcessableEntitiesException e,
                                                               HttpServletResponse response)
            throws IOException {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ValidationErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(), e.getErrors()),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValid(MethodArgumentNotValidException e,
                                                               HttpServletResponse response)
            throws IOException {
        BindingResult bindingResult = e.getBindingResult();
        List<ValidationError> validationErrors = bindingResult.getFieldErrors().stream()
                .map(fieldError -> new ValidationError(fieldError.getField(), fieldError.getCode(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());

        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ValidationErrorResponse(ERROR, validationErrors),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity handleMethodArgumentNotValid(InvalidFormatException errors,
                                                       HttpServletResponse response)
            throws IOException {
        log.error(errors.getMessage(), errors);
        List<ValidationError> validationErrorList = errors.getPath().stream()
                .map(error -> new ValidationError(error.getFieldName(), HttpStatus.UNPROCESSABLE_ENTITY.toString(),
                        errors.getOriginalMessage()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(new ValidationErrorResponse(ERROR, validationErrorList),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
