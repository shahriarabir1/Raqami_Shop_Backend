package com.raqamiEcommerce.EcommerceShop.repository;

import com.raqamiEcommerce.EcommerceShop.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
    Cart findByUserId(Long userId);
}
