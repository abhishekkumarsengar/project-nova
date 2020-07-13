package com.project.nova.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.nova.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "aggregated_rating")
public class AggregatedReviews {

    @Id
    @NotNull(message = Constants.USER_ID + Constants.FIELD_NULL)
    private UUID ratingId;

    @NotNull(message = Constants.PRODUCT_ID + Constants.FIELD_NULL)
    private UUID productId;

    @NotNull(message = Constants.RATING + Constants.FIELD_NULL)
    @Min(1)
    @Max(5)
    private Integer rating;

    @NotNull(message = Constants.NO_OF_REVIEWS + Constants.FIELD_NULL)
    private Integer numberOfReviews;

    public AggregatedReviews(UUID productId, Integer rating) {
        this.productId = productId;
        this.rating = rating;
    }
}
