package com.booking.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "teachers")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Teacher extends BaseEntity {
    private String name;
}
