package com.voluntrack.volunteerplatform.service;

import java.util.List;
import java.util.Optional;

import com.voluntrack.volunteerplatform.entity.Category;

public interface CategoryService {
    List<Category> findAll();

    Optional<Category> findById(Long id);

    Category save(Category category);

    void deleteById(Long id);
}