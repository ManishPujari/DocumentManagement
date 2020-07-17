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
public class Header {

  @JsonProperty("key")
  public String key;

  @JsonProperty("value")
  public String value;

  @JsonProperty("type")
  public String type;

  @JsonProperty("name")
  public String name;
}
