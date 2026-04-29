package com.voluntrack.volunteerplatform.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.voluntrack.volunteerplatform.entity.Profile;
import com.voluntrack.volunteerplatform.entity.Skill;
import com.voluntrack.volunteerplatform.entity.User;
import com.voluntrack.volunteerplatform.service.ProfileService;
import com.voluntrack.volunteerplatform.service.SkillService;
import com.voluntrack.volunteerplatform.service.UserService;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;
    private final UserService userService;
    private final SkillService skillService;

    public ProfileController(ProfileService profileService,
            UserService userService,
            SkillService skillService) {
        this.profileService = profileService;
        this.userService = userService;
        this.skillService = skillService;
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
        model.addAttribute("skills", user.getSkills());

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
        model.addAttribute("allSkills", skillService.findAll());
        model.addAttribute("selectedSkills", user.getSkills());

        return "profile/form";
    }

    @PostMapping("/save")
    public String saveProfile(@ModelAttribute Profile profile,
            @RequestParam(required = false) List<Long> skillIds,
            Authentication authentication) {
        String username = authentication.getName();

        User user = userService.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        profile.setUser(user);
        profileService.save(profile);

        Set<Skill> selectedSkills = new HashSet<>();

        if (skillIds != null) {
            for (Long skillId : skillIds) {
                Skill skill = skillService.findById(skillId)
                        .orElseThrow(() -> new IllegalArgumentException("Skill not found with id: " + skillId));
                selectedSkills.add(skill);
            }
        }

        user.setSkills(selectedSkills);
        userService.save(user);

        return "redirect:/profile";
    }
}