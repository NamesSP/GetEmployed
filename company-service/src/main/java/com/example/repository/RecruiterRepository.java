package com.example.repository;

import com.example.entity.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecruiterRepository extends JpaRepository<Recruiter,Long> {
    Optional<List<Recruiter>> findByCompany_Id(Long companyId);
}
