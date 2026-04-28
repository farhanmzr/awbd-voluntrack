package com.voluntrack.volunteerplatform.service.impl;

import com.voluntrack.volunteerplatform.entity.Profile;
import com.voluntrack.volunteerplatform.repository.ProfileRepository;
import com.voluntrack.volunteerplatform.service.ProfileService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;

    public ProfileServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public Optional<Profile> findByUserId(Long userId) {
        return profileRepository.findByUserId(userId);
    }

    @Override
    public Profile save(Profile profile) {
        return profileRepository.save(profile);
    }
}