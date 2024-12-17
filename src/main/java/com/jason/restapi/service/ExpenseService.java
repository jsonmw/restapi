package com.jason.restapi.service;

import java.util.List;

import com.jason.restapi.DTO.ExpenseDTO;

public interface ExpenseService {
    List<ExpenseDTO> getAllExpenses();
}
