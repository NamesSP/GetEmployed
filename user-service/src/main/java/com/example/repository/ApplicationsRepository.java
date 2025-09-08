package com.example.repository;

import com.example.entity.Applications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationsRepository extends JpaRepository<Applications, Long> {
    List<Applications> findByUserId(Long userId);
    List<Applications> findByJobId(Long jobId);
    List<Applications> findByStatus(Applications.ApplicationStatus status);
    Optional<Applications> findByUserIdAndJobId(Long userId, Long jobId);
    boolean existsByUserIdAndJobId(Long userId, Long jobId);
}
