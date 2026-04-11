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

import com.bloodbank.bloodbank.dto.BloodRequestDTO;
import com.bloodbank.bloodbank.entity.BloodRequest;
import com.bloodbank.bloodbank.service.BloodRequestService;

@RestController
@RequestMapping("/api/blood-requests")
public class BloodRequestController {

    @Autowired
    private BloodRequestService service;

    @PostMapping
    public BloodRequest create(@RequestBody BloodRequestDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}/process")
    public BloodRequest process(@PathVariable Long id) {
        return service.process(id);
    }

    @PutMapping("/{id}/fulfill")
    public BloodRequest fulfill(@PathVariable Long id) {
        return service.fulfill(id);
    }

    @GetMapping
    public List<BloodRequest> getAll() {
        return service.getAll();
    }

    @GetMapping("/pending")
    public List<BloodRequest> getPending() {
        return service.getPending();
    }
}