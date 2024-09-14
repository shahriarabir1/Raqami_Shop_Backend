package com.raqamiEcommerce.EcommerceShop.service.order;

import com.raqamiEcommerce.EcommerceShop.dto.OrderDto;
import com.raqamiEcommerce.EcommerceShop.model.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);
    List<OrderDto> getUserOrder(Long userId);

    OrderDto convertToDto(Order order);
}
