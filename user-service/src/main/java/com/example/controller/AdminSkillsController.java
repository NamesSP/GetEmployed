package com.example.controller;

import com.example.entity.Category;
import com.example.entity.Skills;
import com.example.entity.UserSkills;
import com.example.service.CategoryService;
import com.example.service.SkillsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/skills")
public class AdminSkillsController {

    private final CategoryService categoryService;
    private final SkillsService skillsService;

    public AdminSkillsController(CategoryService categoryService, SkillsService skillsService) {
        this.categoryService = categoryService;
        this.skillsService = skillsService;
    }

    @PostMapping("/categories")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        return ResponseEntity.ok(categoryService.createCategory(category));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> listCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PostMapping
    public ResponseEntity<Skills> createSkill(@RequestBody Skills skill) {
        return ResponseEntity.ok(skillsService.createSkill(skill));
    }

    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<List<UserSkills>> listSkillsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(skillsService.getSkillsByCategory(categoryId));
    }
}
