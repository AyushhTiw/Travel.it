package com.travelit.auth.security;

import com.travelit.auth.entity.User;
import com.travelit.auth.repository.UserRepository;
import com.travelit.auth.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserRepository userRepository
    ) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String requestPath = request.getRequestURI();

        // ① Authorization header read karo
        String authorizationHeader =
                request.getHeader("Authorization");

        System.out.println(
                "JWT FILTER → " +
                request.getMethod() +
                " " +
                requestPath
        );

        // ② Header missing hai
        if (authorizationHeader == null) {

            System.out.println(
                    "JWT FILTER → Authorization header MISSING"
            );

            filterChain.doFilter(request, response);
            return;
        }

        // ③ Bearer format check
        if (!authorizationHeader.startsWith("Bearer ")) {

            System.out.println(
                    "JWT FILTER → Invalid Authorization format"
            );

            filterChain.doFilter(request, response);
            return;
        }

        // ④ "Bearer " ke baad actual JWT
        String token =
                authorizationHeader.substring(7).trim();

        if (token.isEmpty()) {

            System.out.println(
                    "JWT FILTER → Token EMPTY"
            );

            filterChain.doFilter(request, response);
            return;
        }

        try {

            // ⑤ Access-token validation
            boolean valid =
                    jwtService.isAccessTokenValid(token);

            if (!valid) {

                System.out.println(
                        "JWT FILTER → Token INVALID"
                );

                filterChain.doFilter(request, response);
                return;
            }

            System.out.println(
                    "JWT FILTER → Token VALID"
            );

            // ⑥ JWT ke subject se email nikalo
            String email =
                    jwtService.extractEmail(token);

            System.out.println(
                    "JWT FILTER → Email: " + email
            );

            // ⑦ Database se user find karo
            User user =
                    userRepository.findByEmail(email)
                            .orElse(null);

            if (user == null) {

                System.out.println(
                        "JWT FILTER → USER NOT FOUND"
                );

                filterChain.doFilter(request, response);
                return;
            }

            // ⑧ Agar authentication already set nahi hai
            if (SecurityContextHolder
                    .getContext()
                    .getAuthentication() == null) {

                SimpleGrantedAuthority authority =
                        new SimpleGrantedAuthority(
                                "ROLE_" + user.getRole()
                        );

                // ⑨ Spring Security Authentication object
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                List.of(authority)
                        );

                // ⑩ SecurityContext me authenticated user set
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);

                System.out.println(
                        "JWT FILTER → AUTHENTICATED: "
                                + user.getEmail()
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "JWT FILTER → EXCEPTION: "
                            + e.getClass().getSimpleName()
            );

            System.out.println(
                    "JWT FILTER → MESSAGE: "
                            + e.getMessage()
            );

            SecurityContextHolder.clearContext();
        }

        // ⑪ Request ko next filter/controller tak bhejo
        filterChain.doFilter(request, response);
    }
}