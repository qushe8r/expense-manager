package com.qushe8r.expensemanager.security.jwt;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = JwtProperties.class)
class JwtPropertiesTest {

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  private JwtProperties jwtProperties;

  @DisplayName("readPropertiesValue(): 값을 읽었다면 null이 아니다.")
  @Test
  void readPropertiesValue() {
    Assertions.assertThat(jwtProperties.getSecret()).isNotNull();
    Assertions.assertThat(jwtProperties.getAccessTokenExpirationMinutes()).isNotNull();
    Assertions.assertThat(jwtProperties.getRefreshTokenExpirationMinutes()).isNotNull();
  }
}
