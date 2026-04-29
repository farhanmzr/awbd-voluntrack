package com.voluntrack.volunteerplatform.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.voluntrack.volunteerplatform.entity.Skill;
import com.voluntrack.volunteerplatform.service.SkillService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/skills")
public class AdminSkillController {

    private final SkillService skillService;

    public AdminSkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping
    public String listSkills(Model model) {
        model.addAttribute("skills", skillService.findAll());
        return "admin/skills/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("skill", new Skill());
        model.addAttribute("formAction", "/admin/skills/save");
        return "admin/skills/form";
    }

    @PostMapping("/save")
    public String saveSkill(@Valid @ModelAttribute("skill") Skill skill,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("formAction", "/admin/skills/save");
            return "admin/skills/form";
        }

        try {
            skillService.save(skill);
        } catch (DataIntegrityViolationException e) {
            bindingResult.rejectValue("name", "error.name", "Skill name already exists.");
            model.addAttribute("formAction", "/admin/skills/save");
            return "admin/skills/form";
        }

        return "redirect:/admin/skills?saved=true";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Skill skill = skillService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Skill not found with id: " + id));

        model.addAttribute("skill", skill);
        model.addAttribute("formAction", "/admin/skills/save");

        return "admin/skills/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteSkill(@PathVariable Long id) {
        try {
            skillService.deleteById(id);
            return "redirect:/admin/skills?deleted=true";
        } catch (Exception e) {
            return "redirect:/admin/skills?deleteError=true";
        }
    }
}