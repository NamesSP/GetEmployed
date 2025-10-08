package com.example.service;

import com.example.dto.RecruiterRequest;
import com.example.entity.Companies;
import com.example.entity.Recruiter;
import com.example.repository.CompanyRepository;
import com.example.repository.RecruiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
@Service
public class RecruiterService {

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    RecruiterRepository recruiterRepository;
    // Recruiter CRUD
    public List<Recruiter> getAllRecruiters() {
        return recruiterRepository.findAll();
    }

    public Recruiter createRecruiter(Recruiter recruiter) {
        if (recruiter.getCompany() == null || recruiter.getCompany().getCompanyId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company is required for recruiter");
        }
        return recruiterRepository.save(recruiter);
    }

    public Optional<List<Recruiter>> getRecruitersByCompany(Long companyId) {
        return recruiterRepository.findByCompany_CompanyId(companyId);
    }

    public void deleteRecruiter(Long id) {
        if (!recruiterRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recruiter not found");
        }
        recruiterRepository.deleteById(id);
    }

    private RecruiterRequest toDto(Recruiter recruiter) {
        RecruiterRequest dto = new RecruiterRequest();
        dto.setRecruiterId(recruiter.getRecruiterId());
        dto.setRecruiterName(recruiter.getRecruiterName());
        dto.setEmail(recruiter.getEmail());
        if (recruiter.getCompany() != null) {
            dto.setCompanyId(recruiter.getCompany().getCompanyId());
        }
        return dto;
    }

    private Recruiter toEntity(RecruiterRequest dto) {
        Recruiter recruiter = new Recruiter();
        recruiter.setRecruiterName(dto.getRecruiterName());
        recruiter.setEmail(dto.getEmail());

        if (dto.getCompanyId() != null) {
            Companies company = companyRepository.findById(dto.getCompanyId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid company ID"));
            recruiter.setCompany(company);
        }

        return recruiter;
    }
}
