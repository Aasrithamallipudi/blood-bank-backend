package com.bloodbank.bloodbank.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bloodbank.bloodbank.entity.Donor;

public interface DonorRepository extends JpaRepository<Donor, Long> {

    List<Donor> findByBloodGroupIgnoreCase(String bloodGroup);

    Optional<Donor> findByUserId(Long userId);

}