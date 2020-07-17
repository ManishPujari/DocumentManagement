package com.dt.europe.hal.api.common.model.tmf;

import com.dt.europe.hal.api.common.model.BaseTemplate;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@ApiModel(description = "")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
public class TimePeriod extends BaseTemplate {

  @ApiModelProperty(value = "Beginning of the time period", position = 0)
  @Valid
  @JsonProperty("startDateTime")
  private OffsetDateTime startDateTime;

  @ApiModelProperty(value = "End of the time period", position = 1)
  @Valid
  @JsonProperty("endDateTime")
  private OffsetDateTime endDateTime;

  @Override
  public void validateInit() {

  }
}
