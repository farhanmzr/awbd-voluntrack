package com.voluntrack.volunteerplatform.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.voluntrack.volunteerplatform.entity.Registration;
import com.voluntrack.volunteerplatform.enums.RegistrationStatus;

public interface RegistrationService {
    Registration registerUserToEvent(Long userId, Long eventId);

    Registration cancelRegistration(Long registrationId);

    Registration approveRegistration(Long registrationId);

    Registration rejectRegistration(Long registrationId);

    Optional<Registration> findByUserIdAndEventId(Long userId, Long eventId);

    List<Registration> findByEventId(Long eventId);

    List<Registration> findByUserId(Long userId);

    Page<Registration> findByStatus(RegistrationStatus status, Pageable pageable);
}