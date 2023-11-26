package com.qushe8r.expensemanager.matcher;

import com.qushe8r.expensemanager.member.dto.PatchPassword;
import org.mockito.ArgumentMatcher;

public class PatchPasswordMatcher implements ArgumentMatcher<PatchPassword> {

  private final PatchPassword left;

  public PatchPasswordMatcher(PatchPassword left) {
    this.left = left;
  }

  @Override
  public boolean matches(PatchPassword right) {
    return left.password().equals(right.password());
  }
}
