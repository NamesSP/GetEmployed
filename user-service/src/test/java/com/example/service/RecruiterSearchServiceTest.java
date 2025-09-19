package com.example.service;

import com.example.client.ExperienceServiceClient;
import com.example.dto.ExperienceDto;
import com.example.dto.SeekerMatchDto;
import com.example.entity.UserEntity;
import com.example.entity.UserSkills;
import com.example.repository.UserRepository;
import com.example.repository.UserSkillsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecruiterSearchServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserSkillsRepository userSkillsRepository;

    @Mock
    private ExperienceServiceClient experienceClient;

    @InjectMocks
    private RecruiterSearchService recruiterSearchService;

    private UserEntity seekerUser;
    private UserSkills userSkill;
    private ExperienceDto experience;

    @BeforeEach
    void setUp() {
        seekerUser = new UserEntity();
        seekerUser.setUserId(1L);
        seekerUser.setFirstName("John");
        seekerUser.setLastName("Doe");
        seekerUser.setUsername("johndoe");
        seekerUser.setRole("SEEKER");

        userSkill = new UserSkills();
        userSkill.setUserId(1L);
        userSkill.setSkillId(100L);

        experience = new ExperienceDto();
        experience.setDuration(5L);
    }

    @Test
    void testFindSeekersBySkillsAndExperienceWithMatchingData() {
        List<Long> requiredSkills = Arrays.asList(100L);

        when(userSkillsRepository.findBySkillId(100L)).thenReturn(Arrays.asList(userSkill));
        when(userRepository.findById(1L)).thenReturn(Optional.of(seekerUser));
        when(experienceClient.getExperiencesByUser(1L)).thenReturn(Arrays.asList(experience));

        List<SeekerMatchDto> result = recruiterSearchService.findSeekersBySkillsAndExperience(requiredSkills, 3L);

        assertNotNull(result);
        assertEquals(1, result.size());
        SeekerMatchDto dto = result.get(0);
        assertEquals(1L, dto.getUserId());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals("johndoe", dto.getUsername());
        assertEquals("SEEKER", dto.getRole());
        assertEquals(5L, dto.getTotalExperienceYears());
        assertTrue(dto.getMatchingSkillIds().contains(100L));

        verify(userSkillsRepository, times(1)).findBySkillId(100L);
        verify(userRepository, times(1)).findById(1L);
        verify(experienceClient, times(1)).getExperiencesByUser(1L);
    }
    @Test
    void testFindSeekersBySkillsAndExperienceWithEmptySkillsReturnsEmpty() {
        List<Long> requiredSkills = Collections.emptyList();

        List<SeekerMatchDto> result = recruiterSearchService.findSeekersBySkillsAndExperience(requiredSkills, 3L);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(userSkillsRepository, never()).findBySkillId(anyLong());
        verify(userRepository, never()).findById(anyLong());
        verify(experienceClient, never()).getExperiencesByUser(anyLong());
    }

    @Test
    void testFindSeekersBySkillsAndExperienceWithNullSkillsReturnsAllSeekers() {
        List<UserEntity> allUsers = Arrays.asList(seekerUser);
        List<UserSkills> skills = Arrays.asList(userSkill);

        when(userRepository.findAll()).thenReturn(allUsers);
        when(experienceClient.getExperiencesByUser(1L)).thenReturn(Arrays.asList(experience));
        when(userSkillsRepository.findByUserId(1L)).thenReturn(skills);

        List<SeekerMatchDto> result = recruiterSearchService.findSeekersBySkillsAndExperience(null, 3L);

        assertNotNull(result);
        assertEquals(1, result.size());
        SeekerMatchDto dto = result.get(0);
        assertEquals(1L, dto.getUserId());

        verify(userRepository, times(1)).findAll();
        verify(experienceClient, times(1)).getExperiencesByUser(1L);
        verify(userSkillsRepository, times(1)).findByUserId(1L);
    }

    @Test
    void testFindSeekersBySkillsAndExperienceWithInsufficientExperience() {
        List<Long> requiredSkills = Arrays.asList(100L);

        when(userSkillsRepository.findBySkillId(100L)).thenReturn(Arrays.asList(userSkill));
        when(userRepository.findById(1L)).thenReturn(Optional.of(seekerUser));
        when(experienceClient.getExperiencesByUser(1L)).thenReturn(Arrays.asList(experience));

        List<SeekerMatchDto> result = recruiterSearchService.findSeekersBySkillsAndExperience(requiredSkills, 10L);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(userSkillsRepository, times(1)).findBySkillId(100L);
        verify(userRepository, times(1)).findById(1L);
        verify(experienceClient, times(1)).getExperiencesByUser(1L);
    }

    @Test
    void testFindSeekersBySkillsAndExperienceWithNonSeekerUser() {
        seekerUser.setRole("EMPLOYER");

        List<Long> requiredSkills = Arrays.asList(100L);

        when(userSkillsRepository.findBySkillId(100L)).thenReturn(Arrays.asList(userSkill));
        when(userRepository.findById(1L)).thenReturn(Optional.of(seekerUser));

        List<SeekerMatchDto> result = recruiterSearchService.findSeekersBySkillsAndExperience(requiredSkills, 3L);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(userSkillsRepository, times(1)).findBySkillId(100L);
        verify(userRepository, times(1)).findById(1L);
        verify(experienceClient, never()).getExperiencesByUser(anyLong());
    }
}