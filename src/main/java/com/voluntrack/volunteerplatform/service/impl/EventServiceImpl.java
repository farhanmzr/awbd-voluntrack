package com.voluntrack.volunteerplatform.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.voluntrack.volunteerplatform.entity.Event;
import com.voluntrack.volunteerplatform.enums.EventStatus;
import com.voluntrack.volunteerplatform.repository.EventRepository;
import com.voluntrack.volunteerplatform.service.EventService;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Page<Event> findAll(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }

    @Override
    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id);
    }

    @Override
    public Event save(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public void deleteById(Long id) {
        eventRepository.deleteById(id);
    }

    @Override
    public Page<Event> findByStatus(EventStatus status, Pageable pageable) {
        return eventRepository.findByStatus(status, pageable);
    }

    @Override
    public Page<Event> findByCategoryId(Long categoryId, Pageable pageable) {
        return eventRepository.findByCategoryId(categoryId, pageable);
    }

    @Override
    public Page<Event> searchByTitle(String keyword, Pageable pageable) {
        return eventRepository.findByTitleContainingIgnoreCase(keyword, pageable);
    }

    @Override
    public Page<Event> searchAndFilter(String keyword, Long categoryId, Pageable pageable) {
        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
        boolean hasCategory = categoryId != null;

        if (hasKeyword && hasCategory) {
            return eventRepository.findByStatusInAndTitleContainingIgnoreCaseAndCategoryId(
                    PUBLIC_STATUSES,
                    keyword.trim(),
                    categoryId,
                    pageable);
        }

        if (hasKeyword) {
            return eventRepository.findByStatusInAndTitleContainingIgnoreCase(
                    PUBLIC_STATUSES,
                    keyword.trim(),
                    pageable);
        }

        if (hasCategory) {
            return eventRepository.findByStatusInAndCategoryId(
                    PUBLIC_STATUSES,
                    categoryId,
                    pageable);
        }

        return eventRepository.findByStatusIn(PUBLIC_STATUSES, pageable);
    }

    @Override
    public List<Event> findFeaturedEvents(int limit) {
        Pageable pageable = PageRequest.of(0, limit,
                Sort.by("startDateTime").ascending());

        return eventRepository.findByStatusIn(
                List.of(
                        EventStatus.OPEN,
                        EventStatus.FULL,
                        EventStatus.CLOSED,
                        EventStatus.COMPLETED),
                pageable).getContent();
    }

    private final List<EventStatus> PUBLIC_STATUSES = List.of(
            EventStatus.OPEN,
            EventStatus.FULL,
            EventStatus.CLOSED,
            EventStatus.COMPLETED);
}