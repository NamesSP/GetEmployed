package com.example.controller;

import com.example.entity.UserSkills;
import com.example.service.UserSkillsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserSkillControllerTest {

    private UserSkillsService userSkillsService;
    private UserSkillsController userSkillsController;

    @BeforeEach
    void setUp() {
        userSkillsService = mock(UserSkillsService.class);
        userSkillsController = new UserSkillsController(userSkillsService);
    }

    // ✅ Positive Test: Adding a skill returns 204 No Content
    @Test
    void testAddSkillSuccess() {
        try {
            Long userId = 1L;
            Long skillId = 2L;

            doNothing().when(userSkillsService).addUserSkill(userId, skillId);

            ResponseEntity<Void> response = userSkillsController.addSkill(userId, skillId);

            assertEquals(204, response.getStatusCodeValue());
            verify(userSkillsService, times(1)).addUserSkill(userId, skillId);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    // ✅ Positive Test: Removing a skill returns 204 No Content
    @Test
    void testRemoveSkillSuccess() {
        try {
            Long userId = 1L;
            Long skillId = 2L;

            doNothing().when(userSkillsService).removeUserSkill(userId, skillId);

            ResponseEntity<Void> response = userSkillsController.removeSkill(userId, skillId);

            assertEquals(204, response.getStatusCodeValue());
            verify(userSkillsService, times(1)).removeUserSkill(userId, skillId);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    // ✅ Positive Test: Listing user skills returns a non-empty list
    @Test
    void testListUserSkillsSuccess() {
        try {
            Long userId = 1L;

            UserSkills userSkill = new UserSkills();
            userSkill.setUserId(userId);
            userSkill.setSkillId(10L);

            when(userSkillsService.getUserSkills(userId)).thenReturn(Arrays.asList(userSkill));

            ResponseEntity<List<UserSkills>> response = userSkillsController.listUserSkills(userId);

            assertEquals(200, response.getStatusCodeValue());
            assertNotNull(response.getBody());
            assertFalse(response.getBody().isEmpty());
            assertEquals(10L, response.getBody().get(0).getSkillId());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    // ❌ Negative Test: Listing user skills returns empty list
    @Test
    void testListUserSkillsEmpty() {
        try {
            Long userId = 1L;

            when(userSkillsService.getUserSkills(userId)).thenReturn(Collections.emptyList());

            ResponseEntity<List<UserSkills>> response = userSkillsController.listUserSkills(userId);

            assertEquals(200, response.getStatusCodeValue());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().isEmpty());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    // ❌ Negative Test: Service throws exception when listing user skills
//    @Test
//    void testListUserSkillsThrowsException() {
//        try {
//            Long userId = 1L;
//
//            when(userSkillsService.getUserSkills(userId)).thenThrow(new RuntimeException("Database error"));
//
//            ResponseEntity<List<UserSkills>> response = userSkillsController.listUserSkills(userId);
//
//            assertEquals(500, response.getStatusCodeValue());
//            assertNull(response.getBody());
//        } catch (Exception e) {
//            fail("Unexpected exception: " + e.getMessage());
//        }
//    }
}
