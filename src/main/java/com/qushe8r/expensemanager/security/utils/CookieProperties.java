package com.qushe8r.expensemanager.security.utils;

import com.qushe8r.expensemanager.common.config.YamlPropertyConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("cookie")
@PropertySource(value = "classpath:application.yml", factory = YamlPropertyConfig.class)
public class CookieProperties {

  private String domain;
}
