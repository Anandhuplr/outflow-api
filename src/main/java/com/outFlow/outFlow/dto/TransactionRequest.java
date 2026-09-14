package com.outFlow.outFlow.dto;




import com.outFlow.outFlow.entity.PaymentMethod;
import com.outFlow.outFlow.entity.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequest(

        Long categoryId,

        TransactionType type,

        BigDecimal amount,

        String description,

        LocalDate transactionDate,

        PaymentMethod paymentMethod,

        String notes
) {}