package com.voluntrack.volunteerplatform.service.impl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.voluntrack.volunteerplatform.entity.Event;
import com.voluntrack.volunteerplatform.enums.EventStatus;
import com.voluntrack.volunteerplatform.repository.EventRepository;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventServiceImpl eventService;

    @Test
    void findAll_shouldReturnPagedEvents() {
        Pageable pageable = PageRequest.of(0, 5);
        Event event = new Event();
        event.setTitle("City Park Cleanup");

        Page<Event> eventPage = new PageImpl<>(List.of(event));

        when(eventRepository.findAll(pageable)).thenReturn(eventPage);

        Page<Event> result = eventService.findAll(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("City Park Cleanup", result.getContent().get(0).getTitle());
        verify(eventRepository, times(1)).findAll(pageable);
    }

    @Test
    void findById_shouldReturnEventWhenExists() {
        Event event = new Event();
        event.setId(1L);
        event.setTitle("Cleanup Event");

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        Optional<Event> result = eventService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Cleanup Event", result.get().getTitle());
        verify(eventRepository, times(1)).findById(1L);
    }

    @Test
    void save_shouldReturnSavedEvent() {
        Event event = new Event();
        event.setTitle("Tree Planting");

        when(eventRepository.save(event)).thenReturn(event);

        Event result = eventService.save(event);

        assertNotNull(result);
        assertEquals("Tree Planting", result.getTitle());
        verify(eventRepository, times(1)).save(event);
    }

    @Test
    void findByStatus_shouldReturnFilteredEvents() {
        Pageable pageable = PageRequest.of(0, 5);
        Event event = new Event();
        event.setStatus(EventStatus.OPEN);

        Page<Event> eventPage = new PageImpl<>(List.of(event));

        when(eventRepository.findByStatus(EventStatus.OPEN, pageable)).thenReturn(eventPage);

        Page<Event> result = eventService.findByStatus(EventStatus.OPEN, pageable);

        assertEquals(1, result.getTotalElements());
        verify(eventRepository, times(1)).findByStatus(EventStatus.OPEN, pageable);
    }
}