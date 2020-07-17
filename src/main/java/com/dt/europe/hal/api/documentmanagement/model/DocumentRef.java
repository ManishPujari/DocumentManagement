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

@ApiModel(description = "Reference to the related document.")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
public class DocumentRef extends BaseTemplate {

  @JsonProperty("id")
  @ApiModelProperty(value = "Id of the document")
  private String id;

  @JsonProperty("href")
  @ApiModelProperty(value = "A valid link to the document", position = 1)
  private String href;

  @Override
  public void validateInit() {

  }
}
