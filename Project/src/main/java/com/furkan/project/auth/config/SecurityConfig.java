package com.furkan.project.auth.config;

import com.furkan.project.auth.jwt.JwtAuthenticationFilter;
import com.furkan.project.auth.jwt.JwtProperties;
import com.furkan.project.auth.jwt.JwtTokenProvider;
import com.furkan.project.user.entity.User;
import com.furkan.project.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.stream.Collectors;

@Configuration
@EnableMethodSecurity
@EnableConfigurationProperties(JwtProperties.class)
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

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

        http.cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // --- Public altyapı ---
                        .requestMatchers("/error", "/favicon.ico").permitAll()
                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/api/health/**",
                                "/__probe/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/movies/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/movies/*/cast/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/movies/*/comments/**").permitAll()
                        .requestMatchers(HttpMethod.POST,   "/api/v1/movies/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,    "/api/v1/movies/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH,  "/api/v1/movies/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/movies/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,
                                "/api/v1/actors/**",
                                "/api/v1/genres/**",
                                "/api/v1/directors/**",
                                "/api/v1/languages/**",
                                "/api/v1/countries/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/api/v1/actors/**",
                                "/api/v1/genres/**",
                                "/api/v1/directors/**",
                                "/api/v1/languages/**",
                                "/api/v1/countries/**"
                        ).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,
                                "/api/v1/actors/**",
                                "/api/v1/genres/**",
                                "/api/v1/directors/**",
                                "/api/v1/languages/**",
                                "/api/v1/countries/**"
                        ).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH,
                                "/api/v1/actors/**",
                                "/api/v1/genres/**",
                                "/api/v1/directors/**",
                                "/api/v1/languages/**",
                                "/api/v1/countries/**"
                        ).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,
                                "/api/v1/actors/**",
                                "/api/v1/genres/**",
                                "/api/v1/directors/**",
                                "/api/v1/languages/**",
                                "/api/v1/countries/**"
                        ).hasRole("ADMIN")

                        .requestMatchers("/api/v1/users/**").hasRole("ADMIN")

                        .requestMatchers("/api/v1/lists/**").authenticated()
                        .requestMatchers(HttpMethod.POST,   "/api/v1/movies/*/comments").authenticated()
                        .requestMatchers(HttpMethod.PATCH,  "/api/v1/movies/*/comments/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/movies/*/comments/**").authenticated()

                        .requestMatchers("/api/v1/movies/*/ai/**").authenticated()
                        .requestMatchers("/api/v1/ai/cache/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter,
                        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cors = new CorsConfiguration();

        cors.setAllowedOrigins(java.util.List.of(
                "http://localhost:5173",
                "http://localhost:5174"
        ));


        cors.setAllowedMethods(java.util.List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        cors.setAllowedHeaders(java.util.List.of("Authorization","Content-Type","Accept","Origin","X-Requested-With"));
        cors.setExposedHeaders(java.util.List.of("Authorization","Location"));
        cors.setAllowCredentials(true);
        cors.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return source;
    }
}
