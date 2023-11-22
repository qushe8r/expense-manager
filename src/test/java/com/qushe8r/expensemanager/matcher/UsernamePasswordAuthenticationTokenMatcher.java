package com.qushe8r.expensemanager.matcher;

import org.mockito.ArgumentMatcher;
import org.springframework.security.core.Authentication;

public class UsernamePasswordAuthenticationTokenMatcher implements ArgumentMatcher<Authentication> {

  private final Authentication left;

  public UsernamePasswordAuthenticationTokenMatcher(Authentication left) {
    this.left = left;
  }

  @Override
  public boolean matches(Authentication right) {
    boolean isSameName = left.getName().equals(right.getName());
    boolean isSamePrincipal = principal(left).equals(principal(right));
    boolean isSameCredential = credential(left).equals(credential(right));
    return isSameName && isSamePrincipal && isSameCredential;
  }

  private String principal(Authentication authentication) {
    return (String) authentication.getPrincipal();
  }

  private String credential(Authentication authentication) {
    return (String) authentication.getCredentials();
  }
}
