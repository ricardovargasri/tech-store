package com.ecomerce.TechStore.user.mapper;

import com.ecomerce.TechStore.user.dto.UserRequestDTO;
import com.ecomerce.TechStore.user.dto.UserResponseDTO;
import com.ecomerce.TechStore.user.entity.Role;
import com.ecomerce.TechStore.user.entity.User;
import com.ecomerce.TechStore.user.enums.RoleName;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(UserRequestDTO requestDTO);

    @Mapping(target = "roles", expression = "java(mapRoles(user.getRoles()))")
    UserResponseDTO toResponseDTO(User user);

    default List<RoleName> mapRoles(Set<Role> roles) {
        if (roles == null) {
            return Collections.emptyList();
        }
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toList());
    }
}
