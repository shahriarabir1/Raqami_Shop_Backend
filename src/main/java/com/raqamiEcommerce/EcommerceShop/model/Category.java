package com.raqamiEcommerce.EcommerceShop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;


    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private List<Product> products;
    public Category(String name) {
        this.name=name;
    }
}
