package com.bloodbank.bloodbank.dto;

public class DonorDTO {

    private Long userId;
    private String bloodGroup;
    private Double weight;

    // getters & setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }
}