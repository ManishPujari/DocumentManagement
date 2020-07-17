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

@ApiModel(description = "Used to provide Info on Document Relationship to other documents.")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
public class DocumentRelationship extends BaseTemplate {

  @JsonProperty("type")
  @ApiModelProperty(value = "Indicates relationship type: e.g related")
  private String type;

  @JsonProperty("document")
  @ApiModelProperty(value = "Document refs", position = 1)
  private DocumentRef document;

  @Override
  public void validateInit() {

  }
}
