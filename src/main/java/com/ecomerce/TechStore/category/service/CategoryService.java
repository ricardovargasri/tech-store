package com.ecomerce.TechStore.category.service;

import com.ecomerce.TechStore.category.dto.CategoryRequestDTO;
import com.ecomerce.TechStore.category.dto.CategoryResponseDTO;

import java.util.List;

public interface CategoryService {

    CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO);

    CategoryResponseDTO getCategoryById(Long id);

    List<CategoryResponseDTO> getAllCategories();

    List<CategoryResponseDTO> getRootCategories();

    CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO requestDTO);

    void deleteCategory(Long id);
}
