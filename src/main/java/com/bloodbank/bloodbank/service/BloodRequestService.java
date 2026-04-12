package com.bloodbank.bloodbank.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bloodbank.bloodbank.dto.BloodRequestDTO;
import com.bloodbank.bloodbank.dto.RequestFlowResponseDTO;
import com.bloodbank.bloodbank.entity.BloodUnit;
import com.bloodbank.bloodbank.entity.BloodRequest;
import com.bloodbank.bloodbank.entity.EmergencyAlert;
import com.bloodbank.bloodbank.entity.TransfusionRecord;
import com.bloodbank.bloodbank.entity.User;
import com.bloodbank.bloodbank.enums.BloodUnitStatus;
import com.bloodbank.bloodbank.enums.EmergencyStatus;
import com.bloodbank.bloodbank.enums.RequestStatus;
import com.bloodbank.bloodbank.enums.Role;
import com.bloodbank.bloodbank.repository.BloodUnitRepository;
import com.bloodbank.bloodbank.repository.BloodRequestRepository;
import com.bloodbank.bloodbank.repository.EmergencyAlertRepository;
import com.bloodbank.bloodbank.repository.TransfusionRepository;
import com.bloodbank.bloodbank.repository.UserRepository;

@Service
public class BloodRequestService {

    @Autowired
    private BloodRequestRepository repository;

    @Autowired
    private BloodUnitRepository bloodUnitRepository;

    @Autowired
    private EmergencyAlertRepository emergencyAlertRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransfusionRepository transfusionRepository;

    @Autowired
    private EmergencyAlertService emergencyAlertService;

    public BloodRequest create(BloodRequestDTO dto) {

        BloodRequest request = new BloodRequest();

        request.setPatientName(dto.getPatientName());
        request.setPatientBloodGroup(BloodGroupValidator.normalizeAndValidate(dto.getPatientBloodGroup()));
        request.setComponentType(dto.getComponentType());
        request.setUnitsRequested(dto.getUnitsRequested());
        request.setPriority(dto.getPriority() == null || dto.getPriority().isBlank() ? "NORMAL" : dto.getPriority().trim().toUpperCase());
        request.setRequestedByUserId(dto.getRequestedByUserId());
        request.setStatus(RequestStatus.PENDING);

        return repository.save(request);
    }

    public List<BloodRequest> getAll() {
        return repository.findAll();
    }

    public List<BloodRequest> getPending() {
        return repository.findByStatus(RequestStatus.PENDING);
    }

    public List<BloodRequest> getByRequester(Long userId) {
        return repository.findByRequestedByUserIdOrderByIdDesc(userId);
    }

    public BloodRequest process(Long id) {
        BloodRequest req = repository.findById(id).orElseThrow();
        req.setStatus(RequestStatus.PROCESSING);
        return repository.save(req);
    }

    public BloodRequest fulfill(Long id) {
        BloodRequest req = repository.findById(id).orElseThrow();
        req.setStatus(RequestStatus.FULFILLED);
        return repository.save(req);
    }

    public BloodRequest updatePatientBloodGroup(Long id, String bloodGroup) {
        BloodRequest req = repository.findById(id).orElseThrow();
        req.setPatientBloodGroup(BloodGroupValidator.normalizeAndValidate(bloodGroup));
        return repository.save(req);
    }

    @Transactional
    public RequestFlowResponseDTO runFlow(Long requestId) {
        BloodRequest request = repository.findById(requestId).orElseThrow();
        if (request.getStatus() != RequestStatus.PROCESSING && request.getStatus() != RequestStatus.APPROVED) {
            throw new IllegalStateException("Request must be APPROVED before Match & Issue");
        }

        request.setStatus(RequestStatus.PROCESSING);
        repository.save(request);

        List<BloodUnit> availableUnits = findMatchingAvailableUnits(request);
        Integer unitsRequested = request.getUnitsRequested();
        int needed = unitsRequested != null ? unitsRequested : 0;

        RequestFlowResponseDTO response = new RequestFlowResponseDTO();
        response.setRequestId(request.getId());

        if (availableUnits.size() < needed) {
            EmergencyAlert alert = createEmergencyAlertFromRequest(request, needed);
            int notifiedDonors = emergencyAlertService.notifyDonors(alert.getId());

            request.setStatus(RequestStatus.PENDING);
            repository.save(request);

            response.setRequestStatus(request.getStatus().name());
            response.setFlowPath("NOT_AVAILABLE -> EMERGENCY_ALERT -> DONORS_NOTIFIED -> NEW_DONATION_REQUIRED");
            response.setEmergencyAlertId(alert.getId());
            response.setDonorsNotified(notifiedDonors);
            return response;
        }

        List<Long> unitIds = new ArrayList<>();
        for (int i = 0; i < needed; i++) {
            BloodUnit unit = availableUnits.get(i);

            unit.setStatus(BloodUnitStatus.RESERVED);
            bloodUnitRepository.save(unit);

            unit.setStatus(BloodUnitStatus.ISSUED);
            bloodUnitRepository.save(unit);

            TransfusionRecord record = new TransfusionRecord();
            record.setBloodUnitId(unit.getId());
            record.setPatientName(request.getPatientName());
            record.setStatus("COMPLETED");
            record.setReactionSeverity("NONE");
            record.setTransfusionDate(LocalDate.now().toString());
            transfusionRepository.save(record);

            unitIds.add(unit.getId());
        }

        request.setStatus(RequestStatus.FULFILLED);
        repository.save(request);

        response.setRequestStatus(request.getStatus().name());
        response.setFlowPath("AVAILABLE -> ISSUED");
        response.setUnitIds(unitIds);
        response.setDonorsNotified(0);
        return response;
    }

