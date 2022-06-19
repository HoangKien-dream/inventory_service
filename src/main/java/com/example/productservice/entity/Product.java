package com.example.productservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int price;
    private  String thumbnail;
    private String description;
    private int quantity;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @Column(name = "category_id", insertable = false, updatable = false)
    private int categoryId;
    private int status;
    @CreationTimestamp
    private LocalDate createdAt;
    @UpdateTimestamp
    private LocalDate updatedAt;
    private LocalDate deletedAt;

    public Product( String name, int price, String thumbnail, String description,int quantity ,int categoryId, int status) {
        this.name = name;
        this.price = price;
        this.thumbnail = thumbnail;
        this.description = description;
        this.quantity = quantity;
        this.categoryId = categoryId;
        this.status = status;
    }
}
