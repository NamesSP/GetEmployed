package com.example.repository;

import com.example.entity.JobSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobSkillRepository extends JpaRepository<JobSkill, Long> {
    List<JobSkill> findByJobJobId(Long jobid);
}
