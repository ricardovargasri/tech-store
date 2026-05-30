package com.ecomerce.TechStore.category.service.impl;

import com.ecomerce.TechStore.category.dto.CategoryRequestDTO;
import com.ecomerce.TechStore.category.dto.CategoryResponseDTO;
import com.ecomerce.TechStore.category.entity.Category;
import com.ecomerce.TechStore.category.mapper.CategoryMapper;
import com.ecomerce.TechStore.category.repository.CategoryRepository;
import com.ecomerce.TechStore.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO) {
        Category category = categoryMapper.toEntity(requestDTO);
        if (requestDTO.getParentId() != null) {
            Category parent = categoryRepository.findById(requestDTO.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found with id: " + requestDTO.getParentId()));
            category.setParentCategory(parent);
        }
        return categoryMapper.toResponseDTO(categoryRepository.save(category));
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        return categoryMapper.toResponseDTO(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> getRootCategories() {
        return categoryRepository.findByParentCategoryIsNull().stream()
                .map(categoryMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO requestDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        category.setName(requestDTO.getName());
        if (requestDTO.getParentId() != null) {
            Category parent = categoryRepository.findById(requestDTO.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found with id: " + requestDTO.getParentId()));
            category.setParentCategory(parent);
        } else {
            category.setParentCategory(null);
        }
        return categoryMapper.toResponseDTO(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }
}
