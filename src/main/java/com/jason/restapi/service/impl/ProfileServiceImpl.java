package com.jason.restapi.service.impl;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jason.restapi.DTO.ProfileDTO;
import com.jason.restapi.entities.ProfileEntity;
import com.jason.restapi.exceptions.ItemExistsException;
import com.jason.restapi.repository.ProfileRepository;
import com.jason.restapi.service.ProfileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service implementation for Profile module
 * 
 * @author Jason
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder encoder;

    /**
     * Saves user details to database
     * 
     * @param ProfileDTO
     * @return ProfileDTO
     */
    @Override
    public ProfileDTO createProfile(ProfileDTO profileDTO) {
        if (profileRepository.existsByEmail(profileDTO.getEmail())) {
            throw new ItemExistsException("Profile already exists " + profileDTO.getEmail());
        }
        profileDTO.setPassword(encoder.encode(profileDTO.getPassword()));
        ProfileEntity profileEntity = mapToProfileEntity(profileDTO);
        profileEntity.setProfileId(UUID.randomUUID().toString());
        profileEntity = profileRepository.save(profileEntity);
        log.info("Printing the profile entity details {}", profileEntity);
        return mapToProfileDTO(profileEntity);
    }

    /**
     * Maps ProfileDTO values to ProfileEntity object
     * 
     * @param ProfileDTO
     * @return ProfileEntity
     */
    private ProfileEntity mapToProfileEntity(ProfileDTO profileDTO) {
        return modelMapper.map(profileDTO, ProfileEntity.class);
    }

    /**
     * Maps ProfileEntity values to ProfileDTO object
     * 
     * @param ProfileEntity
     * @return ProfileDTO
     */
    private ProfileDTO mapToProfileDTO(ProfileEntity profileEntity) {
        return modelMapper.map(profileEntity, ProfileDTO.class);
    }
}