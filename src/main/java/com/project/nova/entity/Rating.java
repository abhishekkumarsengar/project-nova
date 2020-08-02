package com.project.nova.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.nova.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "rating")
public class Rating {

    @Id
    @NotNull(message = Constants.PRODUCT_ID + Constants.FIELD_NULL)
    private UUID productId;

    @Column(name="rating_1", nullable = false, columnDefinition="INT NOT NULL DEFAULT 0")
    private Integer rating_1 = 0;

    @Column(name="rating_2", nullable = false, columnDefinition="INT NOT NULL DEFAULT 0")
    private Integer rating_2 = 0;

    @Column(name="rating_3", nullable = false, columnDefinition="INT NOT NULL DEFAULT 0")
    private Integer rating_3 = 0;

    @Column(name="rating_4", nullable = false, columnDefinition="INT NOT NULL DEFAULT 0")
    private Integer rating_4 = 0;

    @Column(name="rating_5", nullable = false, columnDefinition="INT NOT NULL DEFAULT 0")
    private Integer rating_5 = 0;

    @Column(name="weighted_sum", nullable = false, columnDefinition="INT NOT NULL DEFAULT 0")
    private Double weightedSum = 0.0;

    @NotNull(message = Constants.NO_OF_REVIEWS + Constants.FIELD_NULL)
    private Integer numberOfReviews;

    public Rating updateBreakDownReviews(UUID productId, Integer rating) {
        Rating reviewRating = new Rating();
        reviewRating.setProductId(productId);
        reviewRating.setNumberOfReviews(1);
        switch (rating) {
            case 1:
                reviewRating.setRating_1(1);
                break;

            case 2:
                reviewRating.setRating_2(1);
                break;

            case 3:
                reviewRating.setRating_3(1);
                break;

            case 4:
                reviewRating.setRating_4(1);
                break;

            case 5:
                reviewRating.setRating_5(1);
                break;
        }

        return reviewRating;
    }
}
