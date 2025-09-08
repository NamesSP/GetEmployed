package com.example.repository;

import com.example.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByAuthId(Long authId);
    List<UserEntity> findByFirstNameContainingIgnoreCase(String firstName);
    List<UserEntity> findByLastNameContainingIgnoreCase(String lastName);
    boolean existsByAuthId(Long authId);
}
