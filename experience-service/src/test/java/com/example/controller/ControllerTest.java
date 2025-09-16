package com.example.controller;

import com.example.ExperienceServiceApplication;
import com.example.dto.ExperienceDto;
import com.example.service.ExperienceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@SpringBootTest(classes = ExperienceServiceApplication.class)
public class ControllerTest {

    @Autowired
    private ExperienceController experienceController;

    @MockBean
    private ExperienceService experienceService;

    private ExperienceDto sampleDto;

    @BeforeEach
    void setup() {
        sampleDto = new ExperienceDto();
        sampleDto.setId(1L);
        sampleDto.setUserId(10L);
        sampleDto.setTitle("Developer");
        sampleDto.setCompany("Sony");
        sampleDto.setCurrentlyWorking(true);
    }

    @Test
    void testGetAllExperiences() {
        try {
            given(experienceService.getAllExperiencesAsDto()).willReturn(List.of(sampleDto));

            ResponseEntity<List<ExperienceDto>> response = experienceController.getAllExperiences();
            assertEquals(200, response.getStatusCodeValue());
            assertNotNull(response.getBody());
            assertEquals(1, response.getBody().size());
            assertEquals(sampleDto.getId(), response.getBody().get(0).getId());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    void testGetExperienceByIdFound() {
        try {
            given(experienceService.getExperienceByIdAsDto(1L)).willReturn(Optional.of(sampleDto));

            ResponseEntity<ExperienceDto> response = experienceController.getExperienceById(1L);
            assertEquals(200, response.getStatusCodeValue());
            assertNotNull(response.getBody());
            assertEquals(sampleDto.getTitle(), response.getBody().getTitle());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    void testGetExperienceByIdNotFound() {
        try {
            given(experienceService.getExperienceByIdAsDto(999L)).willReturn(Optional.empty());

            ResponseEntity<ExperienceDto> response = experienceController.getExperienceById(999L);
            assertEquals(404, response.getStatusCodeValue());
            assertNull(response.getBody());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    void testAddExperience() {
        try {
            given(experienceService.addExperienceFromDto(Mockito.any(ExperienceDto.class))).willReturn(sampleDto);

            ResponseEntity<ExperienceDto> response = experienceController.addExperience(sampleDto);
            assertEquals(200, response.getStatusCodeValue());
            assertNotNull(response.getBody());
            assertEquals(sampleDto.getCompany(), response.getBody().getCompany());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    void testUpdateExperienceFound() {
        try {
            given(experienceService.updateExperienceFromDto(eq(1L), Mockito.any(ExperienceDto.class))).willReturn(Optional.of(sampleDto));

            ResponseEntity<ExperienceDto> response = experienceController.updateExperience(1L, sampleDto);
            assertEquals(200, response.getStatusCodeValue());
            assertNotNull(response.getBody());
            assertEquals(sampleDto.getCompany(), response.getBody().getCompany());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    void testUpdateExperienceNotFound() {
        try {
            given(experienceService.updateExperienceFromDto(eq(999L), Mockito.any(ExperienceDto.class))).willReturn(Optional.empty());

            ResponseEntity<ExperienceDto> response = experienceController.updateExperience(999L, sampleDto);
            assertEquals(404, response.getStatusCodeValue());
            assertNull(response.getBody());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    void testDeleteExperience() {
        try {
            willDoNothing().given(experienceService).deleteExperience(1L);

            ResponseEntity<Void> response = experienceController.deleteExperience(1L);
            assertEquals(204, response.getStatusCodeValue());

            then(experienceService).should().deleteExperience(1L);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    void testGetExperiencesByUser() {
        try {
            given(experienceService.getExperiencesByUserIdAsDto(10L)).willReturn(List.of(sampleDto));

            ResponseEntity<List<ExperienceDto>> response = experienceController.getExperiencesByUser(10L);
            assertEquals(200, response.getStatusCodeValue());
            assertNotNull(response.getBody());
            assertEquals(sampleDto.getUserId(), response.getBody().get(0).getUserId());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}
