package com.voluntrack.volunteerplatform.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.voluntrack.volunteerplatform.entity.Event;
import com.voluntrack.volunteerplatform.enums.EventStatus;

public interface EventService {
    Page<Event> findAll(Pageable pageable);

    Optional<Event> findById(Long id);

    Event save(Event event);

    void deleteById(Long id);

    Page<Event> findByStatus(EventStatus status, Pageable pageable);

    Page<Event> findByCategoryId(Long categoryId, Pageable pageable);

    Page<Event> searchByTitle(String keyword, Pageable pageable);
}