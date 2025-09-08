package com.example.service;

import com.example.client.UserServiceClient;
import com.example.entity.Experience;
import com.example.repository.ExperienceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ExperienceService {
    @Autowired
    ExperienceRepository experienceRepository;

    @Autowired
    UserServiceClient userServiceClient;

    // 🔹 1. Get all experiences
    public List<Experience> getAllExperiences() {
        return experienceRepository.findAll();
    }

    // 🔹 2. Get experience by ID
    public Optional<Experience> getExperienceById(Long id) {
        return experienceRepository.findById(id);
    }

    // 🔹 3. Add experience
    public Experience addExperience(Experience experience) {
        if (experience.getUserId() == null || Boolean.FALSE.equals(userServiceClient.userExists(experience.getUserId()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist for userId " + experience.getUserId());
        }
        return experienceRepository.save(experience);
    }

    // 🔹 4. Update experience
    public Optional<Experience> updateExperience(Long id, Experience updatedExperience) {
        return experienceRepository.findById(id).map(existing -> {
            if (updatedExperience.getUserId() != null && Boolean.FALSE.equals(userServiceClient.userExists(updatedExperience.getUserId()))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist for userId " + updatedExperience.getUserId());
            }
            existing.setTitle(updatedExperience.getTitle());
            existing.setCompany(updatedExperience.getCompany());
            existing.setStartDate(updatedExperience.getStartDate());
            existing.setEndDate(updatedExperience.getEndDate());
            existing.setCurrentlyWorking(updatedExperience.isCurrentlyWorking());
            existing.setDescription(updatedExperience.getDescription());
            return experienceRepository.save(existing);
        });
    }

    // 🔹 5. Delete experience
    public void deleteExperience(Long id) {
        experienceRepository.deleteById(id);
    }

    // 🔹 6. Get experiences by userId
    public List<Experience> getExperiencesByUserId(Long userId) {
        return experienceRepository.findByUserId(userId);
    }
}
