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
        // Find users who have all required skills
        Map<Long, Set<Long>> userIdToSkillIds = new HashMap<>();
        for (Long skillId : requiredSkillIds) {
            for (UserSkills us : userSkillsRepository.findBySkillId(skillId)) {
                userIdToSkillIds.computeIfAbsent(us.getUserId(), k -> new HashSet<>()).add(us.getSkillId());
            }
        }

        List<Long> candidateUserIds = userIdToSkillIds.entrySet().stream()
                .filter(e -> e.getValue().containsAll(new HashSet<>(requiredSkillIds)))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (candidateUserIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<SeekerMatchDto> results = new ArrayList<>();
        for (Long userId : candidateUserIds) {
            List<ExperienceDto> exps = experienceClient.getExperiencesByUser(userId);
            long totalYears = exps == null ? 0 : exps.stream().mapToLong(ExperienceDto::getDuration).sum();
            if (totalYears < minYearsExperience) {
                continue;
            }
            Optional<UserEntity> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                continue;
            }
            UserEntity user = userOpt.get();
            if (user.getRole() == null || !"SEEKER".equalsIgnoreCase(user.getRole())) {
                continue;
            }
            SeekerMatchDto dto = new SeekerMatchDto();
            dto.setUserId(user.getUserId());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            dto.setUsername(user.getUsername());
            dto.setRole(user.getRole());
            dto.setMatchingSkillIds(new ArrayList<>(userIdToSkillIds.getOrDefault(userId, Collections.emptySet())));
            dto.setTotalExperienceYears(totalYears);
            results.add(dto);
        }
        return results;
    }
}
