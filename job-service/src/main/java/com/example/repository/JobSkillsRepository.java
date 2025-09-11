package com.example.repository;

import com.example.entity.JobSkills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobSkillsRepository extends JpaRepository<JobSkills, Long> {
}
