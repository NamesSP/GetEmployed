package com.example.service;

import com.example.entity.Company;
import com.example.repository.CompanyRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class CompanyService {
    @Autowired
   CompanyRepository companyRepository;

    public List<Company>  getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }

    public Optional<Company> getCompanyById(Long id) {
        return companyRepository.findById(id);
    }

    public Company updateCompany(Long id, Company companyDetails) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found."));

        if (companyDetails.getName() != null) {
            company.setName(companyDetails.getName());
        }
        if (companyDetails.getLocation() != null) {
            company.setLocation(companyDetails.getLocation());
        }

        return companyRepository.save(company);
    }

    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }

}
