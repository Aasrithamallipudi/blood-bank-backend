package com.bloodbank.bloodbank.dto;

public class EmergencyAlertDTO {
    private Long coordinatorId;
    private String bloodGroup;
    private Integer requiredUnits;
    private String location;

    public Long getCoordinatorId() {
        return coordinatorId;
    }

    public void setCoordinatorId(Long coordinatorId) {
        this.coordinatorId = coordinatorId;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public Integer getRequiredUnits() {
        return requiredUnits;
    }

    public void setRequiredUnits(Integer requiredUnits) {
        this.requiredUnits = requiredUnits;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
