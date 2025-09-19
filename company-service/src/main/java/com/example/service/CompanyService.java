
package com.example.service;

import com.example.dto.CompanyDto;
import com.example.entity.Company;
import com.example.entity.Recruiter;
import com.example.repository.CompanyRepository;
import com.example.repository.RecruiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    public CompanyDto createCompany(CompanyDto companyDto) {
        Company company = toEntity(companyDto);
        company = companyRepository.save(company);
        return toDto(company);
    }

    public CompanyDto updateCompany(Long id, CompanyDto companyDto) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
        company.setName(companyDto.getName());
        company.setDescription(companyDto.getDescription());
        company = companyRepository.save(company);
        return toDto(company);
    }

    public void deleteCompany(Long id) {
        if (!companyRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found");
        }
        companyRepository.deleteById(id);
    }

    public CompanyDto getCompanyById(Long id) {
        Optional<Company> company = companyRepository.findById(id);
        return company.map(this::toDto).orElse(null);
    }

    public List<CompanyDto> getAllCompanies() {
        return companyRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    // Recruiter CRUD
    public List<Recruiter> getAllRecruiters() {
        return recruiterRepository.findAll();
    }

    public Recruiter createRecruiter(Recruiter recruiter) {
        if (recruiter.getCompany() == null || recruiter.getCompany().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company is required for recruiter");
        }
        return recruiterRepository.save(recruiter);
    }

    public Optional<List<Recruiter>> getRecruitersByCompany(Long companyId) {
        return recruiterRepository.findByCompany_Id(companyId);
    }

    public void deleteRecruiter(Long id) {
        if (!recruiterRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recruiter not found");
        }
        recruiterRepository.deleteById(id);
    }

    private CompanyDto toDto(Company company) {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setId(company.getId());
        companyDto.setName(company.getName());
        companyDto.setDescription(company.getDescription());
        return companyDto;
    }

    private Company toEntity(CompanyDto companyDto) {
        Company company = new Company();
        company.setName(companyDto.getName());
        company.setDescription(companyDto.getDescription());
        return company;
    }
}
