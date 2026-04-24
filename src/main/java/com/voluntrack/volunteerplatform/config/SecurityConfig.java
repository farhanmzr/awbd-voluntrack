package com.voluntrack.volunteerplatform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.voluntrack.volunteerplatform.security.CustomLoginSuccessHandler;
import com.voluntrack.volunteerplatform.security.CustomUserDetailsService;

@Configuration
public class SecurityConfig {

        private final CustomUserDetailsService customUserDetailsService;
        private final PasswordEncoder passwordEncoder;
        private final CustomLoginSuccessHandler customLoginSuccessHandler;

        public SecurityConfig(CustomUserDetailsService customUserDetailsService,
                        PasswordEncoder passwordEncoder,
                        CustomLoginSuccessHandler customLoginSuccessHandler) {
                this.customUserDetailsService = customUserDetailsService;
                this.passwordEncoder = passwordEncoder;
                this.customLoginSuccessHandler = customLoginSuccessHandler;
        }

        @Bean
        public DaoAuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(customUserDetailsService);
                authProvider.setPasswordEncoder(passwordEncoder);
                return authProvider;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/", "/login", "/register", "/css/**", "/js/**",
                                                                "/images/**")
                                                .permitAll()
                                                .requestMatchers("/h2-console/**").permitAll()
                                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                                .requestMatchers("/dashboard", "/profile/**", "/registrations/**")
                                                .hasRole("USER")
                                                .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .successHandler(customLoginSuccessHandler)
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutSuccessUrl("/login?logout")
                                                .permitAll())
                                .authenticationProvider(authenticationProvider())
                                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"));

                return http.build();
        }
}