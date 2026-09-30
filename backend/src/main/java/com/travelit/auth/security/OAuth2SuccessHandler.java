package com.travelit.auth.security;

import com.travelit.auth.entity.User;
import com.travelit.auth.repository.UserRepository;
import com.travelit.auth.service.JwtService;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public OAuth2SuccessHandler( 
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        JwtService jwtService) 
        {
            this.userRepository = userRepository;
            this.passwordEncoder = passwordEncoder;
            this.jwtService = jwtService;
        }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        // ① Google se authenticated user
        OAuth2User googleUser = (OAuth2User) authentication.getPrincipal();

        // ② Google profile se email
        String email = googleUser.getAttribute("email");

        // ③ Google profile se name
        String name = googleUser.getAttribute("name");

        // ④ Google profile picture
        String picture = googleUser.getAttribute("picture");

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                    "Google account email not available"
            );
        }

        // ⑤ Normalize email into a NEW variable
        String normalizedEmail =
                email.trim().toLowerCase();

        /// ⑥ Travel.it user already exists?
        User user = userRepository
                .findByEmail(normalizedEmail)
                .orElseGet(() -> createGoogleUser(name, normalizedEmail, picture));

        // ⑤ Travel.it ka apna JWT generate karo
        String accessToken = jwtService.generateAccessToken(user);

        String refreshToken = jwtService.generateRefreshToken(user);

        // ⑥ Frontend par redirect
        String frontendUrl = System.getProperty("FRONTEND_URL", "http://localhost:5173");
        String redirectUrl =
                UriComponentsBuilder.fromUriString(frontendUrl + "/oauth2/success")
                        .queryParam("accessToken", accessToken)
                        .queryParam("refreshToken", refreshToken)
                        .build().toUriString();
        response.sendRedirect(redirectUrl);
    }

    private User createGoogleUser(String name, String email, String picture)
    {

        User user = new User();

        user.setName(name == null || name.isBlank() ? email.split("@")[0]: name.trim());
        user.setEmail(email);

        /*
          Current User entity mein password nullable=false hai. Google user Travel.it ka local password use nahi karta, isliye random password hash store kar rahe hain.
        */
        String randomPassword = UUID.randomUUID().toString();

        user.setPassword( passwordEncoder.encode(randomPassword));

        user.setRole("USER");
        
        // Google profile picture store karo
        user.setProfilePicture(picture);

        return userRepository.save(user);
    }
}