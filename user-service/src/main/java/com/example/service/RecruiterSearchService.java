package com.example.service;

import com.example.client.ExperienceServiceClient;
import com.example.dto.ExperienceDto;
import com.example.dto.SeekerMatchDto;
import com.example.entity.UserEntity;
import com.example.entity.UserSkills;
import com.example.repository.UserRepository;
import com.example.repository.UserSkillsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecruiterSearchService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserSkillsRepository userSkillsRepository;
    @Autowired
    private ExperienceServiceClient experienceClient;

    public List<SeekerMatchDto> findSeekersBySkillsAndExperience(List<Long> requiredSkillIds, long minYearsExperience) {
        System.out.println(
                "Starting search with skillIds: " + requiredSkillIds + ", minExperience: " + minYearsExperience);

        // If no skills are required (null), return all SEEKER users
        if (requiredSkillIds == null) {
            System.out.println("No skills specified (null), returning all seekers");
            return getAllSeekersWithExperience(minYearsExperience);
        }

        // If empty skill list is provided, return empty results (no users match empty
        // skill requirements)
        if (requiredSkillIds.isEmpty()) {
            System.out.println("Empty skill list provided, returning empty results");
            return Collections.emptyList();
        }

        // Find users who have all required skills
        Map<Long, Set<Long>> userIdToSkillIds = new HashMap<>();

        // For each required skill, find all users who have that skill
        for (Long skillId : requiredSkillIds) {
            List<UserSkills> usersWithSkill = userSkillsRepository.findBySkillId(skillId);
            for (UserSkills us : usersWithSkill) {
                userIdToSkillIds.computeIfAbsent(us.getUserId(), k -> new HashSet<>()).add(us.getSkillId());
            }
        }

        // Get all users who have at least one matching skill, sorted by number of
        // matching skills
        List<Map.Entry<Long, Set<Long>>> usersWithSkills = userIdToSkillIds.entrySet().stream()
                .filter(e -> !e.getValue().isEmpty()) // Only users with at least one matching skill
                .sorted((e1, e2) -> {
                    // Sort by number of matching skills (descending) - more matching skills first
                    int size1 = e1.getValue().size();
                    int size2 = e2.getValue().size();
                    return Integer.compare(size2, size1);
                })
                .collect(Collectors.toList());

        System.out.println("Found " + usersWithSkills.size() + " users with at least one matching skill");

        if (usersWithSkills.isEmpty()) {
            System.out.println("No users found with any matching skills");
            return Collections.emptyList();
        }

        // Process users and create DTOs with experience data
        List<SeekerMatchDto> candidateResults = new ArrayList<>();
        for (Map.Entry<Long, Set<Long>> userEntry : usersWithSkills) {
            Long userId = userEntry.getKey();
            Set<Long> matchingSkills = userEntry.getValue();

            // Get user details
            Optional<UserEntity> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                continue;
            }
            UserEntity user = userOpt.get();
            if (user.getRole() == null || !"SEEKER".equalsIgnoreCase(user.getRole())) {
                continue;
            }

            // Get experience data with error handling
            long totalYears = 0;
            try {
                List<ExperienceDto> exps = experienceClient.getExperiencesByUser(userId);
                totalYears = exps == null ? 0 : exps.stream().mapToLong(ExperienceDto::getDuration).sum();
            } catch (Exception e) {
                // Log the error but continue processing
                System.err.println("Error fetching experience for user " + userId + ": " + e.getMessage());
                totalYears = 0;
            }

            // Check experience requirement
            if (totalYears < minYearsExperience) {
                continue;
            }

            // Create result DTO
            SeekerMatchDto dto = new SeekerMatchDto();
            dto.setUserId(user.getUserId());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            dto.setUsername(user.getUsername());
            dto.setRole(user.getRole());
            dto.setMatchingSkillIds(new ArrayList<>(matchingSkills));
            dto.setTotalExperienceYears(totalYears);
            candidateResults.add(dto);
        }

        // Sort by number of matching skills (descending), then by experience
        // (descending)
        candidateResults.sort((dto1, dto2) -> {
            // First sort by number of matching skills (descending)
            int skillComparison = Integer.compare(dto2.getMatchingSkillIds().size(), dto1.getMatchingSkillIds().size());
            if (skillComparison != 0) {
                return skillComparison;
            }
            // If same number of skills, sort by experience (descending)
            return Long.compare(dto2.getTotalExperienceYears(), dto1.getTotalExperienceYears());
        });

        System.out.println("Returning " + candidateResults.size() + " users sorted by skills and experience");
        return candidateResults;
    }

    private List<SeekerMatchDto> getAllSeekersWithExperience(long minYearsExperience) {
        List<UserEntity> allUsers = userRepository.findAll();
        List<SeekerMatchDto> results = new ArrayList<>();

        for (UserEntity user : allUsers) {
            if (user.getRole() == null || !"SEEKER".equalsIgnoreCase(user.getRole())) {
                continue;
            }

            // Get experience data with error handling
            long totalYears = 0;
            try {
                List<ExperienceDto> exps = experienceClient.getExperiencesByUser(user.getUserId());
                totalYears = exps == null ? 0 : exps.stream().mapToLong(ExperienceDto::getDuration).sum();
            } catch (Exception e) {
                // Log the error but continue processing
                System.err.println("Error fetching experience for user " + user.getUserId() + ": " + e.getMessage());
                totalYears = 0;
            }

            // Check experience requirement
            if (totalYears < minYearsExperience) {
                continue;
            }

            // Get user skills
            List<UserSkills> userSkills = userSkillsRepository.findByUserId(user.getUserId());
            List<Long> skillIds = userSkills.stream()
                    .map(UserSkills::getSkillId)
                    .collect(Collectors.toList());

            // Create result DTO
            SeekerMatchDto dto = new SeekerMatchDto();
            dto.setUserId(user.getUserId());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            dto.setUsername(user.getUsername());
            dto.setRole(user.getRole());
            dto.setMatchingSkillIds(skillIds);
            dto.setTotalExperienceYears(totalYears);
            results.add(dto);
        }
        return results;
    }
}
