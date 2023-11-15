package com.qushe8r.expensemanager.expense.repository;

import com.qushe8r.expensemanager.expense.entity.Expense;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

  @Query("SELECT e FROM Expense e JOIN FETCH e.memberCategory mc WHERE e.id = :expenseId")
  Optional<Expense> findExpenseMemberCategoryById(Long expenseId);
}
