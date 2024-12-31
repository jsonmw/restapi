package com.jason.restapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jason.restapi.entities.ExpenseEntity;

/**
 * JPA Repository for Expense resource
 * 
 * @author Jason
 */
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {
    /**
     * Fetches single expense details from database
     * 
     * @param expenseId
     * @return Optional<ExpenseEntity>
     */
    Optional<ExpenseEntity> findByExpenseId(String expenseId);

    List<ExpenseEntity> findByOwnerId(Long id);

    Optional<ExpenseEntity> findByOwnerIdAndExpenseId(Long ownerId, String expenseId);
}