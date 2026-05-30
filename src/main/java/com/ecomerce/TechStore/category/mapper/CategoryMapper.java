package com.ecomerce.TechStore.category.mapper;

import com.ecomerce.TechStore.category.dto.CategoryRequestDTO;
import com.ecomerce.TechStore.category.dto.CategoryResponseDTO;
import com.ecomerce.TechStore.category.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parentCategory", ignore = true)
    @Mapping(target = "subCategories", ignore = true)
    Category toEntity(CategoryRequestDTO requestDTO);

    @Mapping(target = "parentId", source = "parentCategory.id")
    @Mapping(target = "subCategories", source = "subCategories")
    CategoryResponseDTO toResponseDTO(Category category);
}
