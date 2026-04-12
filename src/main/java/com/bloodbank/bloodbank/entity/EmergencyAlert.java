package com.bloodbank.bloodbank.entity;

import com.bloodbank.bloodbank.enums.EmergencyStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class EmergencyAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bloodGroup;
    private Integer requiredUnits;
    private String location;

    @ManyToOne
    private User coordinator;

    @Enumerated(EnumType.STRING)
    private EmergencyStatus status;

    public Long getId() { return id; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public Integer getRequiredUnits() { return requiredUnits; }
    public void setRequiredUnits(Integer requiredUnits) { this.requiredUnits = requiredUnits; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public User getCoordinator() { return coordinator; }
    public void setCoordinator(User coordinator) { this.coordinator = coordinator; }

    public EmergencyStatus getStatus() { return status; }
    public void setStatus(EmergencyStatus status) { this.status = status; }
}