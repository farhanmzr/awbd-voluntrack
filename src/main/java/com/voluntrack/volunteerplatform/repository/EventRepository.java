package com.voluntrack.volunteerplatform.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.voluntrack.volunteerplatform.entity.Event;
import com.voluntrack.volunteerplatform.enums.EventStatus;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findByStatus(EventStatus status, Pageable pageable);

    Page<Event> findByCategoryId(Long categoryId, Pageable pageable);

    Page<Event> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
    
    Page<Event> findByTitleContainingIgnoreCaseAndCategoryId(String keyword, Long categoryId, Pageable pageable);

    List<Event> findByStatusIn(List<EventStatus> statuses);

    Page<Event> findByStatusInAndTitleContainingIgnoreCaseAndCategoryId(
            List<EventStatus> statuses,
            String keyword,
            Long categoryId,
            Pageable pageable);

    Page<Event> findByStatusInAndTitleContainingIgnoreCase(
            List<EventStatus> statuses,
            String keyword,
            Pageable pageable);

    Page<Event> findByStatusInAndCategoryId(
            List<EventStatus> statuses,
            Long categoryId,
            Pageable pageable);

    Page<Event> findByStatusIn(
            List<EventStatus> statuses,
            Pageable pageable);
            
}