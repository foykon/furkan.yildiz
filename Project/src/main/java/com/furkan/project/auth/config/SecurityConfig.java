package com.furkan.project.auth.config;

import com.furkan.project.auth.jwt.JwtAuthenticationFilter;
import com.furkan.project.auth.jwt.JwtProperties;
import com.furkan.project.auth.jwt.JwtTokenProvider;
import com.furkan.project.user.entity.User;
import com.furkan.project.user.repository.UserRepository;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.stream.Collectors;

@Configuration
@EnableMethodSecurity
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> {
            User u = userRepository.findWithRolesByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            var authorities = u.getRoles().stream()
                    .map(r -> new SimpleGrantedAuthority(r.getName().name()))
                    .collect(Collectors.toSet());

            boolean enabled = u.isEnabled() && !u.isDeleted();
            boolean accountNonLocked = !u.isLocked();

            return new org.springframework.security.core.userdetails.User(
                    u.getUsername(),
                    u.getPassword(),
                    enabled,
                    true,
                    true,
                    accountNonLocked,
                    authorities
            );
        };
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain filterChain(org.springframework.security.config.annotation.web.builders.HttpSecurity http,
                                    JwtTokenProvider tokenProvider,
                                    UserRepository userRepository) throws Exception {

        var jwtFilter = new JwtAuthenticationFilter(tokenProvider, userRepository);

        http.csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/refresh",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/api/health/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
