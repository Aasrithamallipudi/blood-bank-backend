package com.bloodbank.bloodbank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bloodbank.bloodbank.entity.EmergencyAlert;
import com.bloodbank.bloodbank.enums.EmergencyStatus;

public interface EmergencyAlertRepository extends JpaRepository<EmergencyAlert, Long> {

    List<EmergencyAlert> findByStatus(EmergencyStatus status);

    List<EmergencyAlert> findByStatusAndBloodGroupIgnoreCase(EmergencyStatus status, String bloodGroup);
}