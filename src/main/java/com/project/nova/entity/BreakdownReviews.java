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
@Table(name = "breakdown_rating")
public class BreakdownReviews {

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

}
