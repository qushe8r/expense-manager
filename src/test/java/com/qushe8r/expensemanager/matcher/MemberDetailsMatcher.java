package com.qushe8r.expensemanager.matcher;

import com.qushe8r.expensemanager.member.entity.MemberDetails;
import java.util.List;
import org.mockito.ArgumentMatcher;
import org.springframework.security.core.GrantedAuthority;

public class MemberDetailsMatcher implements ArgumentMatcher<MemberDetails> {

  private final MemberDetails left;

  public MemberDetailsMatcher(MemberDetails left) {
    this.left = left;
  }

  @Override
  public boolean matches(MemberDetails right) {
    boolean isSameId = left.getId().equals(right.getId());
    boolean isSameEmail = left.getEmail().equals(right.getEmail());
    boolean isSameUsername = left.getUsername().equals(right.getUsername());
    boolean isSameAuthorities = isSameAuthorities(left, right);
    return isSameId && isSameUsername && isSameEmail && isSameAuthorities;
  }

  private boolean isSameAuthorities(MemberDetails left, MemberDetails right) {
    List<String> leftStr = mapToStringList(left);
    List<String> rightStr = mapToStringList(right);
    return leftStr.containsAll(rightStr);
  }

  private List<String> mapToStringList(MemberDetails memberDetails) {
    return memberDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
  }
}
