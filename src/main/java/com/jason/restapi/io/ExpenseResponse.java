package com.jason.restapi.io;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ExpenseResponse {
    private String expenseId;
    private String name;
    private String note;
    private String category;
    private Date date;
    private BigDecimal amount;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
