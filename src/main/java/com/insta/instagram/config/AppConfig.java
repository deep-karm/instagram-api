package com.insta.instagram.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class AppConfig {
    @Bean
    SecurityFilterChain securityConfiguration(HttpSecurity http) throws Exception {


        http.sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Using sessionCreationPolicy directly
        )
        .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/signup").permitAll() // Allow signup requests
                        .anyRequest().authenticated() // Authenticate other requests
        )
        .addFilterAfter(new JwtTokenGeneratorFilter(), BasicAuthenticationFilter.class) // Add your custom filters if needed
                .addFilterBefore(new JwtTokenValidationFilter(), BasicAuthenticationFilter.class) // Add your custom filters if needed
                .csrf(csrf -> csrf.disable()) // Disable CSRF if not needed (for stateless authentication)
                .httpBasic(); // Enable HTTP Basic Authentication if needed
		
		return http.build();
	}

    @Bean
    PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
