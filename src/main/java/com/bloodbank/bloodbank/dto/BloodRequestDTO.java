package com.bloodbank.bloodbank.dto;

public class BloodRequestDTO {

    private String patientName;
    private String patientBloodGroup;
    private String componentType;
    private Integer unitsRequested;

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getPatientBloodGroup() { return patientBloodGroup; }
    public void setPatientBloodGroup(String patientBloodGroup) { this.patientBloodGroup = patientBloodGroup; }

    public String getComponentType() { return componentType; }
    public void setComponentType(String componentType) { this.componentType = componentType; }

    public Integer getUnitsRequested() { return unitsRequested; }
    public void setUnitsRequested(Integer unitsRequested) { this.unitsRequested = unitsRequested; }
}