package com.bloodbank.bloodbank.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.bloodbank.bloodbank.repository.BloodRequestRepository;
import com.bloodbank.bloodbank.repository.BloodUnitRepository;
import com.bloodbank.bloodbank.repository.DonorNotificationRepository;
import com.bloodbank.bloodbank.repository.DonorRepository;
import com.bloodbank.bloodbank.repository.EmergencyAlertRepository;
import com.bloodbank.bloodbank.repository.TransfusionRepository;
import com.bloodbank.bloodbank.repository.UserRepository;

@Component
@Profile("cleanup")
public class DatabaseCleanupRunner implements CommandLineRunner {

    private final TransfusionRepository transfusionRepository;
    private final BloodUnitRepository bloodUnitRepository;
    private final DonorNotificationRepository donorNotificationRepository;
    private final DonorRepository donorRepository;
    private final EmergencyAlertRepository emergencyAlertRepository;
    private final BloodRequestRepository bloodRequestRepository;
    private final UserRepository userRepository;

    public DatabaseCleanupRunner(
        TransfusionRepository transfusionRepository,
        BloodUnitRepository bloodUnitRepository,
        DonorNotificationRepository donorNotificationRepository,
        DonorRepository donorRepository,
        EmergencyAlertRepository emergencyAlertRepository,
        BloodRequestRepository bloodRequestRepository,
        UserRepository userRepository
    ) {
        this.transfusionRepository = transfusionRepository;
        this.bloodUnitRepository = bloodUnitRepository;
        this.donorNotificationRepository = donorNotificationRepository;
        this.donorRepository = donorRepository;
        this.emergencyAlertRepository = emergencyAlertRepository;
        this.bloodRequestRepository = bloodRequestRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        transfusionRepository.deleteAllInBatch();
        bloodUnitRepository.deleteAllInBatch();
        donorNotificationRepository.deleteAllInBatch();
        donorRepository.deleteAllInBatch();
        emergencyAlertRepository.deleteAllInBatch();
        bloodRequestRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();

        System.out.println("Database cleanup completed.");
    }
}
