package org.example.service.security;

import org.example.dto.security.JwtPerson;
import org.example.exception.JwtParsingException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JwtAuthorizationService {
    public JwtPerson extractJwtPerson() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal).map(JwtPerson.class::cast)
                .orElseThrow(() -> new JwtParsingException("Exception while deserializing token"));
    }

}
