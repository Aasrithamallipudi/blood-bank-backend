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

import com.bloodbank.bloodbank.entity.EmergencyAlert;
import com.bloodbank.bloodbank.service.EmergencyAlertService;

@RestController
@RequestMapping("/api/emergency-alerts")
public class EmergencyAlertController {

    @Autowired
    private EmergencyAlertService service;

    @PostMapping
    public EmergencyAlert create(@RequestBody EmergencyAlert alert) {
        return service.create(alert);
    }

    @GetMapping("/active")
    public List<EmergencyAlert> getActive() {
        return service.getActive();
    }

    @PutMapping("/{id}/fulfill")
    public EmergencyAlert fulfill(@PathVariable Long id) {
        return service.fulfill(id);
    }
}