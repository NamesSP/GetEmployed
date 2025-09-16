package com.example.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void testGettersAndSetters() {
        Category category = new Category();
        category.setId(1L);
        category.setCategoryName("Technology");

        assertEquals(1L, category.getId());
        assertEquals("Technology", category.getCategoryName());
    }

    @Test
    void testEqualsAndHashCode() {
        Category c1 = new Category();
        c1.setId(1L);
        c1.setCategoryName("Tech");

        Category c2 = new Category();
        c2.setId(1L);
        c2.setCategoryName("Tech");

        assertEquals(c1, c2); // Lombok-generated equals
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testToString() {
        Category category = new Category();
        category.setId(10L);
        category.setCategoryName("Science");

        String str = category.toString();
        assertTrue(str.contains("10"));
        assertTrue(str.contains("Science"));
    }

    @Test
    void testObjectCreation() {
        Category category = new Category();
        assertNotNull(category); // Object should be created successfully
    }
}
