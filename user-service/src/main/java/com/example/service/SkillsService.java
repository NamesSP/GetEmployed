package com.example.service;

import com.example.entity.Skills;
import com.example.repository.CategoryRepository;
import com.example.repository.SkillsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SkillsService {
    @Autowired
    private SkillsRepository skillsRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public Skills createSkill(Skills skill) {
        if (skill.getSkillName() == null || skill.getSkillName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Skill name is required");
        }
        if (skill.getCategoryId() == null || categoryRepository.findById(skill.getCategoryId()).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Category does not exist: " + skill.getCategoryId());
        }
        if (skillsRepository.existsBySkillName(skill.getSkillName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Skill already exists: " + skill.getSkillName());
        }
        return skillsRepository.save(skill);
    }

    public List<Skills> getSkillsByCategory(Long categoryId) {
        return skillsRepository.findByCategoryId(categoryId);
    }

    public List<Skills> getAllSkills() {
        return skillsRepository.findAll();
    }
}
