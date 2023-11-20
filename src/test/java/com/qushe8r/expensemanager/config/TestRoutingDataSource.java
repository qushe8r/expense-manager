package com.qushe8r.expensemanager.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class TestRoutingDataSource extends AbstractRoutingDataSource {

  private final DataSourceSelector dataSourceSelector;

  public TestRoutingDataSource(DataSourceSelector dataSourceSelector) {
    this.dataSourceSelector = dataSourceSelector;
  }

  @Override
  protected Object determineCurrentLookupKey() {
    return dataSourceSelector.getSelected();
  }
}
