package com.voluntrack.volunteerplatform.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.voluntrack.volunteerplatform.service.CategoryService;
import com.voluntrack.volunteerplatform.service.EventService;

@Controller
public class HomeController {

    private final CategoryService categoryService;
    private final EventService eventService;

    public HomeController(CategoryService categoryService, EventService eventService) {
        this.categoryService = categoryService;
        this.eventService = eventService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("featuredEvents",
                eventService.findFeaturedEvents(3));
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
