package com.example.service;

import com.example.entity.Job;
import com.example.entity.JobSkill;
import com.example.entity.JobStatus;
import com.example.repository.JobRepository;
import com.example.repository.JobSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final JobSkillRepository jobSkillRepository;

    // Create Job with skills
    public Job createJob(Job job, List<Long> skillIds) {
        job.setPostedOn(LocalDateTime.now());
        job.setCreatedAt(LocalDateTime.now());
        job.setUpdatedAt(LocalDateTime.now());

        Job savedJob = jobRepository.save(job);

        // Add skills
        for (Long skillId : skillIds) {
            JobSkill jobSkill = JobSkill.builder()
                    .job(savedJob)
                    .skillId(skillId)
                    .build();
            jobSkillRepository.save(jobSkill);
        }

        return savedJob;
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Job getJobById(Long jobId) {
        return jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
    }

    public Job updateJob(Long jobId, Job jobUpdate, List<Long> skillIds) {
        Job existing = getJobById(jobId);

        existing.setPosition(jobUpdate.getPosition());
        existing.setRequiredYearsExperience(jobUpdate.getRequiredYearsExperience());
        existing.setOpenings(jobUpdate.getOpenings());
        existing.setStatus(jobUpdate.getStatus());
        existing.setUpdatedAt(LocalDateTime.now());

        Job updatedJob = jobRepository.save(existing);

        // Update skills
        jobSkillRepository.deleteAll(jobSkillRepository.findByJobJobId(jobId));
        for (Long skillId : skillIds) {
            JobSkill jobSkill = JobSkill.builder()
                    .job(updatedJob)
                    .skillId(skillId)
                    .build();
            jobSkillRepository.save(jobSkill);
        }

        return updatedJob;
    }

    public void deleteJob(Long jobId) {
        jobSkillRepository.deleteAll(jobSkillRepository.findByJobJobId(jobId));
        jobRepository.deleteById(jobId);
    }
}
