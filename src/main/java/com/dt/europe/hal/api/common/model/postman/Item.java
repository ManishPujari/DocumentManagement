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
public class Item {

  @JsonProperty("_postman_id")
  public String postmanId;

  @JsonProperty("name")
  public String name;

  @JsonProperty("request")
  public Request request;

  @JsonProperty("response")
  @Builder.Default
  public List<Object> response = new ArrayList<>();
}
