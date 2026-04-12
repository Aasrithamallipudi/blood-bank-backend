package com.bloodbank.bloodbank.dto;

public class EmergencyDonorResponseDTO {

    private Long alertId;
    private Long donorId;
    private Long collectedUnitId;
    private Integer remainingUnits;
    private String alertStatus;
    private String message;

    public Long getAlertId() {
        return alertId;
    }

    public void setAlertId(Long alertId) {
        this.alertId = alertId;
    }

    public Long getDonorId() {
        return donorId;
    }

    public void setDonorId(Long donorId) {
        this.donorId = donorId;
    }

    public Long getCollectedUnitId() {
        return collectedUnitId;
    }

    public void setCollectedUnitId(Long collectedUnitId) {
        this.collectedUnitId = collectedUnitId;
    }

    public Integer getRemainingUnits() {
        return remainingUnits;
    }

    public void setRemainingUnits(Integer remainingUnits) {
        this.remainingUnits = remainingUnits;
    }

    public String getAlertStatus() {
        return alertStatus;
    }

    public void setAlertStatus(String alertStatus) {
        this.alertStatus = alertStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}