package com.bloodbank.bloodbank.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bloodbank.bloodbank.dto.EmergencyDonorResponseDTO;
import com.bloodbank.bloodbank.entity.BloodUnit;
import com.bloodbank.bloodbank.dto.EmergencyAlertDTO;
import com.bloodbank.bloodbank.entity.Donor;
import com.bloodbank.bloodbank.entity.EmergencyAlert;
import com.bloodbank.bloodbank.entity.DonorNotification;
import com.bloodbank.bloodbank.entity.User;
import com.bloodbank.bloodbank.enums.BloodUnitStatus;
import com.bloodbank.bloodbank.enums.DonorNotificationStatus;
import com.bloodbank.bloodbank.enums.EmergencyStatus;
import com.bloodbank.bloodbank.repository.BloodUnitRepository;
import com.bloodbank.bloodbank.repository.DonorRepository;
import com.bloodbank.bloodbank.repository.DonorNotificationRepository;
import com.bloodbank.bloodbank.repository.EmergencyAlertRepository;
import com.bloodbank.bloodbank.repository.UserRepository;

@Service
public class EmergencyAlertService {

    @Autowired
    private EmergencyAlertRepository emergencyAlertRepository;

    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DonorNotificationRepository donorNotificationRepository;

    @Autowired
    private BloodUnitRepository bloodUnitRepository;

    // ✅ Create alert
    public EmergencyAlert createAlert(EmergencyAlertDTO dto) {

        EmergencyAlert alert = new EmergencyAlert();

        User coordinator = userRepository.findById(dto.getCoordinatorId()).orElseThrow();

        alert.setBloodGroup(dto.getBloodGroup());
        alert.setRequiredUnits(dto.getRequiredUnits());
        alert.setLocation(dto.getLocation());
        alert.setCoordinator(coordinator);
        alert.setStatus(EmergencyStatus.ACTIVE);

        return emergencyAlertRepository.save(alert);
    }

    // ✅ Get active alerts
    public List<EmergencyAlert> getActiveAlerts() {
        return emergencyAlertRepository.findByStatus(EmergencyStatus.ACTIVE);
    }

    public List<EmergencyAlert> getActiveAlertsByBloodGroup(String bloodGroup) {
        return emergencyAlertRepository.findByStatusAndBloodGroupIgnoreCase(
                EmergencyStatus.ACTIVE,
                normalizeBloodGroup(bloodGroup)
        );
    }

    // ✅ Fulfill alert
    public EmergencyAlert fulfillAlert(Long id) {
        EmergencyAlert alert = emergencyAlertRepository.findById(id).orElseThrow();
        alert.setStatus(EmergencyStatus.FULFILLED);
        return emergencyAlertRepository.save(alert);
    }

    // ✅ Donor match
    public List<Donor> findMatchingDonors(Long alertId) {
        EmergencyAlert alert = emergencyAlertRepository.findById(alertId).orElseThrow();
        return donorRepository.findByBloodGroupIgnoreCase(alert.getBloodGroup());
    }

    public List<DonorNotification> getActiveAlertNotifications() {
        return donorNotificationRepository.findByEmergencyAlertStatus(EmergencyStatus.ACTIVE);
    }

    public List<DonorNotification> getRespondingDonors() {
        return donorNotificationRepository.findByStatusOrderByIdDesc(DonorNotificationStatus.RESPONDED);
    }

