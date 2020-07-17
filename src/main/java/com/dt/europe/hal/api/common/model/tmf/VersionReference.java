package com.dt.europe.hal.api.common.model.tmf;

import com.dt.europe.hal.api.common.model.BaseTemplate;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@ApiModel(description = "Generic link to another versioning resource")
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VersionReference extends BaseTemplate {

  @ApiModelProperty(value = "Unique identifier of the Reference")
  @JsonProperty("id")
  @NotBlank
  private String id;

  @ApiModelProperty(value = "URI of the Reference", position = 1)
  @JsonProperty("href")
  private String href;

  @ApiModelProperty(value = "Name of the Reference", position = 2)
  @JsonProperty("name")
  private String name;

  @ApiModelProperty(value = "Version of the Reference", position = 3)
  @JsonProperty("version")
  @NotBlank
  private String version;

  @Override
  public void validateInit() {
  }
}
