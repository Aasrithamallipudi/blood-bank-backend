package com.bloodbank.bloodbank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bloodbank.bloodbank.entity.BloodUnit;
import com.bloodbank.bloodbank.entity.DonorNotification;
import com.bloodbank.bloodbank.service.BloodUnitService;
import com.bloodbank.bloodbank.service.EmergencyAlertService;
import com.bloodbank.bloodbank.service.RoleGuardService;

@RestController
@RequestMapping("/api/donor")
public class DonorPortalController {

    @Autowired
    private RoleGuardService roleGuardService;

    @Autowired
    private EmergencyAlertService emergencyAlertService;

    @Autowired
    private BloodUnitService bloodUnitService;

    @GetMapping("/requests/{donorId}")
    public List<DonorNotification> getDonationRequests(
            @PathVariable Long donorId,
            @RequestHeader(value = "X-User-Role", required = false) String userRole
    ) {
        roleGuardService.requireAnyRole(userRole, "DONOR", "ADMIN", "BLOOD_BANK_ADMIN");
        return emergencyAlertService.getNotificationsForDonor(donorId);
    }

    @GetMapping("/history/{donorId}")
    public List<BloodUnit> getDonationHistory(
            @PathVariable Long donorId,
            @RequestHeader(value = "X-User-Role", required = false) String userRole
    ) {
        roleGuardService.requireAnyRole(userRole, "DONOR", "ADMIN", "BLOOD_BANK_ADMIN");
        return bloodUnitService.getByDonorId(donorId);
    }
}