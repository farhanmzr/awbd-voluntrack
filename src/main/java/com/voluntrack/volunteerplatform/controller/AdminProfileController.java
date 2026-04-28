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
@RequestMapping("/admin/profile")
public class AdminProfileController {

    private final ProfileService profileService;
    private final UserService userService;

    public AdminProfileController(ProfileService profileService, UserService userService) {
        this.profileService = profileService;
        this.userService = userService;
    }

    @GetMapping
    public String viewAdminProfile(Authentication authentication, Model model) {
        User user = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Admin user not found"));

        Profile profile = profileService.findByUserId(user.getId())
                .orElseGet(() -> {
                    Profile newProfile = new Profile();
                    newProfile.setUser(user);
                    return newProfile;
                });

        model.addAttribute("profile", profile);
        return "admin/profile/view";
    }

    @GetMapping("/edit")
    public String editAdminProfile(Authentication authentication, Model model) {
        User user = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Admin user not found"));

        Profile profile = profileService.findByUserId(user.getId())
                .orElseGet(() -> {
                    Profile newProfile = new Profile();
                    newProfile.setUser(user);
                    return newProfile;
                });

        model.addAttribute("profile", profile);
        return "admin/profile/form";
    }

    @PostMapping("/save")
    public String saveAdminProfile(@ModelAttribute Profile profile, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Admin user not found"));

        profile.setUser(user);
        profileService.save(profile);

        return "redirect:/admin/profile";
    }
}