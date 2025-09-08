package com.example.repository;

import com.example.entity.UserSkills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSkillsRepository extends JpaRepository<UserSkills, Long> {
    List<UserSkills> findByUserId(Long userId);

    List<UserSkills> findBySkillId(Long skillId);

    boolean existsByUserIdAndSkillId(Long userId, Long skillId);

    void deleteByUserIdAndSkillId(Long userId, Long skillId);
}
