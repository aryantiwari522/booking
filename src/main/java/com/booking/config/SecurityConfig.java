package com.booking.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, @Value("${app.security.enabled:false}") boolean securityEnabled) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        if (securityEnabled) {
            http.authorizeHttpRequests(authz -> authz.anyRequest().authenticated())
                    .httpBasic(httpBasic -> {});
        } else {
            http.authorizeHttpRequests(authz -> authz.anyRequest().permitAll())
                    .httpBasic(AbstractHttpConfigurer::disable);
        }

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(
            @Value("${app.security.username:admin}") String username,
            @Value("${app.security.password:}") String password,
            PasswordEncoder passwordEncoder
    ) {
        if (password == null || password.isBlank()) {
            password = java.util.UUID.randomUUID().toString();
        }
        return new InMemoryUserDetailsManager(User.withUsername(username)
                .password(passwordEncoder.encode(password))
                .roles("ADMIN")
                .build());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
