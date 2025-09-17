package com.example.service;

import com.example.entity.Skills;
import com.example.entity.UserSkills;
import com.example.repository.SkillsRepository;
import com.example.repository.UserRepository;
import com.example.repository.UserSkillsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserSkillsServiceTest {

    @Mock
    private UserSkillsRepository userSkillsRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SkillsRepository skillsRepository;

    @InjectMocks
    private UserSkillsService userSkillsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------- addUserSkill tests -------------

    @Test
    void addUserSkill_successful() {
        Long userId = 1L;
        Long skillId = 100L;

        when(userRepository.existsById(userId)).thenReturn(true);
        when(skillsRepository.findById(skillId)).thenReturn(Optional.of(new Skills()));
        when(userSkillsRepository.existsByUserIdAndSkillId(userId, skillId)).thenReturn(false);

        userSkillsService.addUserSkill(userId, skillId);

        verify(userSkillsRepository, times(1)).save(any(UserSkills.class));
    }

    @Test
    void addUserSkill_userDoesNotExist_throwsException() {
        Long userId = 1L, skillId = 100L;

        when(userRepository.existsById(userId)).thenReturn(false);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> userSkillsService.addUserSkill(userId, skillId));

        assertEquals("400 BAD_REQUEST \"User does not exist: 1\"", ex.getMessage());
        verifyNoInteractions(skillsRepository);
        verify(userSkillsRepository, never()).save(any());
    }

    @Test
    void addUserSkill_skillDoesNotExist_throwsException() {
        Long userId = 1L, skillId = 100L;

        when(userRepository.existsById(userId)).thenReturn(true);
        when(skillsRepository.findById(skillId)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> userSkillsService.addUserSkill(userId, skillId));

        assertEquals("400 BAD_REQUEST \"Skill does not exist: 100\"", ex.getMessage());
        verify(userSkillsRepository, never()).save(any());
    }

    @Test
    void addUserSkill_alreadyExists_noSaveCalled() {
        Long userId = 1L, skillId = 100L;

        when(userRepository.existsById(userId)).thenReturn(true);
        when(skillsRepository.findById(skillId)).thenReturn(Optional.of(new Skills()));
        when(userSkillsRepository.existsByUserIdAndSkillId(userId, skillId)).thenReturn(true);

        userSkillsService.addUserSkill(userId, skillId);

        verify(userSkillsRepository, never()).save(any());
    }

    // ---------- removeUserSkill tests -------------

    @Test
    void removeUserSkill_deletesEntry() {
        Long userId = 1L, skillId = 100L;

        userSkillsService.removeUserSkill(userId, skillId);

        verify(userSkillsRepository, times(1)).deleteByUserIdAndSkillId(userId, skillId);
    }

    // ---------- getUserSkills tests -------------

    @Test
    void getUserSkills_returnsList() {
        Long userId = 1L;
        List<UserSkills> mockList = List.of(new UserSkills());

        when(userSkillsRepository.findByUserId(userId)).thenReturn(mockList);

        List<UserSkills> result = userSkillsService.getUserSkills(userId);

        assertEquals(1, result.size());
        verify(userSkillsRepository, times(1)).findByUserId(userId);
    }

    @Test
    void getUserSkills_returnsEmptyList() {
        Long userId = 1L;

        when(userSkillsRepository.findByUserId(userId)).thenReturn(Collections.emptyList());

        List<UserSkills> result = userSkillsService.getUserSkills(userId);

        assertTrue(result.isEmpty());
        verify(userSkillsRepository, times(1)).findByUserId(userId);
    }
}
