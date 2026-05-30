package com.booking.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "parents")
@Data
@EqualsAndHashCode(callSuper = true)
public class Parent extends BaseEntity {
    private String name;
}
