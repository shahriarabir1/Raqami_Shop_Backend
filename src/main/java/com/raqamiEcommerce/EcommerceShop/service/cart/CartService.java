package com.raqamiEcommerce.EcommerceShop.service.cart;

import com.raqamiEcommerce.EcommerceShop.exception.ProductNotFoundException;
import com.raqamiEcommerce.EcommerceShop.model.Cart;
import com.raqamiEcommerce.EcommerceShop.model.User;
import com.raqamiEcommerce.EcommerceShop.repository.CartItemRepository;
import com.raqamiEcommerce.EcommerceShop.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

@RequiredArgsConstructor
@Service
public class CartService implements ICartService{
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final AtomicLong cartIdGenerator = new AtomicLong(0);
    @Override
    public Cart getCart(Long id) {
       Cart cart=cartRepository.findById(id)
               .orElseThrow(()-> new ProductNotFoundException("Cart not found"));
       BigDecimal totalAmount=cart.getTotalAmount();
       cart.setTotalAmount(totalAmount);
       return cartRepository.save(cart);
    }

    @Override
    public void clearCart(Long id) {
        Cart cart=getCart(id);
        cartItemRepository.deleteAllByCartId(id);
        cart.getCartItems().clear();
        cartRepository.deleteById(id);

    }

    @Override
    public BigDecimal getTotalPrice(long id) {
        Cart cart=getCart(id);
        return cart.getTotalAmount();

    }



    @Override
    public Cart initializeNewCart(User user) {
        Cart newCart = new Cart();
        Long newCartId = cartIdGenerator.incrementAndGet();
        newCart.setId(newCartId);
        return cartRepository.save(newCart);

    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }
}
