package com.pm.patientservice.domain.service;

import com.pm.patientservice.infrastructure.exception.EmailAlreadyExistsException;
import org.springframework.stereotype.Service;

@Service
public class PatientDomainService {
    public void ensureEmailNotExists(boolean exists, String email) {
        if (exists) {
            throw new EmailAlreadyExistsException(
                    "Email already exists: " + email);
        }
    }
}
