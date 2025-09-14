package com.example.service;

import com.example.client.UserServiceClient;
import com.example.dto.ExperienceDto;
import com.example.entity.Experience;
import com.example.repository.ExperienceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        if (experience.getUserId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID is required");
        }
        
        try {
            Boolean userExists = userServiceClient.userExists(experience.getUserId());
            if (Boolean.FALSE.equals(userExists)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist for userId " + experience.getUserId());
            }
        } catch (Exception e) {
            // Log the error and throw a more user-friendly exception
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Unable to verify user existence. Please try again later.");
        }
        
        return experienceRepository.save(experience);
    }

    // 🔹 4. Update experience
    public Optional<Experience> updateExperience(Long id, Experience updatedExperience) {
        return experienceRepository.findById(id).map(existing -> {
            if (updatedExperience.getUserId() != null) {
                try {
                    Boolean userExists = userServiceClient.userExists(updatedExperience.getUserId());
                    if (Boolean.FALSE.equals(userExists)) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist for userId " + updatedExperience.getUserId());
                    }
                } catch (Exception e) {
                    throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Unable to verify user existence. Please try again later.");
                }
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

    // DTO Conversion Methods
    public ExperienceDto convertToDto(Experience experience) {
        if (experience == null) return null;
        
        ExperienceDto dto = new ExperienceDto();
        dto.setId(experience.getId());
        dto.setUserId(experience.getUserId());
        dto.setTitle(experience.getTitle());
        dto.setCompany(experience.getCompany());
        dto.setStartDate(experience.getStartDate());
        dto.setEndDate(experience.getEndDate());
        dto.setDuration(experience.getDuration());
        dto.setCurrentlyWorking(experience.isCurrentlyWorking());
        dto.setDescription(experience.getDescription());
        return dto;
    }

    public Experience convertToEntity(ExperienceDto dto) {
        if (dto == null) return null;
        
        return Experience.builder()
                .id(dto.getId())
                .userId(dto.getUserId())
                .title(dto.getTitle())
                .company(dto.getCompany())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .duration(dto.getDuration())
                .currentlyWorking(dto.isCurrentlyWorking())
                .description(dto.getDescription())
                .build();
    }

    // DTO-based service methods for Feign client compatibility
    public List<ExperienceDto> getAllExperiencesAsDto() {
        return experienceRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<ExperienceDto> getExperienceByIdAsDto(Long id) {
        return experienceRepository.findById(id)
                .map(this::convertToDto);
    }

    public ExperienceDto addExperienceFromDto(ExperienceDto experienceDto) {
        Experience experience = convertToEntity(experienceDto);
        if (experience.getUserId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID is required");
        }
        
        try {
            Boolean userExists = userServiceClient.userExists(experience.getUserId());
            if (Boolean.FALSE.equals(userExists)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist for userId " + experience.getUserId());
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Unable to verify user existence. Please try again later.");
        }
        
        Experience savedExperience = experienceRepository.save(experience);
        return convertToDto(savedExperience);
    }

    public Optional<ExperienceDto> updateExperienceFromDto(Long id, ExperienceDto updatedExperienceDto) {
        Experience updatedExperience = convertToEntity(updatedExperienceDto);
        return experienceRepository.findById(id).map(existing -> {
            if (updatedExperience.getUserId() != null) {
                try {
                    Boolean userExists = userServiceClient.userExists(updatedExperience.getUserId());
                    if (Boolean.FALSE.equals(userExists)) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist for userId " + updatedExperience.getUserId());
                    }
                } catch (Exception e) {
                    throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Unable to verify user existence. Please try again later.");
                }
            }
            existing.setTitle(updatedExperience.getTitle());
            existing.setCompany(updatedExperience.getCompany());
            existing.setStartDate(updatedExperience.getStartDate());
            existing.setEndDate(updatedExperience.getEndDate());
            existing.setCurrentlyWorking(updatedExperience.isCurrentlyWorking());
            existing.setDescription(updatedExperience.getDescription());
            Experience savedExperience = experienceRepository.save(existing);
            return convertToDto(savedExperience);
        });
    }

    public List<ExperienceDto> getExperiencesByUserIdAsDto(Long userId) {
        return experienceRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
