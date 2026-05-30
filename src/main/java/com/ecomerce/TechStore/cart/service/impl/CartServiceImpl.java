package com.ecomerce.TechStore.cart.service.impl;

import com.ecomerce.TechStore.cart.dto.CartItemRequestDTO;
import com.ecomerce.TechStore.cart.dto.CartResponseDTO;
import com.ecomerce.TechStore.cart.entity.Cart;
import com.ecomerce.TechStore.cart.entity.CartItem;
import com.ecomerce.TechStore.cart.mapper.CartMapper;
import com.ecomerce.TechStore.cart.repository.CartItemRepository;
import com.ecomerce.TechStore.cart.repository.CartRepository;
import com.ecomerce.TechStore.cart.service.CartService;
import com.ecomerce.TechStore.product.entity.Product;
import com.ecomerce.TechStore.product.enums.ProductStatus;
import com.ecomerce.TechStore.product.repository.ProductRepository;
import com.ecomerce.TechStore.user.entity.User;
import com.ecomerce.TechStore.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;

    @Override
    @Transactional(readOnly = true)
    public CartResponseDTO getCartByUserId(Long userId) {
        Cart cart = getOrCreateCart(userId);
        return cartMapper.toResponseDTO(cart);
    }

    @Override
    @Transactional
    public CartResponseDTO addItem(Long userId, CartItemRequestDTO requestDTO) {
        Cart cart = getOrCreateCart(userId);
        Product product = productRepository.findById(requestDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + requestDTO.getProductId()));

        if (product.getStatus() != ProductStatus.ACTIVE) {
            throw new IllegalStateException("Product is not available: " + product.getName());
        }

        // If item already exists in cart, update quantity
        CartItem cartItem = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), product.getId())
                .map(existing -> {
                    existing.setQuantity(existing.getQuantity() + requestDTO.getQuantity());
                    return existing;
                })
                .orElse(CartItem.builder()
                        .cart(cart)
                        .product(product)
                        .quantity(requestDTO.getQuantity())
                        .build());

        cartItemRepository.save(cartItem);
        return cartMapper.toResponseDTO(cartRepository.findById(cart.getId()).orElseThrow());
    }

    @Override
    @Transactional
    public CartResponseDTO updateItemQuantity(Long userId, Long cartItemId, Integer quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found with id: " + cartItemId));
        validateCartOwnership(userId, cartItem.getCart().getId());
        if (quantity <= 0) {
            cartItemRepository.delete(cartItem);
        } else {
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }
        return cartMapper.toResponseDTO(cartRepository.findByUserId(userId).orElseThrow());
    }

    @Override
    @Transactional
    public CartResponseDTO removeItem(Long userId, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found with id: " + cartItemId));
        validateCartOwnership(userId, cartItem.getCart().getId());
        cartItemRepository.delete(cartItem);
        return cartMapper.toResponseDTO(cartRepository.findByUserId(userId).orElseThrow());
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    // --- Helpers ---

    private Cart getOrCreateCart(Long userId) {
        return cartRepository.findByUserId(userId).orElseGet(() -> {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
            Cart newCart = Cart.builder().user(user).build();
            return cartRepository.save(newCart);
        });
    }

    private void validateCartOwnership(Long userId, Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        if (!cart.getUser().getId().equals(userId)) {
            throw new SecurityException("Access denied: cart does not belong to user " + userId);
        }
    }
}
