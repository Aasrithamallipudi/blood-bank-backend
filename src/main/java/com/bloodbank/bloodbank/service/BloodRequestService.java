package com.bloodbank.bloodbank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bloodbank.bloodbank.dto.BloodRequestDTO;
import com.bloodbank.bloodbank.entity.BloodRequest;
import com.bloodbank.bloodbank.enums.RequestStatus;
import com.bloodbank.bloodbank.repository.BloodRequestRepository;

@Service
public class BloodRequestService {

    @Autowired
    private BloodRequestRepository repository;

    public BloodRequest create(BloodRequestDTO dto) {

        BloodRequest request = new BloodRequest();

        request.setPatientName(dto.getPatientName());
        request.setPatientBloodGroup(dto.getPatientBloodGroup());
        request.setComponentType(dto.getComponentType());
        request.setUnitsRequested(dto.getUnitsRequested());
        request.setStatus(RequestStatus.PENDING);

        return repository.save(request);
    }

    public List<BloodRequest> getAll() {
        return repository.findAll();
    }

    public List<BloodRequest> getPending() {
        return repository.findByStatus(RequestStatus.PENDING);
    }

    public BloodRequest process(Long id) {
        BloodRequest req = repository.findById(id).orElseThrow();
        req.setStatus(RequestStatus.PROCESSING);
        return repository.save(req);
    }

    public BloodRequest fulfill(Long id) {
        BloodRequest req = repository.findById(id).orElseThrow();
        req.setStatus(RequestStatus.FULFILLED);
        return repository.save(req);
    }
}