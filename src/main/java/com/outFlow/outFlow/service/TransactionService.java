package com.outFlow.outFlow.service;


import com.outFlow.outFlow.dto.TransactionRequest;
import com.outFlow.outFlow.entity.Category;
import com.outFlow.outFlow.entity.Expense;
import com.outFlow.outFlow.entity.User;
import com.outFlow.outFlow.repository.CategoryRepository;
import com.outFlow.outFlow.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final UserService userService;

    public TransactionService(
            TransactionRepository transactionRepository,
            CategoryRepository categoryRepository,
            UserService userService) {

        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        this.userService = userService;
    }

    public List<Expense> getTransactions(
            Long userId) {

        return transactionRepository
                .findByUserId(userId);
    }

    public Expense createTransaction(
            Long userId,
            TransactionRequest request) {

        User user =
                userService.getUserById(userId);

        Category category =
                categoryRepository
                        .findByIdAndUserId(
                                request.categoryId(),
                                userId
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Category not found"
                                ));

        Expense transaction =
                new Expense();

        transaction.setUser(user);
        transaction.setCategory(category);

        transaction.setAmount(request.amount());
        transaction.setDescription(
                request.description()
        );


        return transactionRepository
                .save(transaction);
    }

    public void deleteTransaction(
            Long userId,
            Long transactionId) {

        Expense transaction =
                transactionRepository
                        .findByIdAndUserId(
                                transactionId,
                                userId
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Expense not found"
                                ));

        transactionRepository.delete(transaction);
    }
}