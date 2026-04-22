package com.voluntrack.volunteerplatform.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.voluntrack.volunteerplatform.entity.User;
import com.voluntrack.volunteerplatform.service.RegistrationService;
import com.voluntrack.volunteerplatform.service.UserService;

@Controller
@RequestMapping("/registrations")
public class RegistrationController {

    private final RegistrationService registrationService;
    private final UserService userService;

    public RegistrationController(RegistrationService registrationService,
                                  UserService userService) {
        this.registrationService = registrationService;
        this.userService = userService;
    }

    @PostMapping("/join/{eventId}")
    public String joinEvent(@PathVariable Long eventId, Authentication authentication) {
        String username = authentication.getName();

        User user = userService.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Authenticated user not found"));

        registrationService.registerUserToEvent(user.getId(), eventId);

        return "redirect:/registrations/my";
    }

    @GetMapping("/my")
        public String myRegistrations(Authentication authentication,
                              org.springframework.ui.Model model) {
        String username = authentication.getName();

        User user = userService.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("Authenticated user not found"));

        model.addAttribute("registrations", registrationService.findByUserId(user.getId()));

        return "registrations/my-list";
    }
}