package com.example.controller;

import com.example.entity.Company;
import com.example.service.CompanyService;
import lombok.AllArgsConstructor;
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

    @GetMapping
    public List<Company> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCompanyById(@PathVariable Long id) {
        Optional<Company> company = companyService.getCompanyById(id);

        if (company.isPresent()) {
            return ResponseEntity.ok(company.get());
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Company not found");
        }
    }


    @PostMapping
    public Company createCompany(@RequestBody Company company) {
        return companyService.createCompany(company);
    }

    @PutMapping ("/{id}")
    public ResponseEntity<String> updateCompany(
            @PathVariable Long id,
            @RequestBody Company companyDetails) {
        try {
            Company updatedcompany = companyService.updateCompany(id, companyDetails);
            return ResponseEntity.ok("Company updated successfully : "+updatedcompany.getName());
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Company not found!");
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }
}