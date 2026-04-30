package com.voluntrack.volunteerplatform.controller;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.voluntrack.volunteerplatform.entity.Event;
import com.voluntrack.volunteerplatform.entity.User;
import com.voluntrack.volunteerplatform.enums.EventStatus;
import com.voluntrack.volunteerplatform.repository.RegistrationRepository;
import com.voluntrack.volunteerplatform.service.CategoryService;
import com.voluntrack.volunteerplatform.service.EventService;
import com.voluntrack.volunteerplatform.service.UserService;
import com.voluntrack.volunteerplatform.service.VenueService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/events")
public class AdminEventController {

    private final EventService eventService;
    private final CategoryService categoryService;
    private final VenueService venueService;
    private final UserService userService;
    private final RegistrationRepository registrationRepository;

    private static final Logger logger = LoggerFactory.getLogger(AdminEventController.class);

    public AdminEventController(EventService eventService,
            CategoryService categoryService,
            VenueService venueService,
            UserService userService,
            RegistrationRepository registrationRepository) {
        this.eventService = eventService;
        this.categoryService = categoryService;
        this.venueService = venueService;
        this.userService = userService;
        this.registrationRepository = registrationRepository;
    }

    @GetMapping
    public String listEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "startDateTime") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model) {
        org.springframework.data.domain.Sort sort = sortDir.equalsIgnoreCase("asc")
                ? org.springframework.data.domain.Sort.by(sortField).ascending()
                : org.springframework.data.domain.Sort.by(sortField).descending();

        org.springframework.data.domain.Page<Event> eventPage = eventService.findAll(
                org.springframework.data.domain.PageRequest.of(page, size, sort));

        model.addAttribute("eventPage", eventPage);
        model.addAttribute("events", eventPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", eventPage.getTotalPages());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);

        return "admin/events/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("event", new Event());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("venues", venueService.findAll());
        model.addAttribute("statuses", EventStatus.values());
        model.addAttribute("formAction", "/admin/events/save");
        return "admin/events/form";
    }

    @PostMapping("/save")
    public String saveEvent(@Valid @ModelAttribute("event") Event event,
            BindingResult bindingResult,
            @RequestParam Long categoryId,
            @RequestParam Long venueId,
            Authentication authentication,
            Model model) {

        LocalDateTime now = LocalDateTime.now();

        if (event.getEndDateTime() != null && event.getEndDateTime().isBefore(now)) {
            bindingResult.rejectValue(
                    "endDateTime",
                    "event.endDateTime.past",
                    "End date cannot be in the past.");
        }

        if (event.getStartDateTime() != null
                && event.getEndDateTime() != null
                && event.getEndDateTime().isBefore(event.getStartDateTime())) {
            bindingResult.rejectValue(
                    "endDateTime",
                    "event.endDateTime.beforeStart",
                    "End date cannot be before start date.");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("venues", venueService.findAll());
            model.addAttribute("statuses", EventStatus.values());
            model.addAttribute("formAction", "/admin/events/save");
            return "admin/events/form";
        }

        String username = authentication.getName();

        User adminUser = userService.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Authenticated user not found"));

        Event eventToSave;

        if (event.getId() != null) {
            Event existingEvent = eventService.findById(event.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Event not found with id: " + event.getId()));

            existingEvent.setTitle(event.getTitle());
            existingEvent.setDescription(event.getDescription());
            existingEvent.setStartDateTime(event.getStartDateTime());
            existingEvent.setEndDateTime(event.getEndDateTime());
            existingEvent.setCapacity(event.getCapacity());
            existingEvent.setStatus(event.getStatus());
            existingEvent.setCategory(categoryService.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Category not found")));
            existingEvent.setVenue(venueService.findById(venueId)
                    .orElseThrow(() -> new IllegalArgumentException("Venue not found")));

            eventToSave = existingEvent;
        } else {

            event.setCreatedBy(adminUser);
            event.setCategory(categoryService.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Category not found")));
            event.setVenue(venueService.findById(venueId)
                    .orElseThrow(() -> new IllegalArgumentException("Venue not found")));

            eventToSave = event;
        }

        eventService.save(eventToSave);

        logger.info("Admin {} saved event with title: {}", authentication.getName(), eventToSave.getTitle());

        return "redirect:/admin/events";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Event event = eventService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with id: " + id));

        model.addAttribute("event", event);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("venues", venueService.findAll());
        model.addAttribute("statuses", EventStatus.values());
        model.addAttribute("formAction", "/admin/events/save");

        return "admin/events/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Long id, Model model) {

        if (registrationRepository.existsByEventId(id)) {
            logger.warn("Delete event blocked because event id {} has registrations", id);
            return "redirect:/admin/events?deleteError=true";
        }

        eventService.deleteById(id);
        logger.info("Event with id {} deleted successfully", id);
        return "redirect:/admin/events?deleted=true";
    }
}