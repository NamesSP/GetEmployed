package com.example.repository;

import com.example.entity.Job;
import com.example.entity.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByRecruiterId(Long recruiterId);
    List<Job> findByCompanyId(Long companyId);
    List<Job> findByStatus(JobStatus status);
}
