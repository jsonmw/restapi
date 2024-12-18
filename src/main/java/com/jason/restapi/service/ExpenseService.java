package com.jason.restapi.service;

import java.util.List;

import com.jason.restapi.DTO.ExpenseDTO;

/**
 * Service interface for Expense module
 * @author Jason
 */
public interface ExpenseService {

    /**
     * Retrieves all expenses from database
     * @return list
     */
    List<ExpenseDTO> getAllExpenses();
}
