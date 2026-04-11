package com.bloodbank.bloodbank.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TransfusionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bloodUnitId;
    private String patientName;
    private String transfusionDate;

    public Long getId() { return id; }

    public Long getBloodUnitId() { return bloodUnitId; }
    public void setBloodUnitId(Long bloodUnitId) { this.bloodUnitId = bloodUnitId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getTransfusionDate() { return transfusionDate; }
    public void setTransfusionDate(String transfusionDate) { this.transfusionDate = transfusionDate; }
}