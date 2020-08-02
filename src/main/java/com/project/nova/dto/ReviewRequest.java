package com.project.nova.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.nova.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewRequest implements Serializable {

    @NotNull(message = Constants.USER_ID + Constants.FIELD_NULL)
    private UUID userId;

    @NotNull(message = Constants.RATING + Constants.FIELD_NULL)
    @Min(1)
    @Max(5)
    private Integer rating;

    @NotNull(message = Constants.HEADLINE + Constants.FIELD_NULL)
    @NotEmpty(message = Constants.HEADLINE + Constants.FIELD_INVALID)
    @Size(max = 15)
    private String headline;

    @NotNull(message = Constants.REVIEW + Constants.FIELD_NULL)
    @NotEmpty(message = Constants.REVIEW + Constants.FIELD_INVALID)
    private String review;
}
