package com.bloodbank.bloodbank.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bloodbank.bloodbank.dto.BloodUnitDTO;
import com.bloodbank.bloodbank.entity.BloodUnit;
import com.bloodbank.bloodbank.entity.Donor;
import com.bloodbank.bloodbank.enums.BloodUnitStatus;
import com.bloodbank.bloodbank.repository.BloodUnitRepository;
import com.bloodbank.bloodbank.repository.DonorRepository;

@Service
public class BloodUnitService {

    @Autowired
    private BloodUnitRepository bloodUnitRepository;

    @Autowired
    private DonorRepository donorRepository;

    public BloodUnit save(BloodUnitDTO dto) {

        BloodUnit unit = new BloodUnit();

        Donor donor = donorRepository.findById(dto.getDonorId()).orElseThrow();

        unit.setDonor(donor);
        unit.setBloodGroup(dto.getBloodGroup());
        unit.setComponentType(dto.getComponentType());
        unit.setVolumeMl(dto.getVolumeMl());

        unit.setStatus(BloodUnitStatus.AVAILABLE);
        unit.setExpiryDate(LocalDate.now().plusDays(2));

        return bloodUnitRepository.save(unit);
    }

    public List<BloodUnit> getAll() {
        return bloodUnitRepository.findAll();
    }

    public BloodUnit discard(Long id) {
        BloodUnit unit = bloodUnitRepository.findById(id).orElseThrow();
        unit.setStatus(BloodUnitStatus.DISCARDED);
        return bloodUnitRepository.save(unit);
    }

    public List<BloodUnit> getExpiring() {
        return bloodUnitRepository.findAll()
                .stream()
                .filter(u -> {
                    if (u.getExpiryDate() == null) return false;
                    if (u.getStatus() != BloodUnitStatus.AVAILABLE) return false;

                    LocalDate expiry = u.getExpiryDate();
                    LocalDate today = LocalDate.now();

                    return !expiry.isAfter(today.plusDays(5));
                })
                .toList();
    }
}