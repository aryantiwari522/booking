package com.booking.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "parents")
@Data
public class Parent extends BaseEntity {
    private String name;
}
