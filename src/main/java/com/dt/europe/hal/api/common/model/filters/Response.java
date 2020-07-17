package com.dt.europe.hal.api.common.model.filters;

import com.dt.europe.hal.api.common.model.tmf.Characteristic;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRawValue;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"status", "headers", "cookies", "body"})
public class Response {

  @JsonProperty("status")
  private String status;

  @JsonProperty("headers")
  private List<Characteristic> headers;

  @JsonProperty("cookies")
  private List<Characteristic> cookies;

  @JsonRawValue
  private String body;
}
