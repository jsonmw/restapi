package com.jason.restapi.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.jason.restapi.DTO.ProfileDTO;
import com.jason.restapi.io.ProfileRequest;
import com.jason.restapi.io.ProfileResponse;
import com.jason.restapi.service.ProfileService;

import jakarta.validation.Valid;

/**
 * This is the controller class for Authorization and Profile Creation
 * 
 * @author Jason Wild
 */
@RestController
@Slf4j
@AllArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final ProfileService profileService;
    private final ModelMapper modelMapper;

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
