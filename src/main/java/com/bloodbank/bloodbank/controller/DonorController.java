package com.bloodbank.bloodbank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bloodbank.bloodbank.dto.DonorDTO;
import com.bloodbank.bloodbank.entity.Donor;
import com.bloodbank.bloodbank.service.DonorService;

@RestController
@RequestMapping("/api/donors")
public class DonorController {

    @Autowired
    private DonorService service;

    @PostMapping("/register")
    public Donor register(@RequestBody DonorDTO dto) {
        return service.saveDonor(dto);
    }

    @GetMapping
    public List<Donor> getAll() {
        return service.getAllDonors();
    }

    @GetMapping("/eligible")
    public List<Donor> eligible(@RequestParam String bloodGroup) {
        return service.getEligible(bloodGroup);
    }
}