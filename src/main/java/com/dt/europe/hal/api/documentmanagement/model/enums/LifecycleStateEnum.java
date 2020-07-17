package com.dt.europe.hal.api.documentmanagement.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum LifecycleStateEnum {
  DRAFT("draft"),
  INPROGRESS("inProgress"),
  FINAL("final");

  private String value;

  LifecycleStateEnum(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static LifecycleStateEnum fromValue(String text) {
    for (LifecycleStateEnum b : LifecycleStateEnum.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }

}
