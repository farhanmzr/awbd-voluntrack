package com.voluntrack.volunteerplatform.service;

import java.util.Optional;

import com.voluntrack.volunteerplatform.entity.User;

public interface UserService {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    User save(User user);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}