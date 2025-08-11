package com.toolmanagement.api.repositories;

import com.toolmanagement.api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmployeeId(String employeeId);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    Boolean existsByEmployeeId(String employeeId);
    Boolean existsByEmail(String email);
    Boolean existsByPhone(String phone);
    
    // Methods for username field (mapped to name column in database)
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
}