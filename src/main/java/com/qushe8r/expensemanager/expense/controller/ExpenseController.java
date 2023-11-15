package com.qushe8r.expensemanager.expense.controller;

import com.qushe8r.expensemanager.common.utils.UriCreator;
import com.qushe8r.expensemanager.expense.dto.PostExpense;
import com.qushe8r.expensemanager.expense.service.ExpenseCreateUseCase;
import com.qushe8r.expensemanager.member.entity.MemberDetails;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

  private final ExpenseCreateUseCase expenseCreateUseCase;

  @PostMapping
  public ResponseEntity<Void> createExpense(
      @AuthenticationPrincipal MemberDetails memberDetails,
      @RequestBody @Validated PostExpense dto) {
    Long expenseId = expenseCreateUseCase.createExpense(memberDetails, dto);
    URI uri = UriCreator.createUri(EXPENSE_DEFAULT_URL, expenseId);
    return ResponseEntity.created(uri).build();
  }
}
