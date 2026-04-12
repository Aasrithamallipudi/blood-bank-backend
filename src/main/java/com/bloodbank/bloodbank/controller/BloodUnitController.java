package com.bloodbank.bloodbank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bloodbank.bloodbank.dto.BloodUnitDTO;
import com.bloodbank.bloodbank.entity.BloodUnit;
import com.bloodbank.bloodbank.service.BloodUnitService;
import com.bloodbank.bloodbank.service.RoleGuardService;

@RestController
@RequestMapping("/api/blood-units")
public class BloodUnitController {

    @Autowired
    private BloodUnitService service;

    @Autowired
    private RoleGuardService roleGuardService;

    // ✅ Create Blood Unit
    @PostMapping
    public BloodUnit create(
            @RequestBody BloodUnitDTO dto,
            @RequestHeader(value = "X-User-Role", required = false) String userRole
    ) {
        roleGuardService.requireAnyRole(userRole, "ADMIN", "BLOOD_BANK_ADMIN");
        return service.save(dto);
    }

    // ✅ Get all blood units
    @GetMapping("/inventory")
    public List<BloodUnit> getAll() {
        return service.getAll();
    }

    // ✅ Mark as tested
    @PutMapping("/{id}/start-testing")
    public BloodUnit startTesting(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Role", required = false) String userRole
    ) {
        roleGuardService.requireAnyRole(userRole, "ADMIN", "BLOOD_BANK_ADMIN");
        return service.moveToTesting(id);
    }

    @PutMapping("/{id}/test")
    public BloodUnit markAsTested(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Role", required = false) String userRole
    ) {
        roleGuardService.requireAnyRole(userRole, "ADMIN", "BLOOD_BANK_ADMIN");
        return service.markAsTested(id);
    }

    @PutMapping("/{id}/reserve")
    public BloodUnit reserve(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Role", required = false) String userRole
    ) {
        roleGuardService.requireAnyRole(userRole, "ADMIN", "BLOOD_BANK_ADMIN", "HOSPITAL_STAFF");
        return service.reserve(id);
    }

    @PutMapping("/{id}/issue")
    public BloodUnit issue(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Role", required = false) String userRole
    ) {
        roleGuardService.requireAnyRole(userRole, "ADMIN", "BLOOD_BANK_ADMIN", "HOSPITAL_STAFF");
        return service.issue(id);
    }

    @PutMapping("/{id}/transfuse")
    public BloodUnit transfuse(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Role", required = false) String userRole
    ) {
        roleGuardService.requireAnyRole(userRole, "ADMIN", "BLOOD_BANK_ADMIN", "HOSPITAL_STAFF");
        return service.markAsTransfused(id);
    }

    // ✅ Discard blood unit
    @PutMapping("/{id}/discard")
    public BloodUnit discard(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Role", required = false) String userRole
    ) {
        roleGuardService.requireAnyRole(userRole, "ADMIN", "BLOOD_BANK_ADMIN");
        return service.discard(id);
    }

    // ✅ Expiring units
    @GetMapping("/expiring")
    public List<BloodUnit> getExpiring() {
        return service.getExpiring();
    }
}