package com.voluntrack.volunteerplatform.service;

import java.util.List;
import java.util.Optional;

import com.voluntrack.volunteerplatform.entity.Venue;

public interface VenueService {
    List<Venue> findAll();

    Optional<Venue> findById(Long id);

    Venue save(Venue venue);

    void deleteById(Long id);
}