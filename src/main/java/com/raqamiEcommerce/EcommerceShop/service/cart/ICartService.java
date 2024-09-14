package com.raqamiEcommerce.EcommerceShop.service.cart;

import com.raqamiEcommerce.EcommerceShop.model.Cart;
import com.raqamiEcommerce.EcommerceShop.model.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(long id);

    Cart initializeNewCart(User user);

    Cart getCartByUserId(Long userId);
}
