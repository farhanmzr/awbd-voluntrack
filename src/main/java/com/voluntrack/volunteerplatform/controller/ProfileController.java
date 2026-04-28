package com.voluntrack.volunteerplatform.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.voluntrack.volunteerplatform.entity.Profile;
import com.voluntrack.volunteerplatform.entity.User;
import com.voluntrack.volunteerplatform.service.ProfileService;
import com.voluntrack.volunteerplatform.service.UserService;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;
    private final UserService userService;

    public ProfileController(ProfileService profileService, UserService userService) {
        this.profileService = profileService;
        this.userService = userService;
    }

    @GetMapping
    public String viewProfile(Authentication authentication, Model model) {
        String username = authentication.getName();

        User user = userService.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Profile profile = profileService.findByUserId(user.getId())
                .orElseGet(() -> {
                    Profile newProfile = new Profile();
                    newProfile.setUser(user);
                    return newProfile;
                });

        model.addAttribute("profile", profile);
        return "profile/view";
    }

    @GetMapping("/edit")
    public String editProfile(Authentication authentication, Model model) {
        String username = authentication.getName();

        User user = userService.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Profile profile = profileService.findByUserId(user.getId())
                .orElseGet(() -> {
                    Profile newProfile = new Profile();
                    newProfile.setUser(user);
                    return newProfile;
                });

        model.addAttribute("profile", profile);
        return "profile/form";
    }

    @PostMapping("/save")
    public String saveProfile(@ModelAttribute Profile profile, Authentication authentication) {
        String username = authentication.getName();

        User user = userService.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        profile.setUser(user);

        profileService.save(profile);

        return "redirect:/profile";
    }
}