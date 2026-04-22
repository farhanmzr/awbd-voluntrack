package com.voluntrack.volunteerplatform.service;

import java.util.Optional;

import com.voluntrack.volunteerplatform.entity.Profile;

public interface ProfileService {
    Optional<Profile> findByUserId(Long userId);

    Profile save(Profile profile);
}