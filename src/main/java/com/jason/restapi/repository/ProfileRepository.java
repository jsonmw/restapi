package com.jason.restapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jason.restapi.entities.ProfileEntity;

/**
 * JPA Repository for Profile resource
 * 
 * @author Jason
 */
public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {

    /**
     * Fetches single profile details from database
     * 
     * @param email
     * @return Optional<ExpenseEntity>
     */
    Optional<ProfileEntity> findByEmail(String email);

    Boolean existsByEmail(String email);

}