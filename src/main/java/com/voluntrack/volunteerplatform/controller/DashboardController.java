package com.voluntrack.volunteerplatform.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.voluntrack.volunteerplatform.entity.User;
import com.voluntrack.volunteerplatform.service.RegistrationService;
import com.voluntrack.volunteerplatform.service.UserService;

@Controller
public class DashboardController {

    private final UserService userService;
    private final RegistrationService registrationService;

    public DashboardController(UserService userService,
            RegistrationService registrationService) {
        this.userService = userService;
        this.registrationService = registrationService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        String username = authentication.getName();

        User user = userService.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        model.addAttribute("username", user.getUsername());
        model.addAttribute("registrations", registrationService.findByUserId(user.getId()));

        return "dashboard";
    }
}