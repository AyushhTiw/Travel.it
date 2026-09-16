package com.travelit.auth.controller;

import com.travelit.auth.dto.AuthResponse;
import com.travelit.auth.dto.LoginRequest;
import com.travelit.auth.dto.RefreshTokenRequest;
import com.travelit.auth.dto.SignupRequest;
import com.travelit.auth.dto.UserProfileResponse;
import com.travelit.auth.entity.User;
import com.travelit.auth.repository.UserRepository;
import com.travelit.auth.service.AuthService;
import com.travelit.auth.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthController(
            AuthService authService,
            JwtService jwtService,
            UserRepository userRepository
    ) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    // =========================
    // SIGNUP
    // =========================

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(
            @Valid @RequestBody SignupRequest request
    ) {

        AuthResponse response =
                authService.signup(request);

        return ResponseEntity.ok(response);
    }

    // =========================
    // LOGIN
    // =========================

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {

        AuthResponse response =
                authService.login(request);

        return ResponseEntity.ok(response);
    }

    // =========================
    // REFRESH TOKEN
    // =========================

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @Valid @RequestBody RefreshTokenRequest request
    ) {

        String refreshToken =
                request.getRefreshToken();

        if (!jwtService.isRefreshTokenValid(refreshToken)) {
            throw new IllegalArgumentException(
                    "Invalid or expired refresh token"
            );
        }

        String email =
                jwtService.extractEmail(refreshToken);

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "User not found"
                                )
                        );

        String newAccessToken =
                jwtService.generateAccessToken(user);

        String newRefreshToken =
                jwtService.generateRefreshToken(user);

        AuthResponse response =
                new AuthResponse(
                        newAccessToken,
                        newRefreshToken,
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole()
                );

        return ResponseEntity.ok(response);
    }

    // =========================
    // CURRENT USER
    // =========================

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getCurrentUser(
            Authentication authentication
    ) {

        User user = (User) authentication.getPrincipal();

        UserProfileResponse response =
                new UserProfileResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole()
                );

        return ResponseEntity.ok(response);
    }
    // =========================
// LOGOUT
// =========================

@PostMapping("/logout")
public ResponseEntity<String> logout() {

    return ResponseEntity.ok(
            "Logout successful. Please remove the access token on the client."
    );
}
}