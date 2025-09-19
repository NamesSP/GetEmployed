package com.example.controller;

import com.example.dto.ExperienceDto;
import com.example.entity.Experience;
import com.example.service.ExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/experiences")
public class ExperienceController {
    @Autowired
    ExperienceService experienceService;


    @GetMapping
    public ResponseEntity<List<ExperienceDto>> getAllExperiences() {
        return ResponseEntity.ok(experienceService.getAllExperiencesAsDto());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExperienceDto> getExperienceById(@PathVariable Long id) {
        return experienceService.getExperienceByIdAsDto(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ExperienceDto> addExperience(@RequestBody ExperienceDto experienceDto) {
        return ResponseEntity.ok(experienceService.addExperienceFromDto(experienceDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExperienceDto> updateExperience(
            @PathVariable Long id,
            @RequestBody ExperienceDto updatedExperienceDto) {
        return experienceService.updateExperienceFromDto(id, updatedExperienceDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExperience(@PathVariable Long id) {
        experienceService.deleteExperience(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ExperienceDto>> getExperiencesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(experienceService.getExperiencesByUserIdAsDto(userId));
    }

    // Additional endpoints for internal entity-based operations if needed
    @GetMapping("/internal")
    public ResponseEntity<List<Experience>> getAllExperiencesInternal() {
        return ResponseEntity.ok(experienceService.getAllExperiences());
    }

    @GetMapping("/internal/{id}")
    public ResponseEntity<Experience> getExperienceByIdInternal(@PathVariable Long id) {
        return experienceService.getExperienceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/internal")
    public ResponseEntity<Experience> addExperienceInternal(@RequestBody Experience experience) {
        return ResponseEntity.ok(experienceService.addExperience(experience));
    }

    @PutMapping("/internal/{id}")
    public ResponseEntity<Experience> updateExperienceInternal(
            @PathVariable Long id,
            @RequestBody Experience updatedExperience) {
        return experienceService.updateExperience(id, updatedExperience)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/internal/user/{userId}")
    public ResponseEntity<List<Experience>> getExperiencesByUserInternal(@PathVariable Long userId) {
        return ResponseEntity.ok(experienceService.getExperiencesByUserId(userId));
    }

}
