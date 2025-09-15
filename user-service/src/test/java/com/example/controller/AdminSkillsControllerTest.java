
package com.example.controller;

import com.example.entity.Category;
import com.example.entity.Skills;
import com.example.entity.UserSkills;
import com.example.service.CategoryService;
import com.example.service.SkillsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

public class AdminSkillsControllerTest {

    private CategoryService categoryService;
    private SkillsService skillsService;
    private AdminSkillsController controller;

    @BeforeEach
    void setUp() {
        categoryService = mock(CategoryService.class);
        skillsService = mock(SkillsService.class);
        controller = new AdminSkillsController(categoryService, skillsService);
    }

    // ✅ Positive Test: Create Category
    @Test
    void testCreateCategorySuccess() {
        try {
            Category category = new Category();
            category.setCategoryName("Tech");

            when(categoryService.createCategory(any())).thenReturn(category);

            ResponseEntity<Category> response = controller.createCategory(category);
            assertEquals("Tech", response.getBody().getCategoryName());
            assertEquals(200, response.getStatusCodeValue());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    // ❌ Negative Test: Create Category returns null
    @Test
    void testCreateCategoryFailure() {
        try {
            when(categoryService.createCategory(any())).thenReturn(null);

            ResponseEntity<Category> response = controller.createCategory(new Category());
            assertNull(response.getBody());
            assertEquals(200, response.getStatusCodeValue()); // Still 200, but body is null
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    // ✅ Positive Test: List Categories
    @Test
    void testListCategoriesSuccess() {
        try {
            Category category = new Category();
            category.setCategoryName("Design");

            when(categoryService.getAllCategories()).thenReturn(List.of(category));

            ResponseEntity<List<Category>> response = controller.listCategories();
            assertFalse(response.getBody().isEmpty());
            assertEquals("Design", response.getBody().get(0).getCategoryName());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    // ❌ Negative Test: List Categories returns empty
    @Test
    void testListCategoriesEmpty() {
        try {
            when(categoryService.getAllCategories()).thenReturn(Collections.emptyList());

            ResponseEntity<List<Category>> response = controller.listCategories();
            assertTrue(response.getBody().isEmpty());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    // ✅ Positive Test: Create Skill
    @Test
    void testCreateSkillSuccess() {
        try {
            Skills skill = new Skills();
            skill.setSkillName("Java");

            when(skillsService.createSkill(any())).thenReturn(skill);

            ResponseEntity<Skills> response = controller.createSkill(skill);
            assertEquals("Java", response.getBody().getSkillName());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    // ❌ Negative Test: Create Skill returns null
    @Test
    void testCreateSkillFailure() {
        try {
            when(skillsService.createSkill(any())).thenReturn(null);

            ResponseEntity<Skills> response = controller.createSkill(new Skills());
            assertNull(response.getBody());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    // ✅ Positive Test: List Skills by Category
    @Test
    void testListSkillsByCategorySuccess() {
        try {
            UserSkills userSkill = new UserSkills();
            when(skillsService.getSkillsByCategory(1L)).thenReturn(List.of(userSkill));

            ResponseEntity<List<UserSkills>> response = controller.listSkillsByCategory(1L);
            assertFalse(response.getBody().isEmpty());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    // ❌ Negative Test: List Skills by Category returns empty
    @Test
    void testListSkillsByCategoryEmpty() {
        try {
            when(skillsService.getSkillsByCategory(99L)).thenReturn(Collections.emptyList());

            ResponseEntity<List<UserSkills>> response = controller.listSkillsByCategory(99L);
            assertTrue(response.getBody().isEmpty());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}
