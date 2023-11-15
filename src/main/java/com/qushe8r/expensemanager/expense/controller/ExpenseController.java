package com.qushe8r.expensemanager.expense.controller;

import com.qushe8r.expensemanager.common.utils.UriCreator;
import com.qushe8r.expensemanager.expense.dto.PostExpense;
import com.qushe8r.expensemanager.expense.service.ExpenseService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/expenses")
@RequiredArgsConstructor
public class ExpenseController {

  private static final String EXPENSE_DEFAULT_URL = "/expenses";

  private final ExpenseService expenseService;

  @PostMapping
  public ResponseEntity<Void> createExpense(@RequestBody @Validated PostExpense dto) {
    Long expenseId = expenseService.createExpense(dto);
    URI uri = UriCreator.createUri(EXPENSE_DEFAULT_URL, expenseId);
    return ResponseEntity.created(uri).build();
  }
}
