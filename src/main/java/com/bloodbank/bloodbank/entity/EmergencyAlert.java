package com.bloodbank.bloodbank.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class EmergencyAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String requiredBloodGroup;
    private String componentType;
    private Integer unitsNeeded;
    private String status;

    public Long getId() { return id; }

    public String getRequiredBloodGroup() { return requiredBloodGroup; }
    public void setRequiredBloodGroup(String requiredBloodGroup) { this.requiredBloodGroup = requiredBloodGroup; }

    public String getComponentType() { return componentType; }
    public void setComponentType(String componentType) { this.componentType = componentType; }

    public Integer getUnitsNeeded() { return unitsNeeded; }
    public void setUnitsNeeded(Integer unitsNeeded) { this.unitsNeeded = unitsNeeded; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}