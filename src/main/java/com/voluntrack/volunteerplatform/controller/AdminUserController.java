package com.voluntrack.volunteerplatform.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.voluntrack.volunteerplatform.entity.User;
import com.voluntrack.volunteerplatform.service.UserService;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAllUsersOnly());
        return "admin/users/list";
    }

    @PostMapping("/{id}/toggle-status")
    public String toggleUserStatus(@PathVariable Long id, Authentication authentication) {
        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        if (user.getUsername().equals(authentication.getName())) {
            return "redirect:/admin/users?selfActionError=true";
        }

        user.setEnabled(!user.getEnabled());
        userService.save(user);

        return "redirect:/admin/users?statusUpdated=true";
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        if (!user.getRole().getName().equals("USER")) {
            return "redirect:/admin/users?invalidAction=true";
        }

        try {
            userService.deleteById(id);
            return "redirect:/admin/users?deleted=true";
        } catch (Exception e) {
            return "redirect:/admin/users?deleteError=true";
        }
    }
}