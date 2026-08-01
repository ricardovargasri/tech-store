package com.ecomerce.TechStore.user.service;

import com.ecomerce.TechStore.user.dto.UserRequestDTO;
import com.ecomerce.TechStore.user.dto.UserResponseDTO;
import com.ecomerce.TechStore.user.enums.Role;

import java.util.List;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO requestDTO);

    UserResponseDTO getUserById(Long id);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO updateUser(Long id, UserRequestDTO requestDTO);

    void blockUser(Long id);

    void changeRole(Long id, Role role);
}
