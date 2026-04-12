package com.bloodbank.bloodbank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bloodbank.bloodbank.entity.BloodRequest;
import com.bloodbank.bloodbank.enums.RequestStatus;

public interface BloodRequestRepository extends JpaRepository<BloodRequest, Long> {

    List<BloodRequest> findByStatus(RequestStatus status);

    List<BloodRequest> findByRequestedByUserIdOrderByIdDesc(Long requestedByUserId);
}