package com.qushe8r.expensemanager.security.repository;

import com.qushe8r.expensemanager.security.jwt.RefreshToken;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisOperations;

@DataRedisTest(properties = "spring.data.redis.port=36379")
class RefreshTokenRepositoryTest {

  private static final Charset CHARSET = StandardCharsets.UTF_8;

  @Autowired private RefreshTokenRepository refreshTokenRepository;

  @Autowired private RedisOperations<Object, Object> operations;

  @AfterEach
  @BeforeEach
  void redisInitialize() {
    operations.execute(
        (RedisConnection connection) -> {
          connection.serverCommands().flushDb();
          return "OK";
        });
  }

  @DisplayName("save(): Redis에 RefreshToken을 저장한다.")
  @Test
  void save() {
    // given
    RefreshToken refreshToken = new RefreshToken("id", "token", "email", 10L);

    // when
    refreshTokenRepository.save(refreshToken);

    // then
    Assertions.assertThat(
            operations.execute(
                (RedisConnection connection) ->
                    connection
                        .keyCommands()
                        .exists(("Refresh:" + refreshToken.getId()).getBytes(CHARSET))))
        .isTrue();
  }

  @DisplayName("findById(): Redis에서 token(id)으로 찾을 수 있다.")
  @Test
  void findById() {
    // given
    RefreshToken refreshToken = new RefreshToken("id", "token", "email", 10L);
    RefreshToken saved = refreshTokenRepository.save(refreshToken);

    // when
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    RefreshToken find = refreshTokenRepository.findById(saved.getId()).get();

    // then
    Assertions.assertThat(find)
        .hasFieldOrPropertyWithValue("token", refreshToken.getToken())
        .hasFieldOrPropertyWithValue("email", refreshToken.getEmail());
  }

  @DisplayName("timeToLive(): ttl 설정한 시간이 지나면 찾을 수 없다.")
  @Test
  void timeToLive() {
    // given
    RefreshToken refreshToken = new RefreshToken("id", "token", "email", 1L);
    RefreshToken saved = refreshTokenRepository.save(refreshToken);

    // When & then
    Awaitility.with()
        .conditionEvaluationListener(condition -> System.out.println("isThere?:"))
        .await()
        .atMost(refreshToken.getExpiration() * 2, TimeUnit.SECONDS)
        .pollInterval(Duration.ofMillis(100))
        .until(() -> refreshTokenRepository.findById(saved.getToken()).isEmpty());
  }

  @DisplayName("findbyEmail(): Redis에서 email(index)로 찾을 수 있다.")
  @Test
  void findByEmail() {
    // given
    RefreshToken token1 = new RefreshToken("id#1", "token#1", "email#1", 1L);
    RefreshToken token2 = new RefreshToken("id#2", "token#2", "email#1", 1L);
    refreshTokenRepository.saveAll(List.of(token1, token2));

    // when
    List<RefreshToken> result = refreshTokenRepository.findByEmail(token1.getEmail());

    // then
    Assertions.assertThat(result).isNotEmpty();
    Assertions.assertThat(result.get(0))
        .hasFieldOrPropertyWithValue("token", "token#1")
        .hasFieldOrPropertyWithValue("email", "email#1");
    Assertions.assertThat(result.get(1))
        .hasFieldOrPropertyWithValue("token", "token#2")
        .hasFieldOrPropertyWithValue("email", "email#1");
  }

  @DisplayName("timeToLive2(): ttl 설정한 시간이 지나면 찾을 수 없다.")
  @Test
  void timeToLive2() {
    // given
    RefreshToken token1 = new RefreshToken("id#1", "token#1", "email#1", 1L);
    RefreshToken token2 = new RefreshToken("id#2", "token#2", "email#1", 1L);
    refreshTokenRepository.saveAll(List.of(token1, token2));

    // when
    refreshTokenRepository.findByEmail(token1.getEmail());

    // then
    Awaitility.with()
        .conditionEvaluationListener(condition -> System.out.println("isThere?:"))
        .await()
        .atMost(token1.getExpiration() * 2, TimeUnit.SECONDS)
        .pollInterval(Duration.ofMillis(100))
        .until(() -> refreshTokenRepository.findByEmail(token1.getEmail()).isEmpty());
  }
}
