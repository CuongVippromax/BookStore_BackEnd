package com.cuong.electronicstore.service;

import com.cuong.electronicstore.exception.ForbiddenException;
import com.cuong.electronicstore.model.User;
import com.cuong.electronicstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserService {
    private final UserRepository userRepository;

    public User getCurrentUser() {
        Jwt jwt = getJwt();
        String keycloakId = jwt.getSubject();

        return userRepository.findByKeycloakId(keycloakId)
                .orElseGet(() -> createFromJwt(jwt));
    }

    public boolean isCurrentUserAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equalsIgnoreCase("ADMIN")
                        || a.equalsIgnoreCase("ROLE_ADMIN"));
    }

    private User createFromJwt(Jwt jwt) {
        String email = jwt.getClaimAsString("email");
        String username = jwt.getClaimAsString("preferred_username");
        if (username == null) username = email;

        User user = User.builder()
                .keycloakId(jwt.getSubject())
                .email(email)
                .username(username)
                .build();
        return userRepository.save(user);
    }

    private Jwt getJwt() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ForbiddenException("Not authenticated");
        }
        Object principal = auth.getPrincipal();
        if (principal instanceof Jwt jwt) {
            return jwt;
        }
        throw new ForbiddenException("Authentication is not JWT-based");
    }
}
