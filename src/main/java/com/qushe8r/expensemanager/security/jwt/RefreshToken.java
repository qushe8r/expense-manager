package com.qushe8r.expensemanager.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@RedisHash("Refresh")
@AllArgsConstructor
public class RefreshToken {

  @Id private String id;

  @Indexed private String token;

  @Indexed private String email;

  @TimeToLive private Long expiration;
}
