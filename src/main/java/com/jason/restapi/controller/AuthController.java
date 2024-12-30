package com.jason.restapi.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.jason.restapi.DTO.ProfileDTO;
import com.jason.restapi.io.AuthRequest;
import com.jason.restapi.io.AuthResponse;
import com.jason.restapi.io.ProfileRequest;
import com.jason.restapi.io.ProfileResponse;
import com.jason.restapi.service.CustomUserDetailsService;
import com.jason.restapi.service.ProfileService;
import com.jason.restapi.util.JwtTokenUtil;

import jakarta.validation.Valid;

/**
 * This is the controller class for Authorization and Profile Creation
 * 
 * @author Jason Wild
 */
@RestController
@Slf4j
@AllArgsConstructor
public class AuthController {

    private final ProfileService profileService;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final CustomUserDetailsService userDetailsService;

    /**
     * API endpoint to register new user
     * 
     * @param ProfileRequest
     * @return ProfileResponse
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ProfileResponse createProfile(@RequestBody @Valid ProfileRequest profileRequest) {
        log.info("POST API on /register is called {}", profileRequest);
        ProfileDTO profileDTO = mapToProfileDTO(profileRequest);
        profileDTO = profileService.createProfile(profileDTO);
        log.info("Printing the ProfileDTO details {} ", profileDTO);
        return mapToProfileResponse(profileDTO);

    }

    /**
     * API endpoint to authenticate login and generate token
     * 
     * @param AuthRequest
     * @return AuthResponse
     */
    @PostMapping("/login")
    public AuthResponse authenticate(@RequestBody AuthRequest authRequest) {
        log.info("API /login invoked {}", authRequest);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return new AuthResponse(token, authRequest.getEmail());
    }

    /**
     * Maps values from ProfileDTO to ProfileResponse
     * 
     * @param ProfileDTO
     * @return ProfileResponse
     */
    private ProfileResponse mapToProfileResponse(ProfileDTO profileDTO) {
        return modelMapper.map(profileDTO, ProfileResponse.class);
    }

    /**
     * Maps values from ProfileRequest to ProfileDTO
     * 
     * @param ProfileRequest
     * @return ProfileDTO
     */
    private ProfileDTO mapToProfileDTO(ProfileRequest profileRequest) {
        return modelMapper.map(profileRequest, ProfileDTO.class);
    }
}
