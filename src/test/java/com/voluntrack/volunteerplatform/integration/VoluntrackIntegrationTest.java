package com.voluntrack.volunteerplatform.integration;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class VoluntrackIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void publicEventsPage_shouldBeAccessibleWithoutLogin() throws Exception {
        mockMvc.perform(get("/events"))
                .andExpect(status().isOk())
                .andExpect(view().name("events/list"));
    }

    @Test
    void registerUser_shouldRedirectToLoginWithRegisteredParam() throws Exception {
        String unique = UUID.randomUUID().toString().substring(0, 8);

        mockMvc.perform(post("/register")
                .with(csrf())
                .param("username", "testuser_" + unique)
                .param("email", "testuser_" + unique + "@example.com")
                .param("password", "password123")
                .param("confirmPassword", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?registered"));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void adminDashboard_shouldBeAccessibleForAdmin() throws Exception {
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/dashboard"))
                .andExpect(model().attributeExists("totalEvents"))
                .andExpect(model().attributeExists("pendingRegistrations"))
                .andExpect(model().attributeExists("approvedRegistrations"));
    }
}