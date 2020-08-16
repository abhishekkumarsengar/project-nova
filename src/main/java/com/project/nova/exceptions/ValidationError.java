package com.project.nova.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Validation error provides with validation on specific fields and the error message for them.
 * <p>
 *   Generates a no-args constructor.
 * @NoArgsConstructor Annotation Will generate an error message if such a constructor cannot be written due to the existence of final fields.
 * </p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValidationError {

    private String field;
    private String code;
    private String message;

}
