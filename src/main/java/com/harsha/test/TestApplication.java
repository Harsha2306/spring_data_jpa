package com.harsha.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@Slf4j
public class TestApplication {
  public static void main(String[] args) {
    ApplicationContext applicationContext = SpringApplication.run(TestApplication.class, args);
    //TODO add git
    log.info("Started {} on port 8080", applicationContext.getId());
  }
}
