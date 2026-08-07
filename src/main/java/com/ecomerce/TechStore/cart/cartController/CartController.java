package com.ecomerce.TechStore.cart.cartController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecomerce.TechStore.cart.dto.CartResponseDTO;
import com.ecomerce.TechStore.cart.service.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor

public class CartController {
    private final CartService cartService;

    @GetMapping("/{id}")
    public ResponseEntity<CartResponseDTO> getUserCart(@PathVariable Long id) {
        return ResponseEntity.ok(cartService.getCartByUserId(id));
    }

}
