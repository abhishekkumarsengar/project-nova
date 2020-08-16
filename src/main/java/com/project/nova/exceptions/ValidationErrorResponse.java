package com.project.nova.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

/**
 * The type Validation error response.
 * @AllArgsConstructor annotation Generates an all-args constructor.
 * An all-args constructor requires one argument for every field in the class.
 * <p>
 * Generates a no-args constructor.
 * @NoArgsConstructor Annotation will generate an error message if such a constructor cannot be written due to the existence of final fields.
 * </p>
 * @JsonIgnoreProperties is used to either suppress serialization of properties (during
 * serialization), or ignore processing of JSON properties read (during deserialization). </p>
 * <p>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties("message")
public class ValidationErrorResponse {
    private String status;
    private List<ValidationError> errors;
}
