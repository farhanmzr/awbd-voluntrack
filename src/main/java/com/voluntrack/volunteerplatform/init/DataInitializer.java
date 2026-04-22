package com.voluntrack.volunteerplatform.init;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.voluntrack.volunteerplatform.entity.Category;
import com.voluntrack.volunteerplatform.entity.Event;
import com.voluntrack.volunteerplatform.entity.Role;
import com.voluntrack.volunteerplatform.entity.User;
import com.voluntrack.volunteerplatform.entity.Venue;
import com.voluntrack.volunteerplatform.enums.EventStatus;
import com.voluntrack.volunteerplatform.repository.CategoryRepository;
import com.voluntrack.volunteerplatform.repository.EventRepository;
import com.voluntrack.volunteerplatform.repository.RoleRepository;
import com.voluntrack.volunteerplatform.repository.UserRepository;
import com.voluntrack.volunteerplatform.repository.VenueRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final VenueRepository venueRepository;
    private final EventRepository eventRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository,
                           UserRepository userRepository,
                           CategoryRepository categoryRepository,
                           VenueRepository venueRepository,
                           EventRepository eventRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.venueRepository = venueRepository;
        this.eventRepository = eventRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Role adminRole = createRoleIfNotExists("ADMIN");
        Role userRole = createRoleIfNotExists("USER");

        User adminUser = createAdminIfNotExists(adminRole);
        createRegularUserIfNotExists(userRole);

        Category environmentCategory = createCategoryIfNotExists(
                "Environment",
                "Volunteer activities related to environmental protection and sustainability."
        );

        Venue centralParkVenue = createVenueIfNotExists(
                "Central Park",
                "123 Green Street",
                "Bucharest",
                "Public park used for outdoor volunteer events."
        );

        createEventIfNotExists(
                "City Park Cleanup",
                "Join us in cleaning the city park and promoting a healthier environment.",
                LocalDateTime.now().plusDays(7),
                LocalDateTime.now().plusDays(7).plusHours(3),
                20,
                EventStatus.OPEN,
                environmentCategory,
                centralParkVenue,
                adminUser
        );
    }

    private Role createRoleIfNotExists(String roleName) {
        Optional<Role> existingRole = roleRepository.findByName(roleName);

        if (existingRole.isPresent()) {
            return existingRole.get();
        }

        Role role = new Role();
        role.setName(roleName);
        return roleRepository.save(role);
    }

    private User createAdminIfNotExists(Role adminRole) {
        String adminUsername = "admin";
        String adminEmail = "admin@voluntrack.com";

        Optional<User> existingUser = userRepository.findByUsername(adminUsername);
        if (existingUser.isPresent()) {
            return existingUser.get();
        }

        User admin = new User();
        admin.setUsername(adminUsername);
        admin.setEmail(adminEmail);
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setEnabled(true);
        admin.setRole(adminRole);

        return userRepository.save(admin);
    }

    private User createRegularUserIfNotExists(Role userRole) {
        String username = "volunteer1";
        String email = "volunteer1@voluntrack.com";

        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            return existingUser.get();
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("user123"));
        user.setEnabled(true);
        user.setRole(userRole);

        return userRepository.save(user);
    }

    private Category createCategoryIfNotExists(String name, String description) {
        Optional<Category> existingCategory = categoryRepository.findByName(name);

        if (existingCategory.isPresent()) {
            return existingCategory.get();
        }

        Category category = new Category();
        category.setName(name);
        category.setDescription(description);

        return categoryRepository.save(category);
    }

    private Venue createVenueIfNotExists(String name, String address, String city, String description) {
        Optional<Venue> existingVenue = venueRepository.findAll()
                .stream()
                .filter(venue -> venue.getName().equalsIgnoreCase(name))
                .findFirst();

        if (existingVenue.isPresent()) {
            return existingVenue.get();
        }

        Venue venue = new Venue();
        venue.setName(name);
        venue.setAddress(address);
        venue.setCity(city);
        venue.setDescription(description);

        return venueRepository.save(venue);
    }

    private void createEventIfNotExists(String title,
                                        String description,
                                        LocalDateTime startDateTime,
                                        LocalDateTime endDateTime,
                                        Integer capacity,
                                        EventStatus status,
                                        Category category,
                                        Venue venue,
                                        User createdBy) {

        boolean exists = eventRepository.findAll()
                .stream()
                .anyMatch(event -> event.getTitle().equalsIgnoreCase(title));

        if (exists) {
            return;
        }

        Event event = new Event();
        event.setTitle(title);
        event.setDescription(description);
        event.setStartDateTime(startDateTime);
        event.setEndDateTime(endDateTime);
        event.setCapacity(capacity);
        event.setStatus(status);
        event.setCategory(category);
        event.setVenue(venue);
        event.setCreatedBy(createdBy);

        eventRepository.save(event);
    }
}