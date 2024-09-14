package com.raqamiEcommerce.EcommerceShop.service.cart;

import com.raqamiEcommerce.EcommerceShop.exception.ProductNotFoundException;
import com.raqamiEcommerce.EcommerceShop.model.Cart;
import com.raqamiEcommerce.EcommerceShop.model.CartItem;
import com.raqamiEcommerce.EcommerceShop.model.Product;
import com.raqamiEcommerce.EcommerceShop.repository.CartItemRepository;
import com.raqamiEcommerce.EcommerceShop.repository.CartRepository;
import com.raqamiEcommerce.EcommerceShop.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService{
    private final CartItemRepository cartItemRepository;
    private final IProductService productService;
    private final ICartService cartService;
    private final CartRepository cartRepository;
    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        Cart cart=cartService.getCart(cartId);
        Product product=productService.getProductById(productId);
        CartItem cartItem=cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId()==productId)
                .findFirst()
                .orElse(new CartItem());
        if(cartItem.getId()==null){
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
        }else{
            cartItem.setQuantity
                    (cartItem.getQuantity()+quantity);
        }
        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        CartItem itemToRemove = getCartItem(cartId, productId);
        cart.removeItem(itemToRemove);
        cartRepository.save(cart);
    }

    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart=cartService.getCart(cartId);
        cart.getCartItems()
                .stream()
                .filter(item->item.getProduct().getId()==productId)
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(quantity);
                    item.setUnitPrice(item.getProduct().getPrice());
                    item.setTotalPrice();
                });
        BigDecimal totalAmount=cart.getCartItems()
                .stream().map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);
    }
    @Override
    public CartItem getCartItem(Long cartId, Long productId){
        Cart cart=cartService.getCart(cartId);
        return cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId()==productId)
                .findFirst()
                .orElseThrow(()->new ProductNotFoundException("Item Not Found"));

    }
}
