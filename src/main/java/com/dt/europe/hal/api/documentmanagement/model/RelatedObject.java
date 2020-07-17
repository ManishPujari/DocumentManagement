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
@ApiModel(description = "Used to provide Info on Related Object.")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
public class RelatedObject extends BaseTemplate {

  @JsonProperty("id")
  @ApiModelProperty(value = "Related object identity")
  private String id;

  @JsonProperty("entityType")
  @ApiModelProperty(value = "Class type of resource", position = 10)
  private String entityType;

  @JsonProperty("involvement")
  @ApiModelProperty(value = "Involvement of the related object (for instance disputed, adjusted)", position = 20)
  private String involvement;

  @JsonProperty("reference")
  @ApiModelProperty(value = "Reference of the related object", position = 30)
  private String reference;

  @Override
  public void validateInit() {

  }
}
