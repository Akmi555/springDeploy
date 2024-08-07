package org.blb.service.rent.categoryService;

import org.blb.DTO.rent.categoryDto.CategoryResponseDto;
import org.blb.models.rent.Category;
import org.blb.repository.rent.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class FindCategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private FindCategoryService findCategoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        Category category1 = new Category();
        category1.setName("Category1");

        Category category2 = new Category();
        category2.setName("Category2");

        List<Category> categories = Arrays.asList(category1, category2);
        when(categoryRepository.findAll()).thenReturn(categories);

        List<CategoryResponseDto> result = findCategoryService.findAll();

        assertEquals(2, result.size());
        assertEquals("Category2", result.get(0).getName());
        assertEquals("Category1", result.get(1).getName());

        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testFindByName_existingCategory() {
        String categoryName = "ExistingCategory";
        Category category = new Category();
        category.setName(categoryName);
        when(categoryRepository.findByName(categoryName)).thenReturn(Optional.of(category));

        Optional<Category> result = findCategoryService.findByName(categoryName);

        assertTrue(result.isPresent());
        assertEquals(categoryName, result.get().getName());

        verify(categoryRepository, times(1)).findByName(categoryName);
    }

    @Test
    void testFindByName_nonExistingCategory() {
        String categoryName = "NonExistingCategory";
        when(categoryRepository.findByName(categoryName)).thenReturn(Optional.empty());

        Optional<Category> result = findCategoryService.findByName(categoryName);

        assertTrue(result.isEmpty());

        verify(categoryRepository, times(1)).findByName(categoryName);
    }
}