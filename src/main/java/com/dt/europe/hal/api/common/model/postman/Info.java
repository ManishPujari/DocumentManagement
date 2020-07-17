package com.dt.europe.hal.api.common.model.postman;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Info {

  @JsonProperty("_postman_id")
  public String postmanId;

  @JsonProperty("name")
  public String name;

  @JsonProperty("schema")
  public String schema;
}
