package com.project.nova.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import java.sql.Timestamp;

@Getter
@Setter
@EnableJpaAuditing
public class Audits {

    @CreatedDate
    private Timestamp createdAt;
    @LastModifiedDate
    private Timestamp updatedAt;
    private Timestamp deletedAt;

//    @PrePersist
//    public void onPrePersist(Review object) {
//        object.setCreatedAt(new Timestamp(new Date().getTime()));
//    }
//
//    @PreUpdate
//    public void onPreUpdate(Review object) {
//        object.setUpdatedAt(new Timestamp(new Date().getTime()));
//    }
}


