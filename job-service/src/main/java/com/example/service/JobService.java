
package com.example.service;

import com.example.client.CompanyClient;
import com.example.dto.JobDto;
import com.example.entity.Jobs;
import com.example.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private CompanyClient companyClient;

    public JobDto createJob(JobDto jobDto) {
        Jobs job = toEntity(jobDto);
        job = jobRepository.save(job);
        return toDto(job);
    }

    public JobDto getJobById(Long id) {
        Jobs job = jobRepository.findById(id).orElse(null);
        if (job != null) {
            return toDto(job);
        }
        return null;
    }

    public List<JobDto> getAllJobs() {
        return jobRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<JobDto> getJobsByCompanyId(Long companyId) {
        return jobRepository.findByCompanyId(companyId).stream().map(this::toDto).collect(Collectors.toList());
    }

    private JobDto toDto(Jobs job) {
        JobDto jobDto = new JobDto();
        jobDto.setId(job.getJobId());
        jobDto.setTitle(job.getPosition());
        jobDto.setDescription(job.getDescription());
        jobDto.setCompanyId(job.getCompanyId());
        jobDto.setCompany(companyClient.getCompanyById(job.getCompanyId()));
        return jobDto;
    }

    private Jobs toEntity(JobDto jobDto) {
        Jobs job = new Jobs();
        job.setPosition(jobDto.getTitle());
        job.setDescription(jobDto.getDescription());
        job.setCompanyId(jobDto.getCompanyId());
        return job;
    }
}
