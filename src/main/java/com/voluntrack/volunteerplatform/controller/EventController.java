package com.voluntrack.volunteerplatform.controller;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.voluntrack.volunteerplatform.entity.Event;
import com.voluntrack.volunteerplatform.entity.User;
import com.voluntrack.volunteerplatform.service.CategoryService;
import com.voluntrack.volunteerplatform.service.EventService;
import com.voluntrack.volunteerplatform.service.RegistrationService;
import com.voluntrack.volunteerplatform.service.UserService;

@Controller
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final RegistrationService registrationService;

    public EventController(EventService eventService,
            CategoryService categoryService,
            UserService userService,
            RegistrationService registrationService) {
        this.eventService = eventService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.registrationService = registrationService;
    }

    @GetMapping
    public String listEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "startDateTime") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            Model model) {
        org.springframework.data.domain.Sort sort = sortDir.equalsIgnoreCase("asc")
                ? org.springframework.data.domain.Sort.by(sortField).ascending()
                : org.springframework.data.domain.Sort.by(sortField).descending();

        Page<Event> eventPage = eventService.searchAndFilter(
                keyword,
                categoryId,
                org.springframework.data.domain.PageRequest.of(page, size, sort));

        model.addAttribute("eventPage", eventPage);
        model.addAttribute("events", eventPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", eventPage.getTotalPages());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("categories", categoryService.findAll());

        return "events/list";
    }

    @GetMapping("/{id}")
    public String eventDetail(@PathVariable Long id,
            Model model,
            Authentication authentication) {
        Event event = eventService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with id: " + id));

        boolean alreadyRegistered = false;

        if (authentication != null && authentication.isAuthenticated()
                && authentication.getAuthorities().stream()
                        .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER"))) {

            User user = userService.findByUsername(authentication.getName())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            alreadyRegistered = registrationService.hasUserRegisteredForEvent(user.getId(), event.getId());
        }

        model.addAttribute("event", event);
        model.addAttribute("alreadyRegistered", alreadyRegistered);

        return "events/detail";
    }
}