package com.bloodbank.bloodbank.dto;

import java.util.ArrayList;
import java.util.List;

public class RequestFlowResponseDTO {

    private Long requestId;
    private String requestStatus;
    private String flowPath;
    private Long emergencyAlertId;
    private Integer donorsNotified;
    private List<Long> unitIds = new ArrayList<>();

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getFlowPath() {
        return flowPath;
    }

    public void setFlowPath(String flowPath) {
        this.flowPath = flowPath;
    }

    public Long getEmergencyAlertId() {
        return emergencyAlertId;
    }

    public void setEmergencyAlertId(Long emergencyAlertId) {
        this.emergencyAlertId = emergencyAlertId;
    }

    public Integer getDonorsNotified() {
        return donorsNotified;
    }

    public void setDonorsNotified(Integer donorsNotified) {
        this.donorsNotified = donorsNotified;
    }

    public List<Long> getUnitIds() {
        return unitIds;
    }

    public void setUnitIds(List<Long> unitIds) {
        this.unitIds = unitIds;
    }
}