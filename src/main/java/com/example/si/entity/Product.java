package com.example.si.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private double price;

    private String picture;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "date_time")
    private LocalDateTime dataTime;

    private boolean active;

    @Column(columnDefinition = "TEXT")
    private String description;

    private int quantity;
}
