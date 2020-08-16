package com.project.nova.configuration.interceptors;

import com.project.nova.dto.ReviewRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ReviewValidator implements Validator {

    @Override
    public void validate(Object target, Errors errors) {
        ReviewRequest reviewRequest = (ReviewRequest) target;
//        errors.addAllErrors(errors);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return ReviewRequest.class.isAssignableFrom(clazz);
    }
}
