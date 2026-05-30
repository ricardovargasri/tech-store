package com.ecomerce.TechStore.order.service;

import com.ecomerce.TechStore.order.dto.OrderResponseDTO;
import com.ecomerce.TechStore.order.enums.OrderStatus;

import java.util.List;

public interface OrderService {

    OrderResponseDTO createOrderFromCart(Long userId);

    OrderResponseDTO getOrderById(Long id);

    List<OrderResponseDTO> getOrdersByUserId(Long userId);

    OrderResponseDTO updateOrderStatus(Long id, OrderStatus status);
}
