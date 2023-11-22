package com.qushe8r.expensemanager.config;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

@Configuration
@EnableConfigurationProperties
public class DataSourceConfig {

  @Bean
  @ConfigurationProperties(prefix = "spring.datasource.read")
  public DataSource readDataSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean
  @ConfigurationProperties(prefix = "spring.datasource.write")
  public DataSource writeDataSource() {
    return DataSourceBuilder.create().build();
  }

  @Primary
  @Bean
  public DataSource testDataSource(
      @Qualifier("testRoutingDataSource") DataSource testRoutingDataSource) {
    return new LazyConnectionDataSourceProxy(testRoutingDataSource);
  }

  @Bean
  public DataSource testRoutingDataSource(
      @Qualifier("readDataSource") DataSource readDataSource,
      @Qualifier("writeDataSource") DataSource writeDataSource,
      DataSourceSelector dataSourceSelector) {
    TestRoutingDataSource testRoutingDataSource = new TestRoutingDataSource(dataSourceSelector);
    Map<Object, Object> dataSourceMap = new HashMap<>();

    dataSourceMap.put("write", writeDataSource);
    dataSourceMap.put("read", readDataSource);

    testRoutingDataSource.setTargetDataSources(dataSourceMap);
    testRoutingDataSource.setDefaultTargetDataSource(writeDataSource);

    return testRoutingDataSource;
  }
}
