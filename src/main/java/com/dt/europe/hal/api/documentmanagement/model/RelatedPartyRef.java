package com.dt.europe.hal.api.documentmanagement.model;

import com.dt.europe.hal.api.common.model.BaseTemplate;
import com.dt.europe.hal.api.common.model.tmf.TimePeriod;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@ApiModel(description = "Used to provide Info on Related Party.")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
public class RelatedPartyRef extends BaseTemplate {

  @JsonProperty("id")
  @ApiModelProperty(value = "Unique identifier of a related party")
  private String id;

  @JsonProperty("href")
  @ApiModelProperty(value = "Reference of the related party, could be a party reference "
    + "or a party role reference.", position = 1)
  private String href;

  @JsonProperty("role")
  @ApiModelProperty(value = "Role of the related party", position = 2)
  private String role;

  @JsonProperty("name")
  @ApiModelProperty(value = "Name of the related party", position = 3)
  private String name;

  @JsonProperty("validFor")
  @ApiModelProperty(value = "The time period this related party is valid for", position = 4)
  private TimePeriod validFor;

  @Override
  public void validateInit() {

  }
}