    private List<BloodUnit> findMatchingAvailableUnits(BloodRequest request) {
        String recipientGroup = normalizeBloodGroup(request.getPatientBloodGroup());
        String component = request.getComponentType() == null ? "" : request.getComponentType().trim();
        List<String> compatibleGroups = getCompatibleDonorGroups(recipientGroup);

        return bloodUnitRepository.findAll().stream()
                .filter(unit -> unit.getStatus() == BloodUnitStatus.AVAILABLE)
                .filter(unit -> compatibleGroups.contains(normalizeBloodGroup(unit.getBloodGroup())))
                .filter(unit -> component.isBlank() || component.equalsIgnoreCase(unit.getComponentType()))
                .sorted(
                        Comparator
                                .comparingInt((BloodUnit unit) -> {
                                    String normalized = normalizeBloodGroup(unit.getBloodGroup());
                                    int index = compatibleGroups.indexOf(normalized);
                                    return index >= 0 ? index : Integer.MAX_VALUE;
                                })
                                .thenComparing(BloodUnit::getExpiryDate, Comparator.nullsLast(Comparator.naturalOrder()))
                                .thenComparing(BloodUnit::getId)
                )
                .toList();
    }

    private List<String> getCompatibleDonorGroups(String recipientGroup) {
        String group = normalizeBloodGroup(recipientGroup);
        if (group == null || group.isBlank()) {
            return List.of();
        }

        return switch (group) {
            case "O-" -> List.of("O-");
            case "O+" -> List.of("O+", "O-");
            case "A-" -> List.of("A-", "O-");
            case "A+" -> List.of("A+", "A-", "O+", "O-");
            case "B-" -> List.of("B-", "O-");
            case "B+" -> List.of("B+", "B-", "O+", "O-");
            case "AB-" -> List.of("AB-", "A-", "B-", "O-");
            case "AB+" -> List.of("AB+", "AB-", "A+", "A-", "B+", "B-", "O+", "O-");
            default -> List.of(group);
        };
    }

    private EmergencyAlert createEmergencyAlertFromRequest(BloodRequest request, int needed) {
        EmergencyAlert alert = new EmergencyAlert();
        alert.setBloodGroup(normalizeBloodGroup(request.getPatientBloodGroup()));
        alert.setRequiredUnits(needed);
        alert.setLocation("Auto-generated for request #" + request.getId());
        alert.setStatus(EmergencyStatus.ACTIVE);
        alert.setCoordinator(resolveCoordinator());
        return emergencyAlertRepository.save(alert);
    }

    private User resolveCoordinator() {
        List<User> coordinators = userRepository.findByRole(Role.EMERGENCY_COORDINATOR);
        if (!coordinators.isEmpty()) {
            return coordinators.get(0);
        }

        List<User> bloodBankAdmins = userRepository.findByRole(Role.BLOOD_BANK_ADMIN);
        if (!bloodBankAdmins.isEmpty()) {
            return bloodBankAdmins.get(0);
        }

        List<User> admins = userRepository.findByRole(Role.ADMIN);
        if (!admins.isEmpty()) {
            return admins.get(0);
        }

        return userRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No user available to assign emergency coordinator"));
    }

    private String normalizeBloodGroup(String bloodGroup) {
        return BloodGroupValidator.normalizeAndValidate(bloodGroup);
    }
}