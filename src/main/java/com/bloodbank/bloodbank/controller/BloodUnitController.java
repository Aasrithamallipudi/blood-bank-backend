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

import com.bloodbank.bloodbank.dto.BloodUnitDTO;
import com.bloodbank.bloodbank.entity.BloodUnit;
import com.bloodbank.bloodbank.service.BloodUnitService;

@RestController
@RequestMapping("/api/blood-units")
public class BloodUnitController {

    @Autowired
    private BloodUnitService service;

    @PostMapping
    public BloodUnit add(@RequestBody BloodUnitDTO dto) {
        return service.save(dto);
    }

    @GetMapping("/inventory")
    public List<BloodUnit> inventory() {
        return service.getAll();
    }

    @PutMapping("/{id}/discard")
    public BloodUnit discard(@PathVariable Long id) {
        return service.discard(id);
    }

    @GetMapping("/expiring")
    public List<BloodUnit> expiring() {
        return service.getExpiring();
    }
}