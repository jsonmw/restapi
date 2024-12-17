package com.jason.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jason.restapi.entities.ExpenseEntity;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {

}