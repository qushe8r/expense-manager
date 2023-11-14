package com.qushe8r.expensemanager.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMemberPrincipalsSecurityContextFactory.class)
public @interface WithMemberPrincipals {

  String id() default "1";

  String email() default "test@email.com";

  String password() default "password";
}
