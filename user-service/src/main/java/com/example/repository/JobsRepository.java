package com.example.repository;

import com.example.entity.Jobs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobsRepository extends JpaRepository<Jobs, Long> {
    List<Jobs> findByRecruiterId(Long recruiterId);
    List<Jobs> findByCompanyId(Long companyId);
    List<Jobs> findByStatus(Jobs.JobStatus status);
    List<Jobs> findByPositionContainingIgnoreCase(String position);
}
