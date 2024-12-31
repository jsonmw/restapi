package com.jason.restapi.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.jason.restapi.DTO.ExpenseDTO;
import com.jason.restapi.entities.ExpenseEntity;
import com.jason.restapi.entities.ProfileEntity;
import com.jason.restapi.exceptions.ResourceNotFoundException;
import com.jason.restapi.repository.ExpenseRepository;
import com.jason.restapi.service.AuthService;
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
    private final AuthService authService;

    /**
     * Retrieves all expenses from database
     * 
     * @return list
     */
    @Override
    public List<ExpenseDTO> getAllExpenses() {
        Long loggedInProfileId = authService.getLoggedInProfile().getId();

        // Call repository method
        List<ExpenseEntity> list = expenseRepository.findByOwnerId(loggedInProfileId);
        log.info("Printing the data from repository {}", list);

        // Convert the Entity object to a DTO
        List<ExpenseDTO> listOfExpenses = list.stream().map(expenseEntity -> mapToExpenseDTO(expenseEntity))
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
     * Saves new expense in database
     * 
     * @param expenseDTO
     * @return expenseDTO
     */
    @Override
    public ExpenseDTO saveExpenseDetails(ExpenseDTO expenseDTO) {
        ProfileEntity profileEntity = authService.getLoggedInProfile();
        log.info("Retrieved profile: {}", profileEntity);
        ExpenseEntity expenseEntity = mapToExpenseEntity(expenseDTO);
        expenseEntity.setExpenseId(UUID.randomUUID().toString());
        expenseEntity.setOwner(profileEntity);

        expenseEntity = expenseRepository.save(expenseEntity);
        log.info("Printing the new expense entity details {}", expenseEntity);
        return mapToExpenseDTO(expenseEntity);
    }

    /**
     * Saves new expense in database
     * 
     * @param expenseDTO
     * @param expenseId
     * @return expenseDTO
     */
    @Override
    public ExpenseDTO updateExpenseDetails(ExpenseDTO expenseDTO, String expenseId) {
        ExpenseEntity expenseEntity = getExpenseEntity(expenseId);
        ExpenseEntity updatedExpenseEntity = mapToExpenseEntity(expenseDTO);
        updatedExpenseEntity.setId(expenseEntity.getId());
        updatedExpenseEntity.setExpenseId(expenseEntity.getExpenseId());
        updatedExpenseEntity.setCreatedAt(expenseEntity.getCreatedAt());
        updatedExpenseEntity.setUpdatedAt(expenseEntity.getUpdatedAt());
        updatedExpenseEntity.setOwner(authService.getLoggedInProfile());
        updatedExpenseEntity = expenseRepository.save(updatedExpenseEntity);

        log.info("Printing the updated expense entity details {}", updatedExpenseEntity);
        return mapToExpenseDTO(updatedExpenseEntity);
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
     * Mapper method to convert expense DTO to expense entity
     * 
     * @param expenseDTO
     * @return ExpenseEntity
     */

    private ExpenseEntity mapToExpenseEntity(ExpenseDTO expenseDTO) {
        return modelMapper.map(expenseDTO, ExpenseEntity.class);
    }

    /**
     * Fetch expense by expense id and owner id
     * 
     * @param expenseId
     * @return ExpenseEntity
     */
    private ExpenseEntity getExpenseEntity(String expenseId) {
        Long ownerId = authService.getLoggedInProfile().getId();
        return expenseRepository.findByOwnerIdAndExpenseId(ownerId, expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found for the expense id " + expenseId));

    }
}
