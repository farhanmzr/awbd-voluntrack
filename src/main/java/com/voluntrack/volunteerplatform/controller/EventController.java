package com.voluntrack.volunteerplatform.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.voluntrack.volunteerplatform.entity.Event;
import com.voluntrack.volunteerplatform.service.EventService;

@Controller
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
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

        Page<Event> eventPage = eventService.findAll(
                org.springframework.data.domain.PageRequest.of(page, size, sort));

        model.addAttribute("eventPage", eventPage);
        model.addAttribute("events", eventPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", eventPage.getTotalPages());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        return "events/list";
    }

    @GetMapping("/{id}")
    public String eventDetail(@PathVariable Long id, Model model) {
        Event event = eventService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with id: " + id));

        model.addAttribute("event", event);
        return "events/detail";
    }
}