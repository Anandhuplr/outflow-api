package com.outFlow.outFlow.controller;



import com.outFlow.outFlow.dto.TransactionRequest;
import com.outFlow.outFlow.entity.Expense;
import com.outFlow.outFlow.service.TransactionService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(
            TransactionService transactionService) {

        this.transactionService = transactionService;
    }

    @GetMapping
    public List<Expense> getTransactions(
            Authentication authentication) {

        Long userId =
                (Long) authentication.getPrincipal();

        return transactionService
                .getTransactions(userId);
    }

    @PostMapping
    public Expense createTransaction(
            @RequestBody TransactionRequest request,
            Authentication authentication) {

        Long userId =
                (Long) authentication.getPrincipal();

        return transactionService
                .createTransaction(
                        userId,
                        request
                );
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(
            @PathVariable Long id,
            Authentication authentication) {

        Long userId =
                (Long) authentication.getPrincipal();

        transactionService.deleteTransaction(
                userId,
                id
        );
    }
}