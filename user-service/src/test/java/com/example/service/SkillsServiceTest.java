package com.example.service;

import com.example.entity.Category;
import com.example.entity.Skills;
import com.example.entity.UserSkills;
import com.example.repository.CategoryRepository;
import com.example.repository.SkillsRepository;
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

class SkillsServiceTest {

    @Mock
    private SkillsRepository skillsRepository;

    @Mock
    private UserSkillsRepository userSkillsRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private SkillsService skillsService;

    private Skills skill;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        skill = new Skills();

        skill.setSkillName("Java");
        skill.setCategoryId(100L);
    }

    // ---------- createSkill tests -------------

    @Test
    void createSkill_successful() {
        Category category = new Category();
        category.setCategoryName("Programming");
        when(categoryRepository.findById(100L)).thenReturn(Optional.of(category)); // dummy category
        when(skillsRepository.existsBySkillName("Java")).thenReturn(false);
        when(skillsRepository.save(skill)).thenReturn(skill);

        Skills result = skillsService.createSkill(skill);

        assertNotNull(result);
        assertEquals("Java", result.getSkillName());
        verify(skillsRepository, times(1)).save(skill);
    }

    @Test
    void createSkill_missingName_throwsException() {
        skill.setSkillName(null);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> skillsService.createSkill(skill));

        assertEquals("400 BAD_REQUEST \"Skill name is required\"", ex.getMessage());
        verifyNoInteractions(categoryRepository);
        verifyNoInteractions(skillsRepository);
    }

    @Test
    void createSkill_blankName_throwsException() {
        skill.setSkillName("   ");

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> skillsService.createSkill(skill));

        assertEquals("400 BAD_REQUEST \"Skill name is required\"", ex.getMessage());
    }

    @Test
    void createSkill_missingCategory_throwsException() {
        skill.setCategoryId(null);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> skillsService.createSkill(skill));

        assertTrue(ex.getMessage().contains("Category does not exist"));
        verifyNoInteractions(skillsRepository);
    }

    @Test
    void createSkill_categoryNotFound_throwsException() {
        when(categoryRepository.findById(100L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> skillsService.createSkill(skill));

        assertTrue(ex.getMessage().contains("Category does not exist"));
    }

    @Test
    void createSkill_alreadyExists_throwsException() {
        Category category = new Category();
        category.setCategoryName("Programming");
        when(categoryRepository.findById(100L)).thenReturn(Optional.of(category));
        when(skillsRepository.existsBySkillName("Java")).thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> skillsService.createSkill(skill));

        assertTrue(ex.getMessage().contains("Skill already exists"));
        verify(skillsRepository, never()).save(any());
    }

    // ---------- getSkillsByCategory tests -------------

    @Test
    void getSkillsByCategory_returnsList() {
        List<UserSkills> mockList = List.of(new UserSkills());
        when(skillsRepository.findByCategoryId(100L)).thenReturn(mockList);

        List<UserSkills> result = skillsService.getSkillsByCategory(100L);

        assertEquals(1, result.size());
        verify(skillsRepository, times(1)).findByCategoryId(100L);
    }

    // ---------- getAllSkills tests -------------

    @Test
    void getAllSkills_returnsList() {
        when(skillsRepository.findAll()).thenReturn(List.of(skill));

        List<Skills> result = skillsService.getAllSkills();

        assertEquals(1, result.size());
        assertEquals("Java", result.get(0).getSkillName());
        verify(skillsRepository, times(1)).findAll();
    }
}
