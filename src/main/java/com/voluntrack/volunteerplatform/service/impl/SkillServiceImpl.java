package com.voluntrack.volunteerplatform.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.voluntrack.volunteerplatform.entity.Skill;
import com.voluntrack.volunteerplatform.repository.SkillRepository;
import com.voluntrack.volunteerplatform.service.SkillService;

@Service
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;

    public SkillServiceImpl(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @Override
    public List<Skill> findAll() {
        return skillRepository.findAll();
    }

    @Override
    public Optional<Skill> findById(Long id) {
        return skillRepository.findById(id);
    }

    @Override
    public Skill save(Skill skill) {
        return skillRepository.save(skill);
    }

    @Override
    public void deleteById(Long id) {
        skillRepository.deleteById(id);
    }
}