package com.dt.europe.hal.api.common.exceptions;

public class InputParamInvalidException extends Exception {

  public InputParamInvalidException() {
  }

  public InputParamInvalidException(String message) {
    super(message);
  }

  public InputParamInvalidException(Throwable cause) {
    super(cause);
  }

  public InputParamInvalidException(String message, Throwable cause) {
    super(message, cause);
  }

  public InputParamInvalidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}

