package com.qushe8r.expensemanager.matcher;

import com.qushe8r.expensemanager.member.entity.Member;
import org.mockito.ArgumentMatcher;

public class MemberMatcher implements ArgumentMatcher<Member> {

  private final Member left;

  public MemberMatcher(Member left) {
    this.left = left;
  }

  @Override
  public boolean matches(Member right) {
    boolean isSameId = isSameId(left, right);
    boolean isSameEmail = left.getEmail().equals(right.getEmail());
    boolean isSamePassword = left.getPassword().equals(right.getPassword());
    boolean isSameEvaluationAlarm = left.getEvaluationAlarm().equals(right.getEvaluationAlarm());
    boolean isSameRecommendationAlarm =
        left.getRecommendationAlarm().equals(right.getRecommendationAlarm());

    return isSameId
        && isSameEmail
        && isSamePassword
        && isSameEvaluationAlarm
        && isSameRecommendationAlarm;
  }

  private boolean isSameId(Member left, Member right) {
    if (left.getId() == null && right.getId() == null) {
      return true;
    }
    if (left.getId() != null && right.getId() != null) {
      return left.getId().equals(right.getId());
    }
    return false;
  }
}
