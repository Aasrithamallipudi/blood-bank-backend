package com.bloodbank.bloodbank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bloodbank.bloodbank.entity.BloodUnit;
import com.bloodbank.bloodbank.enums.BloodUnitStatus;

public interface BloodUnitRepository extends JpaRepository<BloodUnit, Long> {

	List<BloodUnit> findByStatusAndBloodGroupIgnoreCaseOrderByExpiryDateAsc(BloodUnitStatus status, String bloodGroup);

	List<BloodUnit> findByDonor_IdOrderByIdDesc(Long donorId);

	List<BloodUnit> findByStatusAndBloodGroupIgnoreCaseAndComponentTypeIgnoreCaseOrderByExpiryDateAsc(
			BloodUnitStatus status,
			String bloodGroup,
			String componentType
	);
}