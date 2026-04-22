package com.voluntrack.volunteerplatform.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.voluntrack.volunteerplatform.entity.Category;
import com.voluntrack.volunteerplatform.service.CategoryService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final CategoryService categoryService;

    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "admin/categories/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("formAction", "/admin/categories/save");
        return "admin/categories/form";
    }

    @PostMapping("/save")
    public String saveCategory(@Valid @ModelAttribute("category") Category category,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("formAction", "/admin/categories/save");
            return "admin/categories/form";
        }

        categoryService.save(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Category category = categoryService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));

        model.addAttribute("category", category);
        model.addAttribute("formAction", "/admin/categories/save");
        return "admin/categories/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
        return "redirect:/admin/categories";
    }
}