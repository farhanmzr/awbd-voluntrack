package com.voluntrack.volunteerplatform.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.voluntrack.volunteerplatform.entity.Venue;
import com.voluntrack.volunteerplatform.repository.VenueRepository;
import com.voluntrack.volunteerplatform.service.VenueService;

@Service
public class VenueServiceImpl implements VenueService {

    private final VenueRepository venueRepository;

    public VenueServiceImpl(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    @Override
    public List<Venue> findAll() {
        return venueRepository.findAll();
    }

    @Override
    public Optional<Venue> findById(Long id) {
        return venueRepository.findById(id);
    }

    @Override
    public Venue save(Venue venue) {
        return venueRepository.save(venue);
    }

    @Override
    public void deleteById(Long id) {
        venueRepository.deleteById(id);
    }
}