package com.qushe8r.expensemanager.matcher;

import com.qushe8r.expensemanager.member.dto.PostMember;
import org.mockito.ArgumentMatcher;

public class PostMemberMatcher implements ArgumentMatcher<PostMember> {

  private final PostMember left;

  public PostMemberMatcher(PostMember left) {
    this.left = left;
  }

  @Override
  public boolean matches(PostMember right) {
    boolean isSameEmail = left.email().equals(right.email());
    boolean isSamePassword = left.password().equals(right.password());
    return isSameEmail && isSamePassword;
  }
}
