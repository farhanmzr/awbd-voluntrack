package com.voluntrack.volunteerplatform.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.voluntrack.volunteerplatform.entity.Venue;
import com.voluntrack.volunteerplatform.service.VenueService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/venues")
public class AdminVenueController {

    private final VenueService venueService;

    public AdminVenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    @GetMapping
    public String listVenues(Model model) {
        model.addAttribute("venues", venueService.findAll());
        return "admin/venues/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("venue", new Venue());
        model.addAttribute("formAction", "/admin/venues/save");
        return "admin/venues/form";
    }

    @PostMapping("/save")
    public String saveVenue(@Valid @ModelAttribute("venue") Venue venue,
                        BindingResult bindingResult,
                        Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("formAction", "/admin/venues/save");
            return "admin/venues/form";
        }

        venueService.save(venue);
        return "redirect:/admin/venues";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Venue venue = venueService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Venue not found with id: " + id));

        model.addAttribute("venue", venue);
        model.addAttribute("formAction", "/admin/venues/save");
        return "admin/venues/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteVenue(@PathVariable Long id) {
        venueService.deleteById(id);
        return "redirect:/admin/venues";
    }
}