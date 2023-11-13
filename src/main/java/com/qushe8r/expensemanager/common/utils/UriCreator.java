package com.qushe8r.expensemanager.common.utils;

import java.net.URI;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.util.UriComponentsBuilder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UriCreator {

  public static URI createUri(String defaultUrl, Long resourceId) {
    return UriComponentsBuilder.newInstance()
        .path(defaultUrl + "/{resourceId}")
        .buildAndExpand(resourceId)
        .toUri();
  }
}
