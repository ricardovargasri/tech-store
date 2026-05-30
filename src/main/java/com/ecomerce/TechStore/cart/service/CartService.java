package com.ecomerce.TechStore.cart.service;

import com.ecomerce.TechStore.cart.dto.CartItemRequestDTO;
import com.ecomerce.TechStore.cart.dto.CartResponseDTO;

public interface CartService {

    CartResponseDTO getCartByUserId(Long userId);

    CartResponseDTO addItem(Long userId, CartItemRequestDTO requestDTO);

    CartResponseDTO updateItemQuantity(Long userId, Long cartItemId, Integer quantity);

    CartResponseDTO removeItem(Long userId, Long cartItemId);

    void clearCart(Long userId);
}
