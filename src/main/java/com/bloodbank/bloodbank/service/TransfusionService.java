package com.bloodbank.bloodbank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bloodbank.bloodbank.entity.TransfusionRecord;
import com.bloodbank.bloodbank.repository.TransfusionRepository;

@Service
public class TransfusionService {

    @Autowired
    private TransfusionRepository repository;

    public TransfusionRecord save(TransfusionRecord record) {
        return repository.save(record);
    }

    public List<TransfusionRecord> getByUnit(Long unitId) {
        return repository.findByBloodUnitId(unitId);
    }
}