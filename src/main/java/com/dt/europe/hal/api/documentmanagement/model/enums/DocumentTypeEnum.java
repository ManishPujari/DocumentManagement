package com.dt.europe.hal.api.documentmanagement.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DocumentTypeEnum {
  INVOICE("invoice"),
  CONTRACT("contract"),
  OTHER("other");

  private String value;

  DocumentTypeEnum(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static DocumentTypeEnum fromValue(String text) {
    for (DocumentTypeEnum b : DocumentTypeEnum.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }

}
