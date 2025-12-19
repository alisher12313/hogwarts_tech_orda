package com.pm.hogwarts.exceptionHandler;

public class ExternalApiE extends RuntimeException {
  public ExternalApiE(String message, Throwable cause) {
    super(message, cause);
  }
}
