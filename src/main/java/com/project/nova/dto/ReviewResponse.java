package com.project.nova.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.nova.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewResponse {

    private List<Review> reviews;

    private Cursor cursors;

    /**
     * The Page number.
     */
    private int pageNumber;
    /**
     * The Page size.
     */
    private int pageSize;
    /**
     * The Total size.
     */
    private int totalSize;
}
