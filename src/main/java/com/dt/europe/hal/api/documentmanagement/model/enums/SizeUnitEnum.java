package com.dt.europe.hal.api.documentmanagement.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SizeUnitEnum {
  BYTES("bytes");

  private String value;

  SizeUnitEnum(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static SizeUnitEnum fromValue(String text) {
    for (SizeUnitEnum b : SizeUnitEnum.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }

}
