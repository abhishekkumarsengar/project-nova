package com.project.nova.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.nova.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "helpful_review")
public class HelpfulReview {

    @Id
    @NotNull(message = Constants.USER_ID + Constants.FIELD_NULL)
    private UUID userId;

    @NotNull(message = Constants.PRODUCT_ID + Constants.FIELD_NULL)
    private UUID productId;

    @NotNull(message = Constants.REVIEW_ID + Constants.FIELD_NULL)
    private UUID reviewId;

//    private Timestamp createdAt;
//    private Timestamp updatedAt;
//    private Timestamp deletedAt;
//
//    @PrePersist
//    public void onPrePersist() {
//        setCreatedAt(new Timestamp(new Date().getTime()));
//    }
//
//    @PreUpdate
//    public void onPreUpdate() {
//        setUpdatedAt(new Timestamp(new Date().getTime()));
//    }


}
