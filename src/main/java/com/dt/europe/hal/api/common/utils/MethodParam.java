package com.dt.europe.hal.api.common.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MethodParam {

  private String apiName;
  private String name;
  private String type;
  @Builder.Default
  private boolean isBody = false;
  @Builder.Default
  private boolean isEnum = false;
  @Builder.Default
  private boolean isPathParam = false;
}
