package com.qushe8r.expensemanager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {

  @Test
  void contextLoads() {}

  @Test
  void applicationContextTest() {
    Application.main(new String[] {});
  }
}