    @Transactional
    public EmergencyDonorResponseDTO decideOnNotification(Long notificationId, boolean accept) {
        DonorNotification notification = donorNotificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));

        if (notification.getEmergencyAlert() == null || notification.getDonor() == null) {
            throw new IllegalStateException("Notification is not linked correctly");
        }

        if (accept) {
            if (notification.getStatus() == DonorNotificationStatus.RESPONDED) {
                EmergencyDonorResponseDTO alreadyAccepted = new EmergencyDonorResponseDTO();
                alreadyAccepted.setAlertId(notification.getEmergencyAlert().getId());
                alreadyAccepted.setDonorId(notification.getDonor().getId());
                alreadyAccepted.setCollectedUnitId(null);
                alreadyAccepted.setRemainingUnits(notification.getEmergencyAlert().getRequiredUnits());
                alreadyAccepted.setAlertStatus(notification.getEmergencyAlert().getStatus().name());
                alreadyAccepted.setMessage("Donor already confirmed");
                return alreadyAccepted;
            }

            return simulateDonorResponse(notification.getEmergencyAlert().getId(), notification.getDonor().getId());
        }

        notification.setStatus(DonorNotificationStatus.DECLINED);
        notification.setRespondedAt(LocalDateTime.now());
        donorNotificationRepository.save(notification);

        EmergencyDonorResponseDTO declined = new EmergencyDonorResponseDTO();
        declined.setAlertId(notification.getEmergencyAlert().getId());
        declined.setDonorId(notification.getDonor().getId());
        declined.setCollectedUnitId(null);
        declined.setRemainingUnits(notification.getEmergencyAlert().getRequiredUnits());
        declined.setAlertStatus(notification.getEmergencyAlert().getStatus().name());
        declined.setMessage("Donor response rejected by coordinator");
        return declined;
    }

    public List<EmergencyAlert> getActiveAlertsForDonor(Long donorId) {
        Donor donor = donorRepository.findById(donorId)
                .orElseThrow(() -> new IllegalArgumentException("Donor not found"));

        if (donor.getBloodGroup() == null || donor.getBloodGroup().isBlank()) {
            return List.of();
        }

        return emergencyAlertRepository.findByStatusAndBloodGroupIgnoreCase(
                EmergencyStatus.ACTIVE,
                donor.getBloodGroup()
        );
    }

    public List<DonorNotification> getNotificationsForDonor(Long donorId) {
        return donorNotificationRepository.findByDonorIdOrderByIdDesc(donorId);
    }

    private String normalizeBloodGroup(String bloodGroup) {
        if (bloodGroup == null) {
            return null;
        }

        return bloodGroup.replace(" ", "+").trim().toUpperCase();
    }

    @Transactional
    public EmergencyDonorResponseDTO donorRespondToNotification(Long donorId, Long notificationId, boolean accept) {
        DonorNotification notification = donorNotificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));

        if (notification.getDonor() == null || !notification.getDonor().getId().equals(donorId)) {
            throw new IllegalStateException("This notification does not belong to the donor");
        }

        if (notification.getStatus() != DonorNotificationStatus.SENT) {
            throw new IllegalStateException("Donation request already responded");
        }

        if (!accept) {
            notification.setStatus(DonorNotificationStatus.DECLINED);
            notification.setRespondedAt(LocalDateTime.now());
            donorNotificationRepository.save(notification);

            EmergencyDonorResponseDTO declined = new EmergencyDonorResponseDTO();
            declined.setAlertId(notification.getEmergencyAlert().getId());
            declined.setDonorId(donorId);
            declined.setCollectedUnitId(null);
            declined.setRemainingUnits(notification.getEmergencyAlert().getRequiredUnits());
            declined.setAlertStatus(notification.getEmergencyAlert().getStatus().name());
            declined.setMessage("Donor declined donation request");
            return declined;
        }

        return simulateDonorResponse(notification.getEmergencyAlert().getId(), donorId);
    }

    @Transactional
    public int notifyDonors(Long alertId) {
        EmergencyAlert alert = emergencyAlertRepository.findById(alertId).orElseThrow();
        List<Donor> donors = donorRepository.findByBloodGroupIgnoreCase(alert.getBloodGroup());

        int createdNotifications = 0;
        for (Donor donor : donors) {
            boolean exists = donorNotificationRepository.existsByEmergencyAlertIdAndDonorId(alertId, donor.getId());
            if (exists) {
                continue;
            }

            DonorNotification notification = new DonorNotification();
            notification.setEmergencyAlert(alert);
            notification.setDonor(donor);
            notification.setStatus(DonorNotificationStatus.SENT);
            notification.setNotifiedAt(LocalDateTime.now());
            donorNotificationRepository.save(notification);
            createdNotifications++;
        }

        return createdNotifications;
    }

    @Transactional
    public EmergencyDonorResponseDTO simulateDonorResponse(Long alertId, Long donorId) {
        EmergencyAlert alert = emergencyAlertRepository.findById(alertId).orElseThrow();

        if (alert.getStatus() != EmergencyStatus.ACTIVE) {
            throw new IllegalStateException("Alert is not active");
        }

        DonorNotification notification;
        if (donorId != null) {
            notification = donorNotificationRepository.findByEmergencyAlertIdAndDonorId(alertId, donorId)
                    .orElseThrow(() -> new IllegalArgumentException("No notification found for the donor in this alert"));
        } else {
            notification = donorNotificationRepository
                    .findFirstByEmergencyAlertIdAndStatusOrderByIdAsc(alertId, DonorNotificationStatus.SENT)
                    .orElseGet(() -> {
                        int newlyNotified = notifyDonors(alertId);
                        if (newlyNotified > 0) {
                            return donorNotificationRepository
                                    .findFirstByEmergencyAlertIdAndStatusOrderByIdAsc(alertId, DonorNotificationStatus.SENT)
                                    .orElseThrow(() -> new IllegalStateException("Unable to load pending donor response"));
                        }

                        List<Donor> fallbackDonors = donorRepository.findByBloodGroupIgnoreCase(alert.getBloodGroup());
                        if (fallbackDonors.isEmpty()) {
                            throw new IllegalStateException("No donor available for this emergency alert");
                        }

                        DonorNotification fallback = new DonorNotification();
                        fallback.setEmergencyAlert(alert);
                        fallback.setDonor(fallbackDonors.get(0));
                        fallback.setStatus(DonorNotificationStatus.SENT);
                        fallback.setNotifiedAt(LocalDateTime.now());
                        return donorNotificationRepository.save(fallback);
                    });
        }

        notification.setStatus(DonorNotificationStatus.RESPONDED);
        notification.setRespondedAt(LocalDateTime.now());
        donorNotificationRepository.save(notification);

        Donor donor = notification.getDonor();
        BloodUnit collectedUnit = new BloodUnit();
        collectedUnit.setDonor(donor);
        collectedUnit.setBloodGroup(donor.getBloodGroup());
        collectedUnit.setComponentType("WHOLE_BLOOD");
        collectedUnit.setVolumeMl(450.0);
        collectedUnit.setExpiryDate(LocalDate.now().plusDays(35));
        collectedUnit.setStatus(BloodUnitStatus.COLLECTED);
        bloodUnitRepository.save(collectedUnit);

        int remaining = java.util.Optional.ofNullable(alert.getRequiredUnits()).orElse(0);
        remaining = Math.max(remaining - 1, 0);
        alert.setRequiredUnits(remaining);
        if (remaining == 0) {
            alert.setStatus(EmergencyStatus.FULFILLED);
        }
        emergencyAlertRepository.save(alert);

        EmergencyDonorResponseDTO response = new EmergencyDonorResponseDTO();
        response.setAlertId(alert.getId());
        response.setDonorId(donor.getId());
        response.setCollectedUnitId(collectedUnit.getId());
        response.setRemainingUnits(remaining);
        response.setAlertStatus(alert.getStatus().name());
        response.setMessage("Donor responded and a new blood unit is collected in COLLECTED state");
        return response;
    }
}