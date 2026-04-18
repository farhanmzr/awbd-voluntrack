package com.voluntrack.volunteerplatform.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.voluntrack.volunteerplatform.entity.Registration;
import com.voluntrack.volunteerplatform.enums.RegistrationStatus;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    Optional<Registration> findByUserIdAndEventId(Long userId, Long eventId);

    List<Registration> findByEventId(Long eventId);

    List<Registration> findByUserId(Long userId);

    Page<Registration> findByStatus(RegistrationStatus status, Pageable pageable);
}