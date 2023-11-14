package com.qushe8r.expensemanager.budget.repository;

import com.qushe8r.expensemanager.budget.entity.Budget;
import java.time.YearMonth;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

  Optional<Budget> findByMonth(YearMonth month);
}
