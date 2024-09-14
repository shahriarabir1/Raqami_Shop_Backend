package com.raqamiEcommerce.EcommerceShop.request;

import com.raqamiEcommerce.EcommerceShop.model.Category;
import com.raqamiEcommerce.EcommerceShop.model.Image;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AddProductRequest {
    private long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private  int inventory;
    private String description;
    private Category category;
}
