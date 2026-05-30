package com.booking.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "courses")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Course extends BaseEntity {
    private String title;

    // store the creator teacher id so we know who created the course
    private Long teacherId;
}
