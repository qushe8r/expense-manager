package com.qushe8r.expensemanager.stub;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ThrowExceptionController {

  @PostMapping("/exceptions")
  public ResponseEntity<Void> throwException() throws Exception {
    throw new Exception();
  }
}
