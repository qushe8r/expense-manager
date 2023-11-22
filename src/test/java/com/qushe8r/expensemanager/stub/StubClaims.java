package com.qushe8r.expensemanager.stub;

import io.jsonwebtoken.Claims;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class StubClaims implements Claims {

  @Override
  public String getIssuer() {
    return null;
  }

  @Override
  public String getSubject() {
    return null;
  }

  @Override
  public Set<String> getAudience() {
    return null;
  }

  @Override
  public Date getExpiration() {
    return null;
  }

  @Override
  public Date getNotBefore() {
    return null;
  }

  @Override
  public Date getIssuedAt() {
    return null;
  }

  @Override
  public String getId() {
    return null;
  }

  @Override
  public <T> T get(String claimName, Class<T> requiredType) {
    return null;
  }

  @Override
  public int size() {
    return 0;
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public boolean containsKey(Object key) {
    return false;
  }

  @Override
  public boolean containsValue(Object value) {
    return false;
  }

  @Override
  public Object get(Object key) {
    return null;
  }

  @Override
  public Object put(String key, Object value) {
    return null;
  }

  @Override
  public Object remove(Object key) {
    return null;
  }

  @Override
  public void putAll(Map<? extends String, ?> m) {}

  @Override
  public void clear() {}

  @Override
  public Set<String> keySet() {
    return null;
  }

  @Override
  public Collection<Object> values() {
    return null;
  }

  @Override
  public Set<Entry<String, Object>> entrySet() {
    return null;
  }
}
