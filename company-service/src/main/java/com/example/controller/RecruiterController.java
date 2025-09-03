package com.example.controller;

import com.example.entity.Recruiter;
import com.example.service.RecruiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(name="/recruiter")
public class RecruiterController {
    @Autowired
    RecruiterService recruiterService;

    @PostMapping
    public ResponseEntity<Recruiter> addRecruiter(@RequestBody Recruiter recruiter) {
        return ResponseEntity.ok(recruiterService.createRecruiter(recruiter));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<Optional<Recruiter>> getRecruitersByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(recruiterService.getRecruitersByCompany(companyId));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecruiter(@PathVariable Long id) {
        recruiterService.deleteRecruiter(id);
        return ResponseEntity.noContent().build();
    }
}
