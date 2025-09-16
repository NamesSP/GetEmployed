package com.example.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SkillsTest {

    @Test
    void testGettersAndSetters() {
        Skills skills = new Skills();
        skills.setId(1L);
        skills.setSkillName("Java Programming");
        skills.setCategoryId(2L);

        assertEquals(1L, skills.getId());
        assertEquals("Java Programming", skills.getSkillName());
        assertEquals(2L, skills.getCategoryId());
    }

    @Test
    void testEqualsAndHashCode() {
        Skills s1 = new Skills();
        s1.setId(1L);
        s1.setSkillName("Python");
        s1.setCategoryId(3L);

        Skills s2 = new Skills();
        s2.setId(1L);
        s2.setSkillName("Python");
        s2.setCategoryId(3L);

        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void testToString() {
        Skills skills = new Skills();
        skills.setId(5L);
        skills.setSkillName("Data Analysis");
        skills.setCategoryId(4L);

        String str = skills.toString();
        assertTrue(str.contains("5"));
        assertTrue(str.contains("Data Analysis"));
        assertTrue(str.contains("4"));
    }

    @Test
    void testObjectCreation() {
        Skills skills = new Skills();
        assertNotNull(skills);
    }
}
