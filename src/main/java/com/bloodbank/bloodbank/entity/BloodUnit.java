package com.bloodbank.bloodbank.entity;

import java.time.LocalDate;

import com.bloodbank.bloodbank.enums.BloodUnitStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class BloodUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bloodGroup;
    private String componentType;
    private Double volumeMl;

    @Enumerated(EnumType.STRING)
    private BloodUnitStatus status;

    private LocalDate expiryDate;

    @ManyToOne
    private Donor donor;

    public Long getId() { return id; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public String getComponentType() { return componentType; }
    public void setComponentType(String componentType) { this.componentType = componentType; }

    public Double getVolumeMl() { return volumeMl; }
    public void setVolumeMl(Double volumeMl) { this.volumeMl = volumeMl; }

    public BloodUnitStatus getStatus() { return status; }
    public void setStatus(BloodUnitStatus status) { this.status = status; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public Donor getDonor() { return donor; }
    public void setDonor(Donor donor) { this.donor = donor; }
}