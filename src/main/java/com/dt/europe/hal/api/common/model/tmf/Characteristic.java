package com.dt.europe.hal.api.common.model.tmf;

import com.dt.europe.hal.api.common.model.BaseTemplate;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class Characteristic extends BaseTemplate {

  @ApiModelProperty(value = "characteristic name")
  @JsonProperty("name")
  @NotBlank
  private String name;

  @ApiModelProperty(value = "characteristic value", position = 1)
  @JsonProperty("value")
  private String value;

  @Override
  public void validateInit() {

  }

}

