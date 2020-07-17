package com.dt.europe.hal.api.common.model.tmf.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SeverityEnum {
  FATAL("FATAL"), ERROR("ERROR"), WARN("WARN"), INFO("INFO"), DEBUG("DEBUG"), TRACE("TRACE");

  private String value;

  SeverityEnum(String value) {
    this.value = value;
  }

  @JsonCreator
  public static SeverityEnum fromValue(String text) {
    if (text == null) {
      return null;
    }
    if (text.isEmpty()) {
      return null;
    }
    for (SeverityEnum b : SeverityEnum.values()) {
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
