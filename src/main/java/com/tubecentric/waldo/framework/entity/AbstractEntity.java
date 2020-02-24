package com.tubecentric.waldo.framework.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractEntity {

    @Id
    @GeneratedValue(generator="uuid2")
    @GenericGenerator(name="uuid2", strategy="org.hibernate.id.UUIDGenerator")
    @Column(name="id")
    private String id;

    @Version
    @Column(name="version")
    private Long version;

    @CreatedBy
    @Column(name="created_by")
    private String createdBy;

    @CreatedDate
    @Column(name="created_date")
    private LocalDateTime createdDate;

    @LastModifiedBy
    @Column(name="last_modified_by")
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(name="last_modified_date")
    private LocalDateTime lastModifiedDate;
}
