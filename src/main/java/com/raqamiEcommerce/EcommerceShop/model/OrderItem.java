package com.raqamiEcommerce.EcommerceShop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_Id")
    private Product product;

    public OrderItem(Order order, Product product,int quantity, BigDecimal price) {
        this.quantity = quantity;
        this.price = price;
        this.order = order;
        this.product = product;
    }
}
