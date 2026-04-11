package com.bloodbank.bloodbank.dto;

public class BloodUnitDTO {

    private Long donorId;
    private String bloodGroup;
    private String componentType;
    private Double volumeMl;

    public Long getDonorId() {
        return donorId;
    }

    public void setDonorId(Long donorId) {
        this.donorId = donorId;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public Double getVolumeMl() {
        return volumeMl;
    }

    public void setVolumeMl(Double volumeMl) {
        this.volumeMl = volumeMl;
    }
}