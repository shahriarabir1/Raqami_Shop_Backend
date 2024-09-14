package com.raqamiEcommerce.EcommerceShop.service.order;

import com.raqamiEcommerce.EcommerceShop.dto.OrderDto;
import com.raqamiEcommerce.EcommerceShop.enums.OrderStatus;
import com.raqamiEcommerce.EcommerceShop.exception.ProductNotFoundException;
import com.raqamiEcommerce.EcommerceShop.model.Cart;
import com.raqamiEcommerce.EcommerceShop.model.Order;
import com.raqamiEcommerce.EcommerceShop.model.OrderItem;
import com.raqamiEcommerce.EcommerceShop.model.Product;
import com.raqamiEcommerce.EcommerceShop.repository.OrderRepository;
import com.raqamiEcommerce.EcommerceShop.repository.ProductRepository;
import com.raqamiEcommerce.EcommerceShop.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final ModelMapper modelMapper;

    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItemList=createOrderItems(order,cart);
        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalAmount(calculatePrice(orderItemList));
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(cart.getId());

        return savedOrder;
    }

    private BigDecimal calculatePrice(List<OrderItem> orderItems) {
        return orderItems
                .stream()
                .map(orderItem -> orderItem.getPrice()
                        .multiply(new BigDecimal(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO,BigDecimal::add);


    }
    private List<OrderItem> createOrderItems(Order order, Cart cart){
        return cart.getCartItems().stream()
                .map(item->{
                    Product product=item.getProduct();
                    product.setInventory(product.getInventory()-item.getQuantity());
                    productRepository.save(product);
                    return new OrderItem(
                            order,
                            product,
                            item.getQuantity(),
                            item.getUnitPrice()
                    );
                }).toList();
    }

    private Order createOrder(Cart cart){
        Order order=new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }
    @Override
    public OrderDto getOrder(Long orderId) {
        return orderRepository.findById(orderId).map(this::convertToDto).
                orElseThrow(()->new ProductNotFoundException("Item Not Found!"));
    }

    @Override
    public List<OrderDto> getUserOrder(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return  orders.stream().map(this :: convertToDto).toList();
    }
    @Override
    public OrderDto convertToDto(Order order) {
        return modelMapper.map(order, OrderDto.class);
    }
}
