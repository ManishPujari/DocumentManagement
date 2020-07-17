package com.dt.europe.hal.api.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Variable implements Comparable {

  private String type;
  private String name;
  private String upperName;
  private String enumName;
  private String serviceName;
  private String serviceClass;
  private String serviceImportPackage;
  private String validatorName;
  private String validatorClass;
  private String validatorImportPackage;
  private boolean isList;
  private boolean isImported;

  @Override
  public int compareTo(Object o) {
    if (!(o instanceof Variable)) {
      throw new RuntimeException("comparing not compatible objects");
    }

    return (this.getName().compareTo(((Variable) o).getName()));
  }
}
