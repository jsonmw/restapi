package com.jason.restapi.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.jason.restapi.DTO.ExpenseDTO;
import com.jason.restapi.entities.ExpenseEntity;
import com.jason.restapi.exceptions.ResourceNotFoundException;
import com.jason.restapi.repository.ExpenseRepository;
import com.jason.restapi.service.ExpenseService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service implementation for Expense module
 * 
 * @author Jason
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ModelMapper modelMapper;

    /**
     * Retrieves all expenses from database
     * 
     * @return list
     */
    @Override
    public List<ExpenseDTO> getAllExpenses() {
        // Call repository method
        List<ExpenseEntity> list = expenseRepository.findAll();
        log.info("Printing the data from repository {}", list);

        // Convert the Entity object to a DTO
        List<ExpenseDTO> listOfExpenses = list.stream()
                .map(expenseEntity -> mapToExpenseDTO(expenseEntity))
                .collect(Collectors.toList());

        return listOfExpenses;
    }

    /**
     * Fetches single expense details from database
     * 
     * @param expenseId
     * @return Expense DTO
     */
    @Override
    public ExpenseDTO getExpenseByExpenseId(String expenseId) {
        ExpenseEntity expenseEntity = getExpenseEntity(expenseId);
        log.info("Printing the expense entity details {}", expenseEntity);
        return mapToExpenseDTO(expenseEntity);
    }

    /**
     * Deletes expense from database
     * 
     * @param expenseId
     * @return void
     */
    @Override
    public void deleteExpenseByExpenseId(String expenseId) {
        ExpenseEntity expenseEntity = getExpenseEntity(expenseId);
        log.info("Printing ExpenseEntity {}", expenseEntity);

        expenseRepository.delete(expenseEntity);
    }

    /**
     * Mapper method to convert expense entity to expense DTO
     * 
     * @param expenseEntity
     * @return ExpenseDTO
     */

    private ExpenseDTO mapToExpenseDTO(ExpenseEntity expenseEntity) {
        return modelMapper.map(expenseEntity, ExpenseDTO.class);
    }

    /**
     * Fetch expense by expense id
     * 
     * @param expenseId
     * @return ExpenseEntity
     */
    private ExpenseEntity getExpenseEntity(String expenseId) {
        return expenseRepository.findByExpenseId(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found for the expense id " + expenseId));

    }
}
