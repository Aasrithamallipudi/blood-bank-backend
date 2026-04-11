package com.bloodbank.bloodbank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bloodbank.bloodbank.entity.User;
import com.bloodbank.bloodbank.enums.Role;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByRole(Role role);

    boolean existsByEmail(String email);
}