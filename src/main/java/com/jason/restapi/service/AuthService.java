package com.jason.restapi.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jason.restapi.entities.ProfileEntity;
import com.jason.restapi.repository.ProfileRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    
    private final ProfileRepository profileRepository;

    public ProfileEntity getLoggedInProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String email = authentication.getName();
        log.info("Retrieving profile at email: {}", email);
        return profileRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Profile not found for the given email " + email));
    }
}
