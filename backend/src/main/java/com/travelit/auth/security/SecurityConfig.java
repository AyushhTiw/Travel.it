package com.travelit.auth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            OAuth2SuccessHandler oAuth2SuccessHandler
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                // ① CSRF disabled — JWT-based stateless API
                .csrf(csrf -> csrf.disable())

                // ② CORS for React/Vite frontend
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                /*
                 * ③ Unauthenticated /api/** requests get 401 JSON,
                 *   not a redirect to Google's HTML login page.
                 */
                .exceptionHandling(exception -> exception
                        .defaultAuthenticationEntryPointFor(
                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                                new AntPathRequestMatcher("/api/**")
                        )
                )

                /*
                 * ④ Session is IF_REQUIRED so the OAuth2 authorization-code
                 *   flow can store state in a transient session.
                 *   JWT API calls are otherwise stateless.
                 */
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )

                .authorizeHttpRequests(auth -> auth

                        // ⑤ CORS pre-flight
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // ⑥ Auth endpoints — always public
                        .requestMatchers("/api/v1/auth/**").permitAll()

                        // ⑦ OAuth2 flow
                        .requestMatchers("/oauth2/**").permitAll()
                        .requestMatchers("/login/oauth2/**").permitAll()

                        // ─────────────────────────────────────────────────
                        // ⑧ GUEST-ACCESSIBLE PUBLIC ENDPOINTS
                        //    Read/explore features work without login.
                        //    JWT is still parsed (JwtAuthenticationFilter
                        //    runs for every request) so authenticated users
                        //    get their context set automatically — but these
                        //    paths do NOT require a valid token.
                        // ─────────────────────────────────────────────────

                        // AI / Trevvy — guests can chat freely
                        .requestMatchers("/api/v1/ai/**").permitAll()

                        // Destinations — public browsing
                        .requestMatchers(HttpMethod.GET, "/api/v1/destinations/**").permitAll()

                        // Categories — public browsing
                        .requestMatchers(HttpMethod.GET, "/api/v1/categories/**").permitAll()

                        // Places — public browsing (internal + Google wrapper)
                        .requestMatchers(HttpMethod.GET, "/api/v1/places/**").permitAll()

                        // Explore / nearby — public map features
                        .requestMatchers("/api/v1/explore/**").permitAll()

                        // ─────────────────────────────────────────────────
                        // ⑨ PROTECTED ENDPOINTS (require authentication)
                        //    Trips / budgets / user data / admin = login required.
                        //    The Trip module will enforce user ownership
                        //    once the userId FK is added to the Trip entity.
                        // ─────────────────────────────────────────────────
                        .requestMatchers("/api/v1/trips/**").authenticated()
                        .requestMatchers("/api/v1/budgets/**").authenticated()
                        .requestMatchers("/api/v1/admin/**").authenticated()

                        // Everything else also requires auth by default
                        .anyRequest().authenticated()
                )

                /*
                 * ⑩ JWT filter runs before Spring's username/password filter.
                 *   For public endpoints the filter still runs but does NOT
                 *   block the request when no token is present — it simply
                 *   leaves the SecurityContext unauthenticated and continues.
                 */
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                // ⑪ Google OAuth2 login is still wired in
                .oauth2Login(oauth2 -> oauth2.successHandler(oAuth2SuccessHandler));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        // React/Vite dev server + deployed frontend
        configuration.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "http://localhost:3000"
        ));

        configuration.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));

        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
