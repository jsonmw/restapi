package com.jason.restapi.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RestController;

import com.jason.restapi.service.ExpenseService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;

import com.jason.restapi.DTO.ExpenseDTO;
import com.jason.restapi.responses.ExpenseResponse;


@RestController
@RequiredArgsConstructor
@Slf4j
public class ExpenseController {
    private final ExpenseService expenseService;
    private final ModelMapper modelMapper;


    @GetMapping("/expenses")
    public List<ExpenseResponse> getExpenses() {
        log.info("API GET /expenses called");

        List<ExpenseDTO> list = expenseService.getAllExpenses();
        log.info("Printing data from service {}", list);

        List<ExpenseResponse> response = list.stream()
            .map(expenseDTO -> mapToExpenseResponse(expenseDTO))
            .collect(Collectors.toList());

        return response;
    }

    private ExpenseResponse mapToExpenseResponse(ExpenseDTO expenseDTO) {
        return modelMapper.map(expenseDTO, ExpenseResponse.class);
    }
    
}
