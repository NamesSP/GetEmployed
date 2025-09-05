package com.example.service;

import com.example.entity.ApplicationEntity;
import com.example.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;

    // Get all applications
    public List<ApplicationEntity> getAllApplications() {
        return applicationRepository.findAll();
    }

    // Get application by ID
    public Optional<ApplicationEntity> getApplicationById(Integer id) {
        return applicationRepository.findById(id);
    }

    // Get applications by user ID
    public List<ApplicationEntity> getApplicationsByUserId(Long userId) {
        return applicationRepository.findByUserId(userId);
    }

    // Get applications by job ID
    public List<ApplicationEntity> getApplicationsByJobId(Long jobId) {
        return applicationRepository.findByJobId(jobId);
    }

    // Create a new application
    public ApplicationEntity createApplication(ApplicationEntity application) {
        application.setAppliedAt(LocalDateTime.now());
        return applicationRepository.save(application);
    }

    // Update application status
    public ApplicationEntity updateApplicationStatus(Integer id, String status) {
        ApplicationEntity application = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        application.setStatus(status);
        return applicationRepository.save(application);
    }

    // Delete an application
    public void deleteApplication(Integer id) {
        applicationRepository.deleteById(id);
    }
}