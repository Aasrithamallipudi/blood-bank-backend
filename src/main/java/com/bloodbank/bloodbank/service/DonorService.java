package com.bloodbank.bloodbank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bloodbank.bloodbank.dto.DonorEligibilityDTO;
import com.bloodbank.bloodbank.dto.DonorDTO;
import com.bloodbank.bloodbank.entity.Donor;
import com.bloodbank.bloodbank.entity.User;
import com.bloodbank.bloodbank.repository.DonorRepository;
import com.bloodbank.bloodbank.repository.UserRepository;

@Service
public class DonorService {

    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private UserRepository userRepository;

    public Donor saveDonor(DonorDTO dto) {

        Donor donor = new Donor();

        User user = userRepository.findById(dto.getUserId()).orElseThrow();

        donor.setUser(user);
        donor.setBloodGroup(normalizeBloodGroup(dto.getBloodGroup()));
        donor.setWeight(dto.getWeight());

        return donorRepository.save(donor);
    }

    public List<Donor> getAllDonors() {
        return donorRepository.findAll();
    }

    public List<Donor> getEligible(String bloodGroup) {
        String normalized = normalizeBloodGroup(bloodGroup);
        return donorRepository.findByBloodGroupIgnoreCase(normalized);
    }

    public Donor getByUserId(Long userId) {
        return donorRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Donor profile not found for user"));
    }

    public Donor updateDonor(Long donorId, DonorDTO dto) {
        Donor donor = donorRepository.findById(donorId)
                .orElseThrow(() -> new IllegalArgumentException("Donor not found"));

        if (dto.getBloodGroup() != null && !dto.getBloodGroup().isBlank()) {
            donor.setBloodGroup(normalizeBloodGroup(dto.getBloodGroup()));
        }

        if (dto.getWeight() != null) {
            donor.setWeight(dto.getWeight());
        }

        return donorRepository.save(donor);
    }

    public DonorEligibilityDTO getEligibility(Long donorId) {
        Donor donor = donorRepository.findById(donorId)
                .orElseThrow(() -> new IllegalArgumentException("Donor not found"));

        boolean hasBloodGroup = donor.getBloodGroup() != null && !donor.getBloodGroup().isBlank();
        boolean hasMinWeight = donor.getWeight() != null && donor.getWeight() >= 50;
        boolean eligible = hasBloodGroup && hasMinWeight;

        DonorEligibilityDTO eligibility = new DonorEligibilityDTO();
        eligibility.setDonorId(donor.getId());
        eligibility.setBloodGroup(donor.getBloodGroup());
        eligibility.setWeight(donor.getWeight());
        eligibility.setEligible(eligible);
        eligibility.setStatus(eligible ? "ELIGIBLE" : "NOT_ELIGIBLE");

        if (!hasBloodGroup) {
            eligibility.setReason("Blood group is required");
        } else if (!hasMinWeight) {
            eligibility.setReason("Minimum weight for donation is 50 kg");
        } else {
            eligibility.setReason("Eligible for donation");
        }

        return eligibility;
    }

    private String normalizeBloodGroup(String bloodGroup) {
        if (bloodGroup == null) {
            return null;
        }

        return bloodGroup.replace(" ", "+").trim().toUpperCase();
    }
}