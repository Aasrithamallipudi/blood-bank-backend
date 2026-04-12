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

import com.bloodbank.bloodbank.entity.TransfusionRecord;
import com.bloodbank.bloodbank.service.RoleGuardService;
import com.bloodbank.bloodbank.service.TransfusionService;

@RestController
@RequestMapping("/api/transfusions")
public class TransfusionController {

    @Autowired
    private TransfusionService service;

    @Autowired
    private RoleGuardService roleGuardService;

    @PostMapping
    public TransfusionRecord create(
            @RequestBody TransfusionRecord record,
            @org.springframework.web.bind.annotation.RequestHeader(value = "X-User-Role", required = false) String userRole
    ) {
        roleGuardService.requireAnyRole(userRole, "ADMIN", "BLOOD_BANK_ADMIN", "HOSPITAL_STAFF");
        return service.save(record);
    }

    @GetMapping
    public List<TransfusionRecord> getAll(
            @org.springframework.web.bind.annotation.RequestHeader(value = "X-User-Role", required = false) String userRole
    ) {
        roleGuardService.requireAnyRole(userRole, "ADMIN", "BLOOD_BANK_ADMIN", "HOSPITAL_STAFF");
        return service.getAll();
    }

    @PutMapping("/{id}")
    public TransfusionRecord update(
            @PathVariable Long id,
            @RequestBody TransfusionRecord record,
            @org.springframework.web.bind.annotation.RequestHeader(value = "X-User-Role", required = false) String userRole
    ) {
        roleGuardService.requireAnyRole(userRole, "ADMIN", "BLOOD_BANK_ADMIN", "HOSPITAL_STAFF");
        return service.update(id, record);
    }

    @GetMapping("/blood-unit/{unitId}")
    public List<TransfusionRecord> getByUnit(@PathVariable Long unitId) {
        return service.getByUnit(unitId);
    }
}