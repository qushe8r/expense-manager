package com.qushe8r.expensemanager.member.entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class MemberDetails implements UserDetails {

  private final Long id;

  private final String email;

  private final String password;

  public MemberDetails(Long id, String email, String password) {
    this.id = id;
    this.email = email;
    this.password = password;
  }

  public Map<String, Object> claims() {
    Map<String, Object> claims = new HashMap<>();
    List<String> list = getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
    claims.put("roles", list);
    claims.put("id", id);
    return claims;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_USER"));
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
