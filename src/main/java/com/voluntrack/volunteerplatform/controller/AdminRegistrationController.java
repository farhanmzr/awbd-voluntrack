package com.voluntrack.volunteerplatform.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.voluntrack.volunteerplatform.enums.RegistrationStatus;
import com.voluntrack.volunteerplatform.service.RegistrationService;

@Controller
@RequestMapping("/admin/registrations")
public class AdminRegistrationController {

    private final RegistrationService registrationService;

    public AdminRegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping
    public String listRegistrations(
            @RequestParam(defaultValue = "PENDING") RegistrationStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "registrationDate") String sortField,
            @RequestParam(defaultValue = "desc") String sortDir,
            Model model) {
        org.springframework.data.domain.Sort sort = sortDir.equalsIgnoreCase("asc")
                ? org.springframework.data.domain.Sort.by(sortField).ascending()
                : org.springframework.data.domain.Sort.by(sortField).descending();

        Page<com.voluntrack.volunteerplatform.entity.Registration> registrationPage = registrationService.findByStatus(
                status,
                org.springframework.data.domain.PageRequest.of(page, size, sort));

        model.addAttribute("registrationPage", registrationPage);
        model.addAttribute("registrations", registrationPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", registrationPage.getTotalPages());
        model.addAttribute("selectedStatus", status);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);

        return "admin/registrations/list";
    }

    @PostMapping("/{id}/approve")
    public String approveRegistration(@PathVariable Long id) {
        registrationService.approveRegistration(id);
        return "redirect:/admin/registrations";
    }

    @PostMapping("/{id}/reject")
    public String rejectRegistration(@PathVariable Long id) {
        registrationService.rejectRegistration(id);
        return "redirect:/admin/registrations";
    }
}