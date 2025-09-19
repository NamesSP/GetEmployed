package com.example.service;

import com.example.entity.Category;
import com.example.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setCategoryName("Electronics");
    }

    @Test
    void testCreateCategorySuccessfully() {
        when(categoryRepository.existsByCategoryName(category.getCategoryName())).thenReturn(false);
        when(categoryRepository.save(category)).thenReturn(category);

        Category result = categoryService.createCategory(category);

        assertEquals("Electronics", result.getCategoryName());
        verify(categoryRepository, times(1)).existsByCategoryName(category.getCategoryName());
        verify(categoryRepository, times(1)).save(category);
    }
    @Test
    void testCreateCategoryWithoutNameThrowsException() {
        category.setCategoryName("   ");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            categoryService.createCategory(category);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Category name is required", exception.getReason());
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void testCreateCategoryWithDuplicateNameThrowsException() {
        when(categoryRepository.existsByCategoryName(category.getCategoryName())).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            categoryService.createCategory(category);
        });

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Category already exists"));
        verify(categoryRepository, times(1)).existsByCategoryName(category.getCategoryName());
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void testGetAllCategories() {
        Category another = new Category();
        another.setId(2L);
        another.setCategoryName("Books");

        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category, another));

        List<Category> result = categoryService.getAllCategories();

        assertEquals(2, result.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testGetByIdFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Optional<Category> result = categoryService.getById(1L);

        assertTrue(result.isPresent());
        assertEquals("Electronics", result.get().getCategoryName());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void testGetByIdNotFound() {
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Category> result = categoryService.getById(999L);

        assertTrue(result.isEmpty());
        verify(categoryRepository, times(1)).findById(999L);
    }
}

