package com.jason.restapi.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jason.restapi.DTO.ExpenseDTO;
import com.jason.restapi.io.ExpenseResponse;
import com.jason.restapi.service.ExpenseService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * This is the controller class for Expense module
 * @author Jason Wild
 *  */
@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin("*")
public class ExpenseController {
    private final ExpenseService expenseService;
    private final ModelMapper modelMapper;

    /**
     * Fetches allexpenses from database
     * @return list
     */
    @GetMapping("/expenses")
    public List<ExpenseResponse> getExpenses() {
        log.info("API GET /expenses called");

        // Call service method

        List<ExpenseDTO> list = expenseService.getAllExpenses();
        log.info("Printing data from service {}", list);

        // Convert ExpenseDTO to ExpenseResponse
        List<ExpenseResponse> response = list.stream()
                .map(expenseDTO -> mapToExpenseResponse(expenseDTO))
                .collect(Collectors.toList());

        return response;
    }

    /**
     * Mapper method for converting expense DTO to Expense Response object
     * @param expenseDTO
     * @return ExpenseResponse
     */
    private ExpenseResponse mapToExpenseResponse(ExpenseDTO expenseDTO) {
        return modelMapper.map(expenseDTO, ExpenseResponse.class);
    }

}
