package org.example.userservice.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @CreatedDate
    @Column(columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    private Timestamp created = Timestamp.valueOf(LocalDateTime.now());

    @LastModifiedDate
    @Column(columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    private Timestamp updated = Timestamp.valueOf(LocalDateTime.now());
}
