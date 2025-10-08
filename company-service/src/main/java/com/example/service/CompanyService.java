
package com.example.service;

import com.example.dto.CompanyDto;
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
import java.util.stream.Collectors;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    public CompanyDto createCompany(CompanyDto companyDto) {
        Companies company = toEntity(companyDto);
        company = companyRepository.save(company);
        return toDto(company);
    }

    public CompanyDto updateCompany(Long id, CompanyDto companyDto) {
        Companies company = companyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
        company.setCompanyName(companyDto.getCompanyName());
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
        Optional<Companies> company = companyRepository.findById(id);
        return company.map(this::toDto).orElse(null);
    }

    public List<CompanyDto> getAllCompanies() {
        return companyRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    private CompanyDto toDto(Companies company) {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setCompanyId(company.getCompanyId());
        companyDto.setCompanyName(company.getCompanyName());
        companyDto.setDescription(company.getDescription());
        return companyDto;
    }

    private Companies toEntity(CompanyDto companyDto) {
        Companies company = new Companies();
        company.setCompanyName(companyDto.getCompanyName());
        company.setDescription(companyDto.getDescription());
        return company;
    }
}
