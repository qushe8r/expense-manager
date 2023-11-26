package com.qushe8r.expensemanager.integration.notification;

import com.qushe8r.expensemanager.config.DataSourceSelector;
import com.qushe8r.expensemanager.notification.evaluation.event.publisher.DailyExpenseEvaluationPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DailyExpenseEvaluationIntegrationTest {

  @Autowired private DailyExpenseEvaluationPublisher dailyExpenseEvaluationPublisher;

  @Autowired private DataSourceSelector dataSourceSelector;

  @BeforeEach
  void setUp() {
    dataSourceSelector.toRead();
  }

  @Test
  void dailyExpenseRecommendation() {
    dailyExpenseEvaluationPublisher.dailyExpenseRecommendation();
  }
}
