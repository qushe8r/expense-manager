package com.qushe8r.expensemanager.annotation;

import com.qushe8r.expensemanager.member.entity.MemberDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMemberPrincipalsSecurityContextFactory
    implements WithSecurityContextFactory<WithMemberPrincipals> {

  @Override
  public SecurityContext createSecurityContext(WithMemberPrincipals annotation) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    MemberDetails memberDetails =
        new MemberDetails(Long.valueOf(annotation.id()), annotation.email(), annotation.password());
    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(memberDetails, "", memberDetails.getAuthorities());
    context.setAuthentication(authentication);
    return context;
  }
}
