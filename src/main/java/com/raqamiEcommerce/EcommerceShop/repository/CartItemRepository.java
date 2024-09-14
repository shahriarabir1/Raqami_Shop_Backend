package com.raqamiEcommerce.EcommerceShop.repository;

import com.raqamiEcommerce.EcommerceShop.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    void deleteAllByCartId(Long id);
}
