package com.example.controller;

import com.example.dto.RecruiterRequest;
import com.example.entity.Company;
import com.example.entity.Recruiter;
import com.example.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/company")
public class MainController {

    @Autowired
    CompanyService companyService;

    // ----------------- Company -----------------

    @GetMapping
    public List<Company> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCompanyById(@PathVariable Long id) {
        return companyService.getCompanyById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> (ResponseEntity<Company>) ResponseEntity.status(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public Company createCompany(@RequestBody Company company) {
        return companyService.createCompany(company);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCompany(@PathVariable Long id, @RequestBody Company companyDetails) {
        try {
            Company updatedCompany = companyService.updateCompany(id, companyDetails);
            return ResponseEntity.ok("Company updated successfully: " + updatedCompany.getCompanyName());
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Company not found!");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }

    // ----------------- Recruiter -----------------

    @GetMapping("/allrecruiter")
    public ResponseEntity<List<Recruiter>> getAllRecruiters() {
        return ResponseEntity.ok(companyService.getAllRecruiters());
    }

    @PostMapping("/recruiter")
    public ResponseEntity<Recruiter> addRecruiter(@RequestBody RecruiterRequest recruiter) {
        return ResponseEntity.ok(companyService.createRecruiter(recruiter));
    }

    @GetMapping("/{companyId}/recruiters")
    public ResponseEntity<Optional<List<Recruiter>>> getRecruitersByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(companyService.getRecruitersByCompany(companyId));
    }

    @DeleteMapping("/recruiter/{id}")
    public ResponseEntity<Void> deleteRecruiter(@PathVariable Long id) {
        companyService.deleteRecruiter(id);
        return ResponseEntity.noContent().build();
    }
}
