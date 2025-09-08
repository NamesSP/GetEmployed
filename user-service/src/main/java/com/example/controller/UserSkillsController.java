package com.example.controller;

import com.example.entity.UserSkills;
import com.example.service.UserSkillsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/skills")
public class UserSkillsController {

    private final UserSkillsService userSkillsService;

    public UserSkillsController(UserSkillsService userSkillsService) {
        this.userSkillsService = userSkillsService;
    }

    @PostMapping("/{userId}/{skillId}")
    public ResponseEntity<Void> addSkill(@PathVariable Long userId, @PathVariable Long skillId) {
        userSkillsService.addUserSkill(userId, skillId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}/{skillId}")
    public ResponseEntity<Void> removeSkill(@PathVariable Long userId, @PathVariable Long skillId) {
        userSkillsService.removeUserSkill(userId, skillId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<UserSkills>> listUserSkills(@PathVariable Long userId) {
        return ResponseEntity.ok(userSkillsService.getUserSkills(userId));
    }
}
