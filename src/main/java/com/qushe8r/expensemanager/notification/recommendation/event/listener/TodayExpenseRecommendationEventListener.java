package com.qushe8r.expensemanager.notification.recommendation.event.listener;

import com.qushe8r.expensemanager.notification.recommendation.dto.DailyExpenseRecommendationEvent;
import org.springframework.context.event.EventListener;

public interface TodayExpenseRecommendationEventListener {

  @EventListener
  void handle(DailyExpenseRecommendationEvent event);
}
