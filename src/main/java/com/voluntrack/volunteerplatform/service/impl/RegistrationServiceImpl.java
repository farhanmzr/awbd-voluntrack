package com.voluntrack.volunteerplatform.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.voluntrack.volunteerplatform.entity.Event;
import com.voluntrack.volunteerplatform.entity.Registration;
import com.voluntrack.volunteerplatform.entity.User;
import com.voluntrack.volunteerplatform.enums.EventStatus;
import com.voluntrack.volunteerplatform.enums.RegistrationStatus;
import com.voluntrack.volunteerplatform.exception.DuplicateRegistrationException;
import com.voluntrack.volunteerplatform.exception.EventRegistrationException;
import com.voluntrack.volunteerplatform.exception.ResourceNotFoundException;
import com.voluntrack.volunteerplatform.repository.EventRepository;
import com.voluntrack.volunteerplatform.repository.RegistrationRepository;
import com.voluntrack.volunteerplatform.repository.UserRepository;
import com.voluntrack.volunteerplatform.service.RegistrationService;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public RegistrationServiceImpl(RegistrationRepository registrationRepository,
                                   UserRepository userRepository,
                                   EventRepository eventRepository) {
        this.registrationRepository = registrationRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public Registration registerUserToEvent(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));

        if (registrationRepository.findByUserIdAndEventId(userId, eventId).isPresent()) {
            throw new DuplicateRegistrationException("User has already registered for this event.");
        }

        if (event.getStatus() != EventStatus.OPEN) {
            throw new EventRegistrationException("This event is not open for registration.");
        }

        long approvedOrPendingCount = registrationRepository.findByEventId(eventId).stream()
                .filter(reg -> reg.getStatus() == RegistrationStatus.PENDING
                        || reg.getStatus() == RegistrationStatus.APPROVED)
                .count();

        if (approvedOrPendingCount >= event.getCapacity()) {
            throw new EventRegistrationException("This event is already full.");
        }

        Registration registration = new Registration();
        registration.setUser(user);
        registration.setEvent(event);
        registration.setStatus(RegistrationStatus.PENDING);

        return registrationRepository.save(registration);
    }

    @Override
    public Registration cancelRegistration(Long registrationId) {
        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new ResourceNotFoundException("Registration not found with id: " + registrationId));

        registration.setStatus(RegistrationStatus.CANCELLED);
        return registrationRepository.save(registration);
    }

    @Override
    public Registration approveRegistration(Long registrationId) {
        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new ResourceNotFoundException("Registration not found with id: " + registrationId));

        registration.setStatus(RegistrationStatus.APPROVED);
        return registrationRepository.save(registration);
    }

    @Override
    public Registration rejectRegistration(Long registrationId) {
        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new ResourceNotFoundException("Registration not found with id: " + registrationId));

        registration.setStatus(RegistrationStatus.REJECTED);
        return registrationRepository.save(registration);
    }

    @Override
    public Optional<Registration> findByUserIdAndEventId(Long userId, Long eventId) {
        return registrationRepository.findByUserIdAndEventId(userId, eventId);
    }

    @Override
    public List<Registration> findByEventId(Long eventId) {
        return registrationRepository.findByEventId(eventId);
    }

    @Override
    public List<Registration> findByUserId(Long userId) {
        return registrationRepository.findByUserId(userId);
    }

    @Override
    public Page<Registration> findByStatus(RegistrationStatus status, Pageable pageable) {
        return registrationRepository.findByStatus(status, pageable);
    }
}