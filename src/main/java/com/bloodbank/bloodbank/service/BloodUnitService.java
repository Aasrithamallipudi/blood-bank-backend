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

    // ✅ Create Blood Unit
    public BloodUnit save(BloodUnitDTO dto) {

        BloodUnit unit = new BloodUnit();

        Donor donor = donorRepository.findById(dto.getDonorId()).orElseThrow();

        unit.setDonor(donor);
        unit.setBloodGroup(dto.getBloodGroup());
        unit.setComponentType(dto.getComponentType());
        unit.setVolumeMl(dto.getVolumeMl());
        unit.setCreatedAt(LocalDate.now());

        unit.setStatus(BloodUnitStatus.COLLECTED);
        unit.setExpiryDate(LocalDate.now().plusDays(2));

        return bloodUnitRepository.save(unit);
    }

    // ✅ Get all
    public List<BloodUnit> getAll() {
        return bloodUnitRepository.findAll();
    }

    public List<BloodUnit> getByDonorId(Long donorId) {
        return bloodUnitRepository.findByDonor_IdOrderByIdDesc(donorId);
    }

    // ✅ Mark as tested
    public BloodUnit markAsTested(Long id) {
        BloodUnit unit = bloodUnitRepository.findById(id).orElseThrow();
        if (unit.getStatus() != BloodUnitStatus.TESTING && unit.getStatus() != BloodUnitStatus.COLLECTED) {
            throw new IllegalStateException("Only collected/testing units can be marked as available");
        }
        unit.setStatus(BloodUnitStatus.AVAILABLE); // or TESTED if you have it
        return bloodUnitRepository.save(unit);
    }

    public BloodUnit moveToTesting(Long id) {
        BloodUnit unit = bloodUnitRepository.findById(id).orElseThrow();
        if (unit.getStatus() != BloodUnitStatus.COLLECTED) {
            throw new IllegalStateException("Only collected units can move to testing");
        }

        unit.setStatus(BloodUnitStatus.TESTING);
        return bloodUnitRepository.save(unit);
    }

    public BloodUnit reserve(Long id) {
        BloodUnit unit = bloodUnitRepository.findById(id).orElseThrow();
        if (unit.getStatus() != BloodUnitStatus.AVAILABLE) {
            throw new IllegalStateException("Only available units can be reserved");
        }

        unit.setStatus(BloodUnitStatus.RESERVED);
        return bloodUnitRepository.save(unit);
    }

    public BloodUnit issue(Long id) {
        BloodUnit unit = bloodUnitRepository.findById(id).orElseThrow();
        if (unit.getStatus() != BloodUnitStatus.RESERVED) {
            throw new IllegalStateException("Only reserved units can be issued");
        }

        unit.setStatus(BloodUnitStatus.ISSUED);
        return bloodUnitRepository.save(unit);
    }

    public BloodUnit markAsTransfused(Long id) {
        BloodUnit unit = bloodUnitRepository.findById(id).orElseThrow();
        if (unit.getStatus() != BloodUnitStatus.ISSUED) {
            throw new IllegalStateException("Only issued units can be marked transfused");
        }

        unit.setStatus(BloodUnitStatus.DISCARDED);
        return bloodUnitRepository.save(unit);
    }

    // ✅ Discard
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