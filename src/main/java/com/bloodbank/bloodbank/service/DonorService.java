package com.bloodbank.bloodbank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        donor.setBloodGroup(dto.getBloodGroup());
        donor.setWeight(dto.getWeight());

        return donorRepository.save(donor);
    }

    public List<Donor> getAllDonors() {
        return donorRepository.findAll();
    }

    public List<Donor> getEligible(String bloodGroup) {
        return donorRepository.findAll()
                .stream()
                .filter(d -> d.getBloodGroup().equalsIgnoreCase(bloodGroup))
                .toList();
    }
}