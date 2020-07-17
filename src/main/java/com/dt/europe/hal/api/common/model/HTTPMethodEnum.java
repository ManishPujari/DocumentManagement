package com.dt.europe.hal.api.common.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum HTTPMethodEnum {
  GET("GET"), POST("POST"), PATCH("PATCH"), PUT("PUT"), DELETE("DELETE"), HEAD("HEAD");

  private String value;

  HTTPMethodEnum(String value) {
    this.value = value;
  }

  @JsonCreator
  public static HTTPMethodEnum fromValue(String text) {
    for (HTTPMethodEnum b : HTTPMethodEnum.values()) {
      if (String.valueOf(b.value)
                .equals(text)) {
        return b;
      }
    }
    return null;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

}
