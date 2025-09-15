package com.example.entity;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class EntityTest {

    @Test
    void testCategoryEntity() {
        Category category = new Category();
        category.setId(1L);
        category.setCategoryName("Software");

        assertEquals(1L, category.getId());
        assertEquals("Software", category.getCategoryName());
    }

    @Test
    void testSkillsEntity() {
        Skills skill = new Skills();
        skill.setId(10L);
        skill.setSkillName("Java");
        skill.setCategoryId(1L);

        assertEquals(10L, skill.getId());
        assertEquals("Java", skill.getSkillName());
        assertEquals(1L, skill.getCategoryId());
    }

    @Test
    void testUserEntity() {
        UserEntity user = new UserEntity();
        user.setUserId(100L);
        user.setAuthId(200L);
        user.setFirstName("Alice");
        user.setLastName("Smith");
        user.setEmail("alice@example.com");
        user.setUsername("alice123");
        user.setRole("USER");

        assertEquals(100L, user.getUserId());
        assertEquals(200L, user.getAuthId());
        assertEquals("Alice", user.getFirstName());
        assertEquals("Smith", user.getLastName());
        assertEquals("alice@example.com", user.getEmail());
        assertEquals("alice123", user.getUsername());
        assertEquals("USER", user.getRole());
    }

    @Test
    void testUserSkillsEntity() {
        UserSkills userSkills = new UserSkills();
        userSkills.setUserId(100L);
        userSkills.setSkillId(10L);

        assertEquals(100L, userSkills.getUserId());
        assertEquals(10L, userSkills.getSkillId());
    }

    @Test
    void testUserSkillsIdEquality() {
        UserSkills.UserSkillsId id1 = new UserSkills.UserSkillsId(1L, 2L);
        UserSkills.UserSkillsId id2 = new UserSkills.UserSkillsId(1L, 2L);
        UserSkills.UserSkillsId id3 = new UserSkills.UserSkillsId(1L, 3L);

        // Equality
        assertEquals(id1, id2);
        assertNotEquals(id1, id3);

        // HashSet behavior
        Set<UserSkills.UserSkillsId> set = new HashSet<>();
        set.add(id1);
        set.add(id2); // duplicate
        set.add(id3); // different

        assertEquals(2, set.size()); // Only two unique entries
    }
}
