package com.bloodbank.bloodbank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bloodbank.bloodbank.entity.EmergencyAlert;
import com.bloodbank.bloodbank.repository.EmergencyAlertRepository;

@Service
public class EmergencyAlertService {

    @Autowired
    private EmergencyAlertRepository repository;

    public EmergencyAlert create(EmergencyAlert alert) {
        alert.setStatus("ACTIVE");
        return repository.save(alert);
    }

    public List<EmergencyAlert> getActive() {
        return repository.findByStatus("ACTIVE");
    }

    public EmergencyAlert fulfill(Long id) {
        EmergencyAlert alert = repository.findById(id).orElseThrow();
        alert.setStatus("FULFILLED");
        return repository.save(alert);
    }
}