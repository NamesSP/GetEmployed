package com.example.repository;

import com.example.entity.Recruiters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecruitersRepository extends JpaRepository<Recruiters, Long> {
    Optional<Recruiters> findByRecruiterId(Long recruiterId);
    List<Recruiters> findByCompanyId(Long companyId);
    boolean existsByRecruiterId(Long recruiterId);
}
