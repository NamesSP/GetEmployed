package com.example.repository;

import com.example.entity.ApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationEntity,Integer> {
    List<ApplicationEntity> findByUserId(Long userId);

    // Custom query to find applications by job ID
    List<ApplicationEntity> findByJobId(Long jobId);
}
