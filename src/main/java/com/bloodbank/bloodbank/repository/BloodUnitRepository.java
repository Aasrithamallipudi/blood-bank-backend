package com.bloodbank.bloodbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bloodbank.bloodbank.entity.BloodUnit;

public interface BloodUnitRepository extends JpaRepository<BloodUnit, Long> {
}