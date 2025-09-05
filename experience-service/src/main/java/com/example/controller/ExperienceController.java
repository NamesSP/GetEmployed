package com.example.controller;

import com.example.entity.Experience;
import com.example.service.ExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/experiences")
@Component
public class ExperienceController {
    @Autowired
    ExperienceService experienceService;


    @GetMapping
    public ResponseEntity<List<Experience>> getAllExperiences() {
        return ResponseEntity.ok(experienceService.getAllExperiences());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Experience> getExperienceById(@PathVariable Long id) {
        return experienceService.getExperienceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<Experience> addExperience(@RequestBody Experience experience) {
        return ResponseEntity.ok(experienceService.addExperience(experience));
    }


    @PutMapping("/{id}")
    public ResponseEntity<Experience> updateExperience(
            @PathVariable Long id,
            @RequestBody Experience updatedExperience) {
        return experienceService.updateExperience(id, updatedExperience)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExperience(@PathVariable Long id) {
        experienceService.deleteExperience(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Experience>> getExperiencesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(experienceService.getExperiencesByUserId(userId));
    }

}
