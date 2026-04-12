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
        if (record.getStatus() == null || record.getStatus().isBlank()) {
            record.setStatus("COMPLETED");
        }
        if (record.getReactionSeverity() == null || record.getReactionSeverity().isBlank()) {
            record.setReactionSeverity("NONE");
        }
        return repository.save(record);
    }

    public TransfusionRecord update(Long id, TransfusionRecord updates) {
        TransfusionRecord record = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transfusion record not found"));

        if (updates.getPatientName() != null && !updates.getPatientName().isBlank()) {
            record.setPatientName(updates.getPatientName());
        }

        if (updates.getStatus() != null && !updates.getStatus().isBlank()) {
            record.setStatus(updates.getStatus().trim().toUpperCase());
        }

        if (updates.getReactionSeverity() != null && !updates.getReactionSeverity().isBlank()) {
            record.setReactionSeverity(updates.getReactionSeverity().trim().toUpperCase());
        }

        if (updates.getReactionNotes() != null) {
            record.setReactionNotes(updates.getReactionNotes());
        }

        if (updates.getTransfusionDate() != null && !updates.getTransfusionDate().isBlank()) {
            record.setTransfusionDate(updates.getTransfusionDate());
        }

        return repository.save(record);
    }

    public List<TransfusionRecord> getAll() {
        return repository.findAll();
    }

    public List<TransfusionRecord> getByUnit(Long unitId) {
        return repository.findByBloodUnitId(unitId);
    }
}