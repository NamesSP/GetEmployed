package com.example.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserSkillsTest {

    @Test
    void testGettersAndSetters() {
        UserSkills userSkills = new UserSkills();
        userSkills.setUserId(1L);
        userSkills.setSkillId(2L);

        assertEquals(1L, userSkills.getUserId());
        assertEquals(2L, userSkills.getSkillId());
    }

    @Test
    void testEqualsAndHashCode_UserSkills() {
        UserSkills us1 = new UserSkills();
        us1.setUserId(1L);
        us1.setSkillId(2L);

        UserSkills us2 = new UserSkills();
        us2.setUserId(1L);
        us2.setSkillId(2L);

        assertEquals(us1, us2);
        assertEquals(us1.hashCode(), us2.hashCode());
    }

    @Test
    void testEqualsAndHashCode_UserSkillsId() {
        UserSkills.UserSkillsId id1 = new UserSkills.UserSkillsId(1L, 2L);
        UserSkills.UserSkillsId id2 = new UserSkills.UserSkillsId(1L, 2L);

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void testToString() {
        UserSkills userSkills = new UserSkills();
        userSkills.setUserId(3L);
        userSkills.setSkillId(4L);

        String str = userSkills.toString();
        assertTrue(str.contains("3"));
        assertTrue(str.contains("4"));
    }

    @Test
    void testObjectCreation() {
        UserSkills userSkills = new UserSkills();
        assertNotNull(userSkills);

        UserSkills.UserSkillsId id = new UserSkills.UserSkillsId(1L, 2L);
        assertNotNull(id);
    }
}
