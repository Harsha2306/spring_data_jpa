package com.harsha.test.exceptions;

public class PageLimitExceededException extends RuntimeException {
  public PageLimitExceededException(String message) {
    super(message);
  }
}
