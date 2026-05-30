package com.ecomerce.TechStore.product.service;

import com.ecomerce.TechStore.product.dto.ProductRequestDTO;
import com.ecomerce.TechStore.product.dto.ProductResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface ProductService {

    ProductResponseDTO createProduct(ProductRequestDTO requestDTO);

    ProductResponseDTO getProductById(Long id);

    Page<ProductResponseDTO> getAllProducts(Pageable pageable);

    Page<ProductResponseDTO> searchProducts(String name, String brand, Long categoryId,
                                            BigDecimal minPrice, BigDecimal maxPrice,
                                            Pageable pageable);

    ProductResponseDTO updateProduct(Long id, ProductRequestDTO requestDTO);

    void deleteProduct(Long id);

    ProductResponseDTO toggleStatus(Long id);
}
