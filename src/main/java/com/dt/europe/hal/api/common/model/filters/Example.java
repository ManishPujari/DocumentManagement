package com.dt.europe.hal.api.common.model.filters;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"version", "request", "response"})
public class Example {

  @JsonProperty("version")
  private String version;

  @JsonProperty("request")
  private Request request;

  @JsonProperty("response")
  private Response response;
}
