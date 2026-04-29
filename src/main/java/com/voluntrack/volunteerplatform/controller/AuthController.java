package com.voluntrack.volunteerplatform.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.voluntrack.volunteerplatform.dto.RegisterRequest;
import com.voluntrack.volunteerplatform.entity.Role;
import com.voluntrack.volunteerplatform.entity.User;
import com.voluntrack.volunteerplatform.repository.RoleRepository;
import com.voluntrack.volunteerplatform.service.UserService;

import jakarta.validation.Valid;

@Controller
public class AuthController {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(UserService userService,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("registerRequest") RegisterRequest registerRequest,
            BindingResult bindingResult,
            Model model) {

        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "Passwords do not match.");
        }

        if (userService.existsByUsername(registerRequest.getUsername())) {
            bindingResult.rejectValue("username", "error.username", "Username is already taken.");
            logger.warn("Registration failed: username already taken - {}", registerRequest.getUsername());
        }

        if (userService.existsByEmail(registerRequest.getEmail())) {
            bindingResult.rejectValue("email", "error.email", "Email is already registered.");
            logger.warn("Registration failed: email already registered - {}", registerRequest.getEmail());
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalArgumentException("USER role not found."));

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEnabled(true);
        user.setRole(userRole);

        userService.save(user);

        logger.info("New user registered with username: {}", user.getUsername());

        return "redirect:/login?registered";
    }
}