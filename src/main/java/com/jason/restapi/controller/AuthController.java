package com.jason.restapi.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jason.restapi.DTO.ProfileDTO;
import com.jason.restapi.io.AuthRequest;
import com.jason.restapi.io.AuthResponse;
import com.jason.restapi.io.ProfileRequest;
import com.jason.restapi.io.ProfileResponse;
import com.jason.restapi.service.CustomUserDetailsService;
import com.jason.restapi.service.ProfileService;
import com.jason.restapi.service.TokenBlacklistService;
import com.jason.restapi.util.JwtTokenUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
    private final TokenBlacklistService tokenBlacklistService;

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
    public AuthResponse authenticateProfile(@RequestBody AuthRequest authRequest) throws Exception {
        log.info("API /login invoked {}", authRequest);
        authenticate(authRequest);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return new AuthResponse(token, authRequest.getEmail());
    }

    /**
     * API endpoint to authenticate logout and add JWT token to blacklist
     * 
     * @param HttpServletRequest
     * @return ResponseEntity
     */

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/signout")
    public void signout(HttpServletRequest request) {
        
        String jwtToken = extractJwtTokenFromRequest(request);
        log.info("API /signout invoked {}", jwtToken);

        if (jwtToken != null) {
            tokenBlacklistService.addTokenToBlacklist(jwtToken);
        }

    }

    /**
     * Extracts String of JWT Token from an HTTP request
     * 
     * @param HttpServletRequest
     * @return String
     */
    private String extractJwtTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            System.out.println(bearerToken.substring(7));
            return bearerToken.substring(7);
        }

        return null;
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

    private void authenticate(AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));

        } catch (DisabledException ex) {
            throw new Exception("Profile disabled");
        } catch (BadCredentialsException ex) {
            throw new Exception("Username or password not accepted");
        }
    }
}
