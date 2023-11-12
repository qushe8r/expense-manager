package com.qushe8r.expensemanager.common.config;

import jakarta.annotation.Nullable;
import java.util.Objects;
import java.util.Properties;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.util.StringUtils;

public class YamlPropertyConfig implements PropertySourceFactory {

  @SuppressWarnings("NullableProblems")
  @Override
  public PropertySource<?> createPropertySource(
      @Nullable String name, EncodedResource encodedResource) {
    YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
    factory.setResources(encodedResource.getResource());
    Properties propertySource = factory.getObject();
    String propertyName =
        StringUtils.hasText(name) ? name : encodedResource.getResource().getFilename();
    return new PropertiesPropertySource(
        Objects.requireNonNull(propertyName), Objects.requireNonNull(propertySource));
  }
}
