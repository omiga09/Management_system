package com.omar.Management_System.Configuration;

import com.omar.Management_System.Authintication.CustomerUserDetailsService.CustomerUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;
    private final CustomerUserDetailsService customerUserDetailsService;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter,
                          @Lazy CustomerUserDetailsService customerUserDetailsService) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.customerUserDetailsService = customerUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                                .requestMatchers("/api/auth/**").permitAll()


                                .requestMatchers("/api/reservations/**").hasRole("BUYER")


                                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                                .requestMatchers("/api/users/**").hasRole("ADMIN")
                                .requestMatchers("/api/properties/search").hasRole("BUYER")
                                .requestMatchers("/api/properties/{id}/approve").hasRole("ADMIN")
                                .requestMatchers("/api/transactions/**").hasAnyRole("ADMIN", "BUYER")
                                .requestMatchers("/api/transactions/reservation/**").hasAuthority("BUYER")



                                .requestMatchers("/api/seller/**").hasRole("SELLER")
                                .requestMatchers("/api/buyer/**").hasRole("BUYER")


                                .requestMatchers("/api/properties/admin").hasRole("ADMIN")
                                .requestMatchers("/api/properties/**").hasAnyRole("ADMIN", "SELLER","BUYER")



                                .anyRequest().authenticated()
                        )

                        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
