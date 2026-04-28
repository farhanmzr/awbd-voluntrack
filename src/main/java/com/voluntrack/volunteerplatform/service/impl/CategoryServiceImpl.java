package com.voluntrack.volunteerplatform.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.voluntrack.volunteerplatform.entity.Category;
import com.voluntrack.volunteerplatform.repository.CategoryRepository;
import com.voluntrack.volunteerplatform.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}