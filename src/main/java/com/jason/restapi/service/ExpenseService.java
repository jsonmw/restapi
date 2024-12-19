package com.jason.restapi.service;

import java.util.List;

import com.jason.restapi.DTO.ExpenseDTO;

/**
 * Service interface for Expense module
 * 
 * @author Jason
 */
public interface ExpenseService {

    /**
     * Retrieves all expenses from database
     * 
     * @return list
     */
    List<ExpenseDTO> getAllExpenses();

    /**
     * Fetches single expense details from database
     * 
     * @param expenseId
     * @return Expense DTO
     */
    ExpenseDTO getExpenseByExpenseId(String expenseId);
    
    /**
     * Deletes expense from database
     * 
     * @param expenseId
     * @return void
     */
    void deleteExpenseByExpenseId(String expenseId);
}
