
package com.med.system.ManTick.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.med.system.ManTick.JWT.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import static com.med.system.ManTick.Users.Role.*;
import static org.springframework.http.HttpMethod.*;
import static com.med.system.ManTick.Users.Permission.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(
                    authorize -> authorize
                                
                                .requestMatchers("/api/v1/auth/authenticate")
                                .permitAll()
                                .requestMatchers("/api/v1/auth/register").hasAnyRole(ADMIN.name())
                                .requestMatchers("/api/v1/management/**").hasAnyRole(ADMIN.name(), MANAGER.name())
                                .requestMatchers(GET, "/api/v1/management/**").hasAnyAuthority(ADMIN_READ.name(), MANAGER_READ.name())
                                .requestMatchers(POST, "/api/v1/management/**").hasAnyAuthority(ADMIN_CREATE.name(), MANAGER_CREATE.name())
                                .requestMatchers(PUT, "/api/v1/management/**").hasAnyAuthority(ADMIN_UPDATE.name(), MANAGER_UPDATE.name())
                                .requestMatchers(DELETE, "/api/v1/management/**").hasAnyAuthority(ADMIN_DELETE.name(), MANAGER_DELETE.name())
                                // .requestMatchers(GET, "/api/v1/ticket").hasAnyRole(ADMIN.name(), MANAGER.name())
                                .requestMatchers(PUT, "/api/v1/ticket/assignTicket").hasAnyRole(ADMIN.name(), MANAGER.name())
                                .requestMatchers(PUT, "/api/v1/ticket/closeTicket").hasAnyRole(ADMIN.name(), MANAGER.name())
                                .anyRequest()
                                
                                .authenticated()
                                
                    )
                .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS

                ))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                            .logoutUrl("/api/v1/auth/logout")
                            .addLogoutHandler(logoutHandler)
                            .logoutSuccessHandler((request, response, authentication) -> 
                                SecurityContextHolder.clearContext())
      
                );        
        
        return http.build();
    }
}
