package com.ecomerce.TechStore.user.repository;

import com.ecomerce.TechStore.user.entity.RoleName;
import com.ecomerce.TechStore.user.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleName, Long> {

    Optional<RoleName> findByName(Role name);
}
