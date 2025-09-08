package com.example.repository;

import com.example.entity.JobSkills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobSkillsRepository extends JpaRepository<JobSkills, Long> {
    List<JobSkills> findByJobId(Long jobId);

    List<JobSkills> findBySkillId(Long skillId);

    boolean existsByJobIdAndSkillId(Long jobId, Long skillId);

    void deleteByJobIdAndSkillId(Long jobId, Long skillId);
}
