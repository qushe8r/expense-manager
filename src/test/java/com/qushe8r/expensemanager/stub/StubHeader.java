package com.qushe8r.expensemanager.stub;

import io.jsonwebtoken.Header;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class StubHeader implements Header {

  @Override
  public String getType() {
    return null;
  }

  @Override
  public String getContentType() {
    return null;
  }

  @Override
  public String getAlgorithm() {
    return null;
  }

  @Override
  public String getCompressionAlgorithm() {
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
