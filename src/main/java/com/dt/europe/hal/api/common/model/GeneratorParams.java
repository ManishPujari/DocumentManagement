package com.dt.europe.hal.api.common.model;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneratorParams {

  private String className;
  private String modelPackageName;
  private String servicePackageName;
  private String validatorPackageName;
  @Builder.Default
  private List<Variable> variables = new ArrayList<>();
}
