package com.dt.europe.hal.api.common.exceptions;

public class ExampleException extends Exception {

  public ExampleException() {
  }

  public ExampleException(String message) {
    super(message);
  }

  public ExampleException(Throwable cause) {
    super(cause);
  }

  public ExampleException(String message, Throwable cause) {
    super(message, cause);
  }

  public ExampleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}

