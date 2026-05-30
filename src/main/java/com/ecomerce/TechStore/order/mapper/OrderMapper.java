package com.ecomerce.TechStore.order.mapper;

import com.ecomerce.TechStore.order.dto.OrderItemResponseDTO;
import com.ecomerce.TechStore.order.dto.OrderResponseDTO;
import com.ecomerce.TechStore.order.entity.Order;
import com.ecomerce.TechStore.order.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "userId", source = "user.id")
    OrderResponseDTO toResponseDTO(Order order);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    OrderItemResponseDTO toItemResponseDTO(OrderItem orderItem);
}
