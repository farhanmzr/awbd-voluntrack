package com.voluntrack.volunteerplatform.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.voluntrack.volunteerplatform.entity.Event;
import com.voluntrack.volunteerplatform.enums.EventStatus;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findByStatus(EventStatus status, Pageable pageable);

    Page<Event> findByCategoryId(Long categoryId, Pageable pageable);

    Page<Event> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
}