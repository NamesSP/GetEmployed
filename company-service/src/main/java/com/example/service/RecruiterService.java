package com.example.service;

import com.example.entity.Company;
import com.example.entity.Recruiter;
import com.example.repository.CompanyRepository;
import com.example.repository.RecruiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecruiterService {
    @Autowired
            CompanyRepository companyRepository;
    RecruiterRepository recruiterRepository;

    public Recruiter createRecruiter(Recruiter entity) {
        Company company = companyRepository.findById(entity.getId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        Recruiter recruiter = new Recruiter();
        recruiter.setRecruiterName(entity.getRecruiterName());
        recruiter.setEmail(entity.getEmail());
        recruiter.setCompany(company);

        return recruiterRepository.save(recruiter);
    }
    public Optional<Recruiter> getRecruitersByCompany(Long companyId) {
        return recruiterRepository.findById(companyId);
    }

    public void deleteRecruiter(Long id) {
        recruiterRepository.deleteById(id);
    }
}
