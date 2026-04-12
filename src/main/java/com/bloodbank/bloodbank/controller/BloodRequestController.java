package com.bloodbank.bloodbank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import com.bloodbank.bloodbank.dto.BloodRequestDTO;
import com.bloodbank.bloodbank.dto.RequestFlowResponseDTO;
import com.bloodbank.bloodbank.entity.BloodRequest;
import com.bloodbank.bloodbank.service.BloodRequestService;
import com.bloodbank.bloodbank.service.RoleGuardService;

@RestController
@RequestMapping("/api/blood-requests")
public class BloodRequestController {

    @Autowired
    private BloodRequestService service;

    @Autowired
    private RoleGuardService roleGuardService;

    @PostMapping
    public BloodRequest create(
            @RequestBody BloodRequestDTO dto,
            @org.springframework.web.bind.annotation.RequestHeader(value = "X-User-Role", required = false) String userRole
    ) {
        roleGuardService.requireAnyRole(userRole, "ADMIN", "BLOOD_BANK_ADMIN", "HOSPITAL_STAFF");
        return service.create(dto);
    }

    @PutMapping("/{id}/process")
    public BloodRequest process(
            @PathVariable Long id,
            @org.springframework.web.bind.annotation.RequestHeader(value = "X-User-Role", required = false) String userRole
    ) {
        roleGuardService.requireAnyRole(userRole, "ADMIN", "BLOOD_BANK_ADMIN", "HOSPITAL_STAFF");
        return service.process(id);
    }

    @PutMapping("/{id}/approve")
    public BloodRequest approve(
            @PathVariable Long id,
            @org.springframework.web.bind.annotation.RequestHeader(value = "X-User-Role", required = false) String userRole
    ) {
        roleGuardService.requireAnyRole(userRole, "ADMIN", "BLOOD_BANK_ADMIN");
        return service.process(id);
    }

    @PutMapping("/{id}/fulfill")
    public BloodRequest fulfill(
            @PathVariable Long id,
            @org.springframework.web.bind.annotation.RequestHeader(value = "X-User-Role", required = false) String userRole
    ) {
        roleGuardService.requireAnyRole(userRole, "ADMIN", "BLOOD_BANK_ADMIN", "HOSPITAL_STAFF");
        return service.fulfill(id);
    }

    @PutMapping("/{id}/blood-group")
    public BloodRequest updatePatientBloodGroup(
            @PathVariable Long id,
            @RequestParam String bloodGroup,
            @org.springframework.web.bind.annotation.RequestHeader(value = "X-User-Role", required = false) String userRole
    ) {
        roleGuardService.requireAnyRole(userRole, "ADMIN", "BLOOD_BANK_ADMIN");
        return service.updatePatientBloodGroup(id, bloodGroup);
    }

    @GetMapping
    public List<BloodRequest> getAll() {
        return service.getAll();
    }

    @GetMapping("/pending")
    public List<BloodRequest> getPending() {
        return service.getPending();
    }

    @GetMapping("/mine")
    public List<BloodRequest> getMine(
            @RequestParam Long userId,
            @org.springframework.web.bind.annotation.RequestHeader(value = "X-User-Role", required = false) String userRole
    ) {
        roleGuardService.requireAnyRole(userRole, "ADMIN", "BLOOD_BANK_ADMIN", "HOSPITAL_STAFF");
        return service.getByRequester(userId);
    }

    @PutMapping("/{id}/run-flow")
    public RequestFlowResponseDTO runFlow(
            @PathVariable Long id,
            @org.springframework.web.bind.annotation.RequestHeader(value = "X-User-Role", required = false) String userRole
    ) {
        roleGuardService.requireAnyRole(userRole, "ADMIN", "BLOOD_BANK_ADMIN", "HOSPITAL_STAFF");
        return service.runFlow(id);
    }
}