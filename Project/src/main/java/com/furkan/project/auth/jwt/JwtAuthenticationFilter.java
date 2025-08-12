package com.furkan.project.auth.jwt;

import com.furkan.project.auth.entity.AuthUser;
import com.furkan.project.user.entity.User;
import com.furkan.project.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws ServletException, IOException {

        String auth = req.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);
            if (tokenProvider.validate(token)) {
                String username = tokenProvider.getUsername(token);

                User user = userRepository.findWithRolesByUsername(username)
                        .filter(u -> !u.isDeleted() && u.isEnabled() && !u.isLocked())
                        .orElse(null);

                if (user != null) {
                    var authorities = user.getRoles().stream()
                            .map(r -> new SimpleGrantedAuthority(r.getName().name()))
                            .collect(Collectors.toSet());

                    var principal = new AuthUser(user.getId(), user.getUsername(), authorities);

                    var authentication = new UsernamePasswordAuthenticationToken(
                            principal, null, authorities
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                }
            }
        }

        chain.doFilter(req, res);
    }
}
