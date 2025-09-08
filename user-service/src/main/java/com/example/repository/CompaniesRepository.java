package com.example.repository;

import com.example.entity.Companies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompaniesRepository extends JpaRepository<Companies, Long> {
    Optional<Companies> findByCompanyName(String companyName);
    boolean existsByCompanyName(String companyName);
}
