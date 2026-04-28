package com.voluntrack.volunteerplatform.service.impl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

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

@ExtendWith(MockitoExtension.class)
class RegistrationServiceImplTest {

    @Mock
    private RegistrationRepository registrationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    @Test
    void registerUserToEvent_shouldCreatePendingRegistration_whenValid() {
        User user = new User();
        user.setId(1L);

        Event event = new Event();
        event.setId(1L);
        event.setStatus(EventStatus.OPEN);
        event.setCapacity(10);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(registrationRepository.findByUserIdAndEventId(1L, 1L)).thenReturn(Optional.empty());
        when(registrationRepository.findByEventId(1L)).thenReturn(List.of());
        when(registrationRepository.save(any(Registration.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Registration result = registrationService.registerUserToEvent(1L, 1L);

        assertNotNull(result);
        assertEquals(RegistrationStatus.PENDING, result.getStatus());
        assertEquals(user, result.getUser());
        assertEquals(event, result.getEvent());
        verify(registrationRepository, times(1)).save(any(Registration.class));
    }

    @Test
    void registerUserToEvent_shouldThrowDuplicateRegistrationException_whenAlreadyRegistered() {
        User user = new User();
        user.setId(1L);

        Event event = new Event();
        event.setId(1L);
        event.setStatus(EventStatus.OPEN);
        event.setCapacity(10);

        Registration existingRegistration = new Registration();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(registrationRepository.findByUserIdAndEventId(1L, 1L)).thenReturn(Optional.of(existingRegistration));

        assertThrows(DuplicateRegistrationException.class,
                () -> registrationService.registerUserToEvent(1L, 1L));
    }

    @Test
    void registerUserToEvent_shouldThrowEventRegistrationException_whenEventNotOpen() {
        User user = new User();
        user.setId(1L);

        Event event = new Event();
        event.setId(1L);
        event.setStatus(EventStatus.CLOSED);
        event.setCapacity(10);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(registrationRepository.findByUserIdAndEventId(1L, 1L)).thenReturn(Optional.empty());

        assertThrows(EventRegistrationException.class,
                () -> registrationService.registerUserToEvent(1L, 1L));
    }

    @Test
    void registerUserToEvent_shouldThrowEventRegistrationException_whenEventIsFull() {
        User user = new User();
        user.setId(1L);

        Event event = new Event();
        event.setId(1L);
        event.setStatus(EventStatus.OPEN);
        event.setCapacity(1);

        Registration approvedRegistration = new Registration();
        approvedRegistration.setStatus(RegistrationStatus.APPROVED);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(registrationRepository.findByUserIdAndEventId(1L, 1L)).thenReturn(Optional.empty());
        when(registrationRepository.findByEventId(1L)).thenReturn(List.of(approvedRegistration));

        assertThrows(EventRegistrationException.class,
                () -> registrationService.registerUserToEvent(1L, 1L));
    }

    @Test
    void approveRegistration_shouldUpdateStatusToApproved() {
        Registration registration = new Registration();
        registration.setId(1L);
        registration.setStatus(RegistrationStatus.PENDING);

        when(registrationRepository.findById(1L)).thenReturn(Optional.of(registration));
        when(registrationRepository.save(registration)).thenReturn(registration);

        Registration result = registrationService.approveRegistration(1L);

        assertEquals(RegistrationStatus.APPROVED, result.getStatus());
        verify(registrationRepository, times(1)).save(registration);
    }

    @Test
    void cancelRegistration_shouldUpdateStatusToCancelled() {
        Registration registration = new Registration();
        registration.setId(1L);
        registration.setStatus(RegistrationStatus.PENDING);

        when(registrationRepository.findById(1L)).thenReturn(Optional.of(registration));
        when(registrationRepository.save(registration)).thenReturn(registration);

        Registration result = registrationService.cancelRegistration(1L);

        assertEquals(RegistrationStatus.CANCELLED, result.getStatus());
        verify(registrationRepository, times(1)).save(registration);
    }

    @Test
    void approveRegistration_shouldThrowResourceNotFoundException_whenRegistrationMissing() {
        when(registrationRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> registrationService.approveRegistration(999L));
    }
}