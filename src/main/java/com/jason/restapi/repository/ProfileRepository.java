package com.jason.restapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jason.restapi.entities.ProfileEntity;


public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {

    
}