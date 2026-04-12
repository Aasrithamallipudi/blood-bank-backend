package com.bloodbank.bloodbank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bloodbank.bloodbank.dto.EmergencyAlertDTO;
import com.bloodbank.bloodbank.dto.EmergencyDonorResponseDTO;
import com.bloodbank.bloodbank.entity.Donor;
import com.bloodbank.bloodbank.entity.DonorNotification;
import com.bloodbank.bloodbank.entity.EmergencyAlert;
import com.bloodbank.bloodbank.service.EmergencyAlertService;
import com.bloodbank.bloodbank.service.RoleGuardService;

@RestController
@RequestMapping("/api/emergency-alerts")
public class EmergencyAlertController {

    @Autowired
    private EmergencyAlertService service;

    @Autowired
    private RoleGuardService roleGuardService;
    @PostMapping
    public EmergencyAlert create(
            @RequestBody EmergencyAlertDTO dto,
            @RequestHeader(value = "X-User-Role", required = false) String userRole
    ) {
        roleGuardService.requireAnyRole(userRole, "ADMIN", "BLOOD_BANK_ADMIN", "EMERGENCY_COORDINATOR");
        return service.createAlert(dto);
    }
    @GetMapping("/active")
    public List<EmergencyAlert> getActive() {
        return service.getActiveAlerts();
    }

    @GetMapping("/by-blood-group/{bloodGroup}")
    public List<EmergencyAlert> getByBloodGroup(@PathVariable String bloodGroup) {
        return service.getActiveAlertsByBloodGroup(bloodGroup);
    }
    @PutMapping("/{id}/fulfill")
    public EmergencyAlert fulfill(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Role", required = false) String userRole
    ) {
        roleGuardService.requireAnyRole(userRole, "ADMIN", "BLOOD_BANK_ADMIN", "EMERGENCY_COORDINATOR");
        return service.fulfillAlert(id);
    }
    @GetMapping("/{id}/donor-match")
    public List<Donor> findMatchingDonors(@PathVariable Long id) {
        return service.findMatchingDonors(id);
    }

    @PostMapping("/{id}/notify")
    public Integer notifyDonors(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Role", required = false) String userRole
    ) {
        roleGuardService.requireAnyRole(userRole, "ADMIN", "BLOOD_BANK_ADMIN", "EMERGENCY_COORDINATOR");
        return service.notifyDonors(id);
    }

    @PostMapping("/{id}/simulate-response")
    public EmergencyDonorResponseDTO simulateResponse(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Role", required = false) String userRole,
            @RequestParam(required = false) Long donorId
    ) {
        roleGuardService.requireAnyRole(userRole, "ADMIN", "BLOOD_BANK_ADMIN", "EMERGENCY_COORDINATOR");
        return service.simulateDonorResponse(id, donorId);
    }

    @GetMapping("/notifications/active")
    public List<DonorNotification> getActiveNotifications(
            @RequestHeader(value = "X-User-Role", required = false) String userRole
    ) {
        roleGuardService.requireAnyRole(userRole, "ADMIN", "BLOOD_BANK_ADMIN", "EMERGENCY_COORDINATOR");
        return service.getActiveAlertNotifications();
    }

    @GetMapping("/responders")
    public List<DonorNotification> getRespondingDonors(
            @RequestHeader(value = "X-User-Role", required = false) String userRole
    ) {
        roleGuardService.requireAnyRole(userRole, "ADMIN", "BLOOD_BANK_ADMIN", "EMERGENCY_COORDINATOR");
        return service.getRespondingDonors();
    }

    @PostMapping("/notifications/{notificationId}/decision")
    public EmergencyDonorResponseDTO decideOnNotification(
            @PathVariable Long notificationId,
            @RequestParam boolean accept,
            @RequestHeader(value = "X-User-Role", required = false) String userRole
    ) {
        roleGuardService.requireAnyRole(userRole, "ADMIN", "BLOOD_BANK_ADMIN", "EMERGENCY_COORDINATOR");
        return service.decideOnNotification(notificationId, accept);
    }
}