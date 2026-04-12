package com.bloodbank.bloodbank.entity;

import java.time.LocalDateTime;

import com.bloodbank.bloodbank.enums.DonorNotificationStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class DonorNotification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private EmergencyAlert emergencyAlert;

	@ManyToOne
	private Donor donor;

	@Enumerated(EnumType.STRING)
	private DonorNotificationStatus status;

	private LocalDateTime notifiedAt;
	private LocalDateTime respondedAt;

	public Long getId() {
		return id;
	}

	public EmergencyAlert getEmergencyAlert() {
		return emergencyAlert;
	}

	public void setEmergencyAlert(EmergencyAlert emergencyAlert) {
		this.emergencyAlert = emergencyAlert;
	}

	public Donor getDonor() {
		return donor;
	}

	public void setDonor(Donor donor) {
		this.donor = donor;
	}

	public DonorNotificationStatus getStatus() {
		return status;
	}

	public void setStatus(DonorNotificationStatus status) {
		this.status = status;
	}

	public LocalDateTime getNotifiedAt() {
		return notifiedAt;
	}

	public void setNotifiedAt(LocalDateTime notifiedAt) {
		this.notifiedAt = notifiedAt;
	}

	public LocalDateTime getRespondedAt() {
		return respondedAt;
	}

	public void setRespondedAt(LocalDateTime respondedAt) {
		this.respondedAt = respondedAt;
	}
}
