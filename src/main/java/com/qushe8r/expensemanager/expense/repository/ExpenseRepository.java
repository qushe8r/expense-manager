package com.qushe8r.expensemanager.expense.repository;

import com.qushe8r.expensemanager.expense.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {}
