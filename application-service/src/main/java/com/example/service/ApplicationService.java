package com.example.service;

import com.example.entity.ApplicationEntity;
import com.example.entity.StatusEntity;
import com.example.repository.ApplicationRepository;
import com.example.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationService {


    private final StatusRepository statusRepository;
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

        if (application.getStatus() == null) {
            StatusEntity defaultStatus = statusRepository.findById(1) // e.g., Pending
                    .orElseThrow(() -> new RuntimeException("Default status not found"));
            application.setStatus(defaultStatus);
        }

        return applicationRepository.save(application);
    }

    // Update application status
    public ApplicationEntity updateApplicationStatus(Integer id, Integer statusId) {
        ApplicationEntity application = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        StatusEntity status = statusRepository.findById(statusId)
                .orElseThrow(() -> new RuntimeException("Status not found"));

        application.setStatus(status);
         return applicationRepository.save(application);

    }



    // Delete an application
    public void deleteApplication(Integer id) {
        applicationRepository.deleteById(id);
    }
}