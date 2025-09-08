package com.example.repository;

import com.example.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
//    boolean existsByUserName(String userName);
//    boolean existsByEmail(String email);

      Optional<UserEntity> findByAuthId(Long authId);
}
