package com.project.nova.configuration.interceptors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.nova.configuration.filters.RequestWrappers;
import com.project.nova.configuration.filters.ResponseWrappers;
import com.project.nova.dto.EntityResponse;
import com.project.nova.exceptions.BadRequestException;
import com.project.nova.exceptions.UnProcessableEntitiesException;
import com.project.nova.exceptions.ValidationError;
import com.project.nova.service.impl.ReviewsServiceImpl;
import com.project.nova.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ValidationInterceptor extends HandlerInterceptorAdapter {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ValidationInterceptor.class);

    ModelAndView modelAndView = new ModelAndView();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("Came here");

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    private void requestBodyValidation(BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            Boolean badRequestExceptionExists = false;
            for (FieldError error : bindingResult.getFieldErrors()) {
                if (error.getRejectedValue() == null) {
                    badRequestExceptionExists = true;
                }
            }
            if (badRequestExceptionExists) {
                throw new BadRequestException("adfa");
            } else {
                throw new UnProcessableEntitiesException(removeDuplicateFieldError(resolveUnProcessableEntitiesExceptionList(bindingResult)));
            }
        }
    }

    public List<ValidationError> removeDuplicateFieldError(List<ValidationError> errors) {
        Map<String, ValidationError> errorMap = new HashMap<>();
        for (ValidationError error : errors) {
            if (!errorMap.containsKey(error.getField())) {
                errorMap.put(error.getField(), error);
            }
        }
        errors.clear();
        errors.addAll(errorMap.values());
        return errors;
    }


    public List<ValidationError> resolveUnProcessableEntitiesExceptionList(BindingResult result) {
        List<ValidationError> validationErrorList = new ArrayList<>();
        List<FieldError> errorList = new ArrayList<>();
        for (FieldError error : result.getFieldErrors()) {
            logger.error("error.getRejected() value: " + error.getRejectedValue().toString());
            if (error.getRejectedValue().toString().trim().isEmpty() || error.getRejectedValue().toString().equals("") || error.getRejectedValue().toString().equals("[]") || !error.getDefaultMessage().trim().isEmpty()) {
                errorList.add(error);
            }
        }
        validationErrorList = getValidationErrors(errorList);
        return validationErrorList;
    }

    public List<ValidationError> getValidationErrors(List<FieldError> errorList) {
        return errorList.stream().map((FieldError error) -> {
            if (Constants.REQUIRED.equalsIgnoreCase(error.getDefaultMessage())) {

                logger.info(Constants.IS_REQUIRED);
                return new ValidationError(error.getField(),
                        error.getField() + Constants.IS_REQUIRED);
            } else if (Constants.FIELD_INVALID.equalsIgnoreCase(error.getDefaultMessage())) {

                logger.info(Constants.FIELD_INVALID);
                return new ValidationError(error.getField(), Constants.FIELD_INVALID);
            } else {
                return new ValidationError(error.getField(),
                        error.getDefaultMessage());
            }
        }).collect(Collectors.toList());
    }
}
