
package com.example.repository;

import com.example.entity.Jobs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Jobs, Long> {

    List<Jobs> findByCompanyId(Long companyId);
}
