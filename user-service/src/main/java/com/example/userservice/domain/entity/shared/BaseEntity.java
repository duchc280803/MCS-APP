package com.example.userservice.domain.entity.shared;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;
import org.eclipse.persistence.annotations.AdditionalCriteria;

import java.io.Serializable;
import java.time.OffsetDateTime;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@AdditionalCriteria("this.isDeleted = false")
@FieldNameConstants
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 3712848328289479407L;

    @Column(name = "IS_DELETED")
    private boolean isDeleted;

    @Column(name = "DELETED_AT")
    private OffsetDateTime deletedAt;

    @Column(name = "CREATED_AT", updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private OffsetDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
        if (this.updatedAt == null){
            updatedAt = OffsetDateTime.now();
        }
        isDeleted = false;
    }

    @PreUpdate
    protected void onUpdate() {
        if (this.updatedAt == null){
            updatedAt = OffsetDateTime.now();
        }
        if (this.isDeleted && this.deletedAt == null) {
            deletedAt = OffsetDateTime.now();
        } else if (!this.isDeleted) {
            deletedAt = null;
        }
    }

}

