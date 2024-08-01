package org.blb.service.rent.categoryService;

import lombok.AllArgsConstructor;
import org.blb.DTO.rent.categoryDto.CategoryResponseDto;
import org.blb.models.rent.Category;
import org.blb.repository.rent.CategoryRepository;
import org.springframework.stereotype.Service;


import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FindCategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryResponseDto> findAll() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryResponseDto> dtoList = categories.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        Collections.reverse(dtoList);
        return dtoList;
    }

    private CategoryResponseDto toDto(Category category) {
        return new CategoryResponseDto(category.getName());
    }

    public Optional<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }
}
