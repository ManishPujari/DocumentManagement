package com.dt.europe.hal.api.common.model.postman;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Request {

  @JsonProperty("method")
  public String method;

  @JsonProperty("header")
  @Builder.Default
  public List<Header> header = new ArrayList<>();

  @JsonProperty("body")
  public Body body;

  @JsonProperty("url")
  public Url url;

  @JsonProperty("description")
  public String description;
}
