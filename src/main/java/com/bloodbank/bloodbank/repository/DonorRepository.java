package com.bloodbank.bloodbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bloodbank.bloodbank.entity.Donor;

public interface DonorRepository extends JpaRepository<Donor, Long> {
}