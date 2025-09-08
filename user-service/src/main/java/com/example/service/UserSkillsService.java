package com.example.service;

import com.example.entity.UserSkills;
import com.example.repository.SkillsRepository;
import com.example.repository.UserRepository;
import com.example.repository.UserSkillsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserSkillsService {
    @Autowired
    private UserSkillsRepository userSkillsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SkillsRepository skillsRepository;

    public void addUserSkill(Long userId, Long skillId) {
        if (!userRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist: " + userId);
        }
        if (skillsRepository.findById(skillId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Skill does not exist: " + skillId);
        }
        if (userSkillsRepository.existsByUserIdAndSkillId(userId, skillId)) {
            return; // idempotent
        }
        UserSkills us = new UserSkills();
        us.setUserId(userId);
        us.setSkillId(skillId);
        userSkillsRepository.save(us);
    }

    public void removeUserSkill(Long userId, Long skillId) {
        userSkillsRepository.deleteByUserIdAndSkillId(userId, skillId);
    }

    public List<UserSkills> getUserSkills(Long userId) {
        return userSkillsRepository.findByUserId(userId);
    }
}
