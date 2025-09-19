package com.example.controller;

import com.example.dto.CompanyDto;
import com.example.entity.Recruiter;
import com.example.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<CompanyDto> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDto> getCompanyById(@PathVariable Long id) {
        CompanyDto dto = companyService.getCompanyById(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public CompanyDto createCompany(@RequestBody CompanyDto company) {
        return companyService.createCompany(company);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyDto> updateCompany(@PathVariable Long id, @RequestBody CompanyDto companyDetails) {
        return ResponseEntity.ok(companyService.updateCompany(id, companyDetails));
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
    public ResponseEntity<Recruiter> addRecruiter(@RequestBody Recruiter recruiter) {
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
