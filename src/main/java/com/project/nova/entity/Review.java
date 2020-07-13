package com.project.nova.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.nova.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID reviewId;
    private UUID userId;
    private UUID productId;

    @NotNull(message = Constants.RATING + Constants.FIELD_NULL)
    @Min(1)
    @Max(5)
    private Integer rating;

    @NotNull(message = Constants.HEADLINE + Constants.FIELD_NULL)
    @NotEmpty(message = Constants.HEADLINE + Constants.FIELD_INVALID)
    @Size(max = 15)
    private String headline;

    private String review;

    @Column(name="helpful", nullable = false, columnDefinition="INT NOT NULL DEFAULT 0")
    private Integer helpful = 0;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "IST")
    @CreationTimestamp
    private Timestamp createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "IST")
    @LastModifiedDate
    private Timestamp updatedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "IST")
    private Timestamp deletedAt;

    public Review(UUID reviewId, UUID productId) {
        this.reviewId = reviewId;
        this.productId = productId;
    }
}
