package com.ecomerce.TechStore.order.service.impl;

import com.ecomerce.TechStore.cart.entity.Cart;
import com.ecomerce.TechStore.cart.entity.CartItem;
import com.ecomerce.TechStore.cart.repository.CartRepository;
import com.ecomerce.TechStore.order.dto.OrderResponseDTO;
import com.ecomerce.TechStore.order.entity.Order;
import com.ecomerce.TechStore.order.entity.OrderItem;
import com.ecomerce.TechStore.order.enums.OrderStatus;
import com.ecomerce.TechStore.order.mapper.OrderMapper;
import com.ecomerce.TechStore.order.repository.OrderRepository;
import com.ecomerce.TechStore.order.service.OrderService;
import com.ecomerce.TechStore.product.entity.Product;
import com.ecomerce.TechStore.product.repository.ProductRepository;
import com.ecomerce.TechStore.user.entity.User;
import com.ecomerce.TechStore.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponseDTO createOrderFromCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));

        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot create order from an empty cart.");
        }

        Order order = Order.builder().user(user).status(OrderStatus.PENDING).build();

        List<OrderItem> orderItems = cart.getItems().stream().map(cartItem -> {
            // Use pessimistic lock to ensure stock integrity (RC-001)
            Product product = productRepository.findByIdWithLock(cartItem.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + cartItem.getProduct().getId()));

            if (product.getStock() < cartItem.getQuantity()) {
                throw new IllegalStateException(
                        "Insufficient stock for product: " + product.getName() +
                        ". Available: " + product.getStock() + ", Requested: " + cartItem.getQuantity());
            }

            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            return OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(cartItem.getQuantity())
                    .price(product.getPrice())
                    .build();
        }).collect(Collectors.toList());

        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        // Clear the cart after successful order
        cart.getItems().clear();
        cartRepository.save(cart);

        return orderMapper.toResponseDTO(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        return orderMapper.toResponseDTO(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(orderMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponseDTO updateOrderStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        order.setStatus(status);
        return orderMapper.toResponseDTO(orderRepository.save(order));
    }
}
