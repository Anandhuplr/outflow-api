package com.outFlow.outFlow.repository;

import com.outFlow.outFlow.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository
        extends JpaRepository<Expense, Long> {

    List<Expense> findByUserId(Long userId);

    Optional<Expense> findByIdAndUserId(Long id, Long userId);
}
