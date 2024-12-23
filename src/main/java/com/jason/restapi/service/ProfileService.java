package com.jason.restapi.service;

import com.jason.restapi.DTO.ProfileDTO;

/**
 * Saves user details to database
 * 
 * @param ProfileDTO
 * @return ProfileDTO
 */
public interface ProfileService {
    ProfileDTO createProfile(ProfileDTO profileDTO);
}
