package com.example.service;

import com.example.dto.RecruiterRequest;
import com.example.entity.Company;
import com.example.entity.Recruiter;
import com.example.repository.CompanyRepository;
import com.example.repository.RecruiterRepository;
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

        if (companyDetails.getCompanyName() != null) {
            company.setCompanyName(companyDetails.getCompanyName());
        }
        if (companyDetails.getLocation() != null) {
            company.setLocation(companyDetails.getLocation());
        }

        return companyRepository.save(company);
    }

    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }

    @Autowired
    RecruiterRepository recruiterRepository;

    public Recruiter createRecruiter(RecruiterRequest request) {
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        Recruiter recruiter = new Recruiter();
        recruiter.setRecruiterName(request.getRecruiterName());
        recruiter.setEmail(request.getEmail());
        recruiter.setCompany(company);

        return recruiterRepository.save(recruiter);
    }

    public Optional<List<Recruiter>> getRecruitersByCompany(Long companyId) {
        return recruiterRepository.findByCompany_companyId(companyId);
    }

    public void deleteRecruiter(Long id) {
        recruiterRepository.deleteById(id);
    }

    public List<Recruiter> getAllRecruiters() {
        return recruiterRepository.findAll();
    }

}
