package com.example.controller;


import com.example.entity.Recruiter;
import com.example.service.CompanyService;
import com.example.service.RecruiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/recruiter")
public class RecruiterController {
    @Autowired
    RecruiterService recruiterService;

    @GetMapping("/allrecruiter")
    public ResponseEntity<List<Recruiter>> getAllRecruiters() {
        return ResponseEntity.ok(recruiterService.getAllRecruiters());
    }

    @PostMapping("/recruiter")
    public ResponseEntity<Recruiter> addRecruiter(@RequestBody Recruiter recruiter) {
        return ResponseEntity.ok(recruiterService.createRecruiter(recruiter));
    }

    @GetMapping("/{companyId}/recruiters")
    public ResponseEntity<Optional<List<Recruiter>>> getRecruitersByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(recruiterService.getRecruitersByCompany(companyId));
    }

    @DeleteMapping("/recruiter/{id}")
    public ResponseEntity<Void> deleteRecruiter(@PathVariable Long id) {
        recruiterService.deleteRecruiter(id);
        return ResponseEntity.noContent().build();
    }
}
