package com.voluntrack.volunteerplatform.service;

import java.util.List;
import java.util.Optional;

import com.voluntrack.volunteerplatform.entity.Skill;

public interface SkillService {
    List<Skill> findAll();

    Optional<Skill> findById(Long id);

    Skill save(Skill skill);

    void deleteById(Long id);
}