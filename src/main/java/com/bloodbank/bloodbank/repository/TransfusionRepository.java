package com.bloodbank.bloodbank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bloodbank.bloodbank.entity.TransfusionRecord;

public interface TransfusionRepository extends JpaRepository<TransfusionRecord, Long> {

    List<TransfusionRecord> findByBloodUnitId(Long bloodUnitId);
}