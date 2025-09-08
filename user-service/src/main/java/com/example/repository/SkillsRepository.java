package com.example.repository;

import com.example.entity.Skills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillsRepository extends JpaRepository<Skills, Long> {
    Optional<Skills> findBySkillName(String skillName);
    boolean existsBySkillName(String skillName);
    List<Skills> findByCategoryId(Long categoryId);
}
