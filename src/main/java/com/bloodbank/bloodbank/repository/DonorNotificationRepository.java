package com.bloodbank.bloodbank.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bloodbank.bloodbank.entity.DonorNotification;
import com.bloodbank.bloodbank.enums.DonorNotificationStatus;
import com.bloodbank.bloodbank.enums.EmergencyStatus;

public interface DonorNotificationRepository extends JpaRepository<DonorNotification, Long> {

    boolean existsByEmergencyAlertIdAndDonorId(Long emergencyAlertId, Long donorId);

    List<DonorNotification> findByEmergencyAlertId(Long emergencyAlertId);

    Optional<DonorNotification> findFirstByEmergencyAlertIdAndStatusOrderByIdAsc(
            Long emergencyAlertId,
            DonorNotificationStatus status
    );

    Optional<DonorNotification> findByEmergencyAlertIdAndDonorId(Long emergencyAlertId, Long donorId);

    List<DonorNotification> findByEmergencyAlertStatus(EmergencyStatus status);

    List<DonorNotification> findByDonorIdOrderByIdDesc(Long donorId);

    List<DonorNotification> findByStatusOrderByIdDesc(DonorNotificationStatus status);
}