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
    private String status;
    private String transfusionDate;
    private String reactionSeverity;
    private String reactionNotes;

    public Long getId() { return id; }

    public Long getBloodUnitId() { return bloodUnitId; }
    public void setBloodUnitId(Long bloodUnitId) { this.bloodUnitId = bloodUnitId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTransfusionDate() { return transfusionDate; }
    public void setTransfusionDate(String transfusionDate) { this.transfusionDate = transfusionDate; }

    public String getReactionSeverity() { return reactionSeverity; }
    public void setReactionSeverity(String reactionSeverity) { this.reactionSeverity = reactionSeverity; }

    public String getReactionNotes() { return reactionNotes; }
    public void setReactionNotes(String reactionNotes) { this.reactionNotes = reactionNotes; }
}