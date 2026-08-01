package com.ecomerce.TechStore.user.service.impl;

import com.ecomerce.TechStore.user.dto.UserRequestDTO;
import com.ecomerce.TechStore.user.dto.UserResponseDTO;
import com.ecomerce.TechStore.user.entity.RoleName;
import com.ecomerce.TechStore.user.entity.User;
import com.ecomerce.TechStore.user.enums.Role;
import com.ecomerce.TechStore.user.mapper.UserMapper;
import com.ecomerce.TechStore.user.repository.RoleRepository;
import com.ecomerce.TechStore.user.repository.UserRepository;
import com.ecomerce.TechStore.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO requestDTO) {
        if (userRepository.existsByEmail(requestDTO.email())) {
            throw new IllegalArgumentException("Email already registered: " + requestDTO.email());
        }
        User user = userMapper.toEntity(requestDTO);
        user.setPassword(passwordEncoder.encode(requestDTO.password()));

        RoleName customerRole = roleRepository.findByName(Role.ROLE_CUSTOMER)
                .orElseThrow(() -> new RuntimeException("Default role not found. Please seed the roles table."));
        user.setRoles(Set.of(customerRole));

        return userMapper.toResponseDTO(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return userMapper.toResponseDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(Long id, UserRequestDTO requestDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setName(requestDTO.name());
        if (requestDTO.password() != null && !requestDTO.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(requestDTO.password()));
        }
        return userMapper.toResponseDTO(userRepository.save(user));
    }

    @Override
    @Transactional
    public void blockUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void changeRole(Long id, Role role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        RoleName roleName = roleRepository.findByName(role)
                .orElseThrow(() -> new RuntimeException("Role not found: " + role));
        user.setRoles(Set.of(roleName));
        userRepository.save(user);
    }
}
