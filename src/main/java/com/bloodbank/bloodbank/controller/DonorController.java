package com.bloodbank.bloodbank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bloodbank.bloodbank.dto.DonorEligibilityDTO;
import com.bloodbank.bloodbank.dto.DonorDTO;
import com.bloodbank.bloodbank.dto.EmergencyDonorResponseDTO;
import com.bloodbank.bloodbank.entity.Donor;
import com.bloodbank.bloodbank.entity.DonorNotification;
import com.bloodbank.bloodbank.entity.EmergencyAlert;
import com.bloodbank.bloodbank.service.DonorService;
import com.bloodbank.bloodbank.service.EmergencyAlertService;
import com.bloodbank.bloodbank.service.RoleGuardService;

@RestController
@RequestMapping("/api/donors")
public class DonorController {

    @Autowired
    private DonorService service;

    @Autowired
    private RoleGuardService roleGuardService;

    @Autowired
    private EmergencyAlertService emergencyAlertService;

    @PostMapping("/register")
    public Donor register(
            @RequestBody DonorDTO dto,
            @RequestHeader(value = "X-User-Role", required = false) String userRole
    ) {
        roleGuardService.requireAnyRole(userRole, "ADMIN", "BLOOD_BANK_ADMIN", "DONOR");
        return service.saveDonor(dto);
    }

    @PutMapping("/{id}/profile")
    public Donor updateProfile(
            @PathVariable Long id,
            @RequestBody DonorDTO dto,
            @RequestHeader(value = "X-User-Role", required = false) String userRole
    ) {
        roleGuardService.requireAnyRole(userRole, "DONOR", "ADMIN", "BLOOD_BANK_ADMIN");
        return service.updateDonor(id, dto);
    }

    @GetMapping("/me")
    public Donor getMyProfile(
            @RequestParam Long userId,
            @RequestHeader(value = "X-User-Role", required = false) String userRole
    ) {
        roleGuardService.requireAnyRole(userRole, "DONOR", "ADMIN", "BLOOD_BANK_ADMIN");
        return service.getByUserId(userId);
    }


    @GetMapping("/{id}/eligibility")
    public DonorEligibilityDTO getEligibility(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Role", required = false) String userRole
    ) {
        roleGuardService.requireAnyRole(userRole, "DONOR", "ADMIN", "BLOOD_BANK_ADMIN");
        return service.getEligibility(id);
    }

    @GetMapping("/{id}/alerts")
    public List<EmergencyAlert> getAlertsForDonor(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Role", required = false) String userRole
    ) {
        roleGuardService.requireAnyRole(userRole, "DONOR", "ADMIN", "BLOOD_BANK_ADMIN", "EMERGENCY_COORDINATOR");
        return emergencyAlertService.getActiveAlertsForDonor(id);
    }

    @GetMapping("/{id}/notifications")
    public List<DonorNotification> getNotificationsForDonor(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Role", required = false) String userRole
    ) {
        roleGuardService.requireAnyRole(userRole, "DONOR", "ADMIN", "BLOOD_BANK_ADMIN", "EMERGENCY_COORDINATOR");
        return emergencyAlertService.getNotificationsForDonor(id);
    }

    @PostMapping("/{id}/notifications/{notificationId}/respond")
    public EmergencyDonorResponseDTO respondToDonationRequest(
            @PathVariable Long id,
            @PathVariable Long notificationId,
            @RequestParam boolean accept,
            @RequestHeader(value = "X-User-Role", required = false) String userRole
    ) {
        roleGuardService.requireAnyRole(userRole, "DONOR", "ADMIN", "BLOOD_BANK_ADMIN");
        return emergencyAlertService.donorRespondToNotification(id, notificationId, accept);
    }

    @GetMapping
    public List<Donor> getAll() {
        return service.getAllDonors();
    }

    @GetMapping("/notify")
    public List<Donor> notifyDonors(@RequestParam String bloodGroup) {
        return service.getEligible(bloodGroup);
    }

    @GetMapping("/eligible")
    public List<Donor> eligible(@RequestParam String bloodGroup) {
        return service.getEligible(bloodGroup);
    }
}