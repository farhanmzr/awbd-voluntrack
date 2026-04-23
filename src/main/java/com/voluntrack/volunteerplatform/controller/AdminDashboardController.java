package com.voluntrack.volunteerplatform.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.voluntrack.volunteerplatform.enums.RegistrationStatus;
import com.voluntrack.volunteerplatform.service.CategoryService;
import com.voluntrack.volunteerplatform.service.EventService;
import com.voluntrack.volunteerplatform.service.RegistrationService;
import com.voluntrack.volunteerplatform.service.VenueService;

@Controller
public class AdminDashboardController {

    private final EventService eventService;
    private final RegistrationService registrationService;
    private final CategoryService categoryService;
    private final VenueService venueService;

    public AdminDashboardController(EventService eventService,
            RegistrationService registrationService,
            CategoryService categoryService,
            VenueService venueService) {
        this.eventService = eventService;
        this.registrationService = registrationService;
        this.categoryService = categoryService;
        this.venueService = venueService;
    }

    @GetMapping("/admin")
    public String adminRoot() {
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        long totalEvents = eventService.findAll(PageRequest.of(0, 1000)).getTotalElements();
        long pendingRegistrations = registrationService
                .findByStatus(RegistrationStatus.PENDING, PageRequest.of(0, 1000))
                .getTotalElements();
        long approvedRegistrations = registrationService
                .findByStatus(RegistrationStatus.APPROVED, PageRequest.of(0, 1000))
                .getTotalElements();

        long totalCategories = categoryService.findAll().size();
        long totalVenues = venueService.findAll().size();

        model.addAttribute("totalEvents", totalEvents);
        model.addAttribute("pendingRegistrations", pendingRegistrations);
        model.addAttribute("approvedRegistrations", approvedRegistrations);
        model.addAttribute("totalCategories", totalCategories);
        model.addAttribute("totalVenues", totalVenues);

        return "admin/dashboard";
    }
}