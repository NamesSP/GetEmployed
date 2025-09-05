package com.example.controller;

import com.example.entity.ApplicationEntity;
import com.example.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;

    // Get all applications
    @GetMapping("/allapplications")
    public List<ApplicationEntity> getAllApplications() {
        return applicationService.getAllApplications();
    }

    // Get application by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationEntity> getApplicationById(@PathVariable Integer id) {
        Optional<ApplicationEntity> application = applicationService.getApplicationById(id);
        return application.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get applications by user ID
    @GetMapping("/user/{userId}")
    public List<ApplicationEntity> getApplicationsByUserId(@PathVariable Long userId) {
        return applicationService.getApplicationsByUserId(userId);
    }

    // Get applications by job ID
    @GetMapping("/job/{jobId}")
    public List<ApplicationEntity> getApplicationsByJobId(@PathVariable Long jobId) {
        return applicationService.getApplicationsByJobId(jobId);
    }

    // Create a new application
    @PostMapping
    public ApplicationEntity createApplication(@RequestBody ApplicationEntity application) {
        return applicationService.createApplication(application);
    }

    // Update application status
    @PutMapping("/{id}/status")
    public ResponseEntity<ApplicationEntity> updateApplicationStatus(
            @PathVariable Integer id,
            @RequestParam String status) {
        try {
            ApplicationEntity updatedApplication = applicationService.updateApplicationStatus(id, status);
            return ResponseEntity.ok(updatedApplication);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete an application
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Integer id) {
        applicationService.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }
}