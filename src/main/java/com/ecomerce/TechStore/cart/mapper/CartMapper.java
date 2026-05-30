package com.ecomerce.TechStore.cart.mapper;

import com.ecomerce.TechStore.cart.dto.CartItemResponseDTO;
import com.ecomerce.TechStore.cart.dto.CartResponseDTO;
import com.ecomerce.TechStore.cart.entity.Cart;
import com.ecomerce.TechStore.cart.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "userId", source = "user.id")
    CartResponseDTO toResponseDTO(Cart cart);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    CartItemResponseDTO toItemResponseDTO(CartItem cartItem);
}
