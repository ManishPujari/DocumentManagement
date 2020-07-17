package com.dt.europe.hal.api.common.model;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@AllArgsConstructor
@Data
@RequiredArgsConstructor
public class Header {

  @NotNull
  private String name = null;

  @NotNull
  private String value = null;
}
