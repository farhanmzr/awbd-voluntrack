package com.voluntrack.volunteerplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.voluntrack.volunteerplatform.entity.Venue;

public interface VenueRepository extends JpaRepository<Venue, Long> {

}