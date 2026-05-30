package com.ecomerce.TechStore.product.service.impl;

import com.ecomerce.TechStore.category.entity.Category;
import com.ecomerce.TechStore.category.repository.CategoryRepository;
import com.ecomerce.TechStore.product.dto.ProductRequestDTO;
import com.ecomerce.TechStore.product.dto.ProductResponseDTO;
import com.ecomerce.TechStore.product.entity.Product;
import com.ecomerce.TechStore.product.enums.ProductStatus;
import com.ecomerce.TechStore.product.mapper.ProductMapper;
import com.ecomerce.TechStore.product.repository.ProductRepository;
import com.ecomerce.TechStore.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO requestDTO) {
        Category category = categoryRepository.findById(requestDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + requestDTO.getCategoryId()));
        Product product = productMapper.toEntity(requestDTO);
        product.setCategory(category);
        product.setStatus(ProductStatus.ACTIVE);
        return productMapper.toResponseDTO(productRepository.save(product));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return productMapper.toResponseDTO(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> getAllProducts(Pageable pageable) {
        return productRepository.findByStatus(ProductStatus.ACTIVE, pageable)
                .map(productMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDTO> searchProducts(String name, String brand, Long categoryId,
                                                   BigDecimal minPrice, BigDecimal maxPrice,
                                                   Pageable pageable) {
        return productRepository.findByFilters(name, brand, categoryId, minPrice, maxPrice, pageable)
                .map(productMapper::toResponseDTO);
    }

    @Override
    @Transactional
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO requestDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        Category category = categoryRepository.findById(requestDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + requestDTO.getCategoryId()));
        product.setName(requestDTO.getName());
        product.setDescription(requestDTO.getDescription());
        product.setPrice(requestDTO.getPrice());
        product.setStock(requestDTO.getStock());
        product.setBrand(requestDTO.getBrand());
        product.setCategory(category);
        return productMapper.toResponseDTO(productRepository.save(product));
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        // Logical delete
        product.setStatus(ProductStatus.DELETED);
        productRepository.save(product);
    }

    @Override
    @Transactional
    public ProductResponseDTO toggleStatus(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        if (product.getStatus() == ProductStatus.ACTIVE) {
            product.setStatus(ProductStatus.INACTIVE);
        } else if (product.getStatus() == ProductStatus.INACTIVE) {
            product.setStatus(ProductStatus.ACTIVE);
        }
        return productMapper.toResponseDTO(productRepository.save(product));
    }
}
