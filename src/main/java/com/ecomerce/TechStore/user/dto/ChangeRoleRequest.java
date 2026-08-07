package com.ecomerce.TechStore.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ChangeRoleRequest(
        @NotBlank(message = "Role must not be blank")
        @Pattern(
                regexp = "ROLE_ADMIN|ROLE_CUSTOMER",
                message = "Role must be either ROLE_ADMIN or ROLE_CUSTOMER"
        )
        String role) {
}
