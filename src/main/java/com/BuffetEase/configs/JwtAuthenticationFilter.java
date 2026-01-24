package com.BuffetEase.configs;

import com.BuffetEase.services.CustomUserDetailsService;
import com.BuffetEase.services.JwtService;
import com.BuffetEase.services.TokenRefreshService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final TokenRefreshService tokenRefreshService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            CustomUserDetailsService userDetailsService,
            TokenRefreshService tokenRefreshService)
    {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.tokenRefreshService = tokenRefreshService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);

        try {
            userEmail = jwtService.extractUsername(jwt);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    System.out.println("Token valid for user: " + userEmail);
                }
            }

        }
        catch (io.jsonwebtoken.ExpiredJwtException e) {
            System.out.println("Token expired, attempting auto-refresh...");

            String expiredTokenEmail = jwtService.extractUsernameFromExpiredToken(jwt);

            Optional<String> newAccessToken = tokenRefreshService.attemptAutoRefresh(expiredTokenEmail);

            if (newAccessToken.isPresent()) {
                response.setHeader("X-New-Access-Token", newAccessToken.get());
                response.setHeader("X-Token-Refreshed", "true");

                System.out.println("Token auto-refreshed for user: " + expiredTokenEmail);

                UserDetails userDetails = this.userDetailsService.loadUserByUsername(expiredTokenEmail);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

            } else {
                System.out.println("✗ Auto-refresh failed for user: " + expiredTokenEmail);
                response.setHeader("X-Token-Refreshed", "false");
            }

        } catch (Exception e) {
            System.out.println("✗ Invalid token: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}