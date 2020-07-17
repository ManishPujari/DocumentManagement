package com.dt.europe.hal.api.documentmanagement.model;

import com.dt.europe.hal.api.common.model.BaseTemplate;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@ApiModel(description = "Used to provide Info on Document Specification.")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
public class DocumentSpecification extends BaseTemplate {

  @JsonProperty("id")
  @ApiModelProperty(value = "Document specification id")
  private String id;

  @JsonProperty("href")
  @ApiModelProperty(value = "A valid link to the specification", position = 1)
  private String href;

  @JsonProperty("name")
  @ApiModelProperty(value = "Name of the specification", position = 2)
  private String name;

  @JsonProperty("url")
  @ApiModelProperty(value = "The URL of the specification", position = 3)
  private String url;

  @JsonProperty("version")
  @ApiModelProperty(value = "Specification version", position = 4)
  private String version;

  @Override
  public void validateInit() {

  }
}
