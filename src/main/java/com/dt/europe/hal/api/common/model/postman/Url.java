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
public class Url {

  @JsonProperty("raw")
  public String raw;

  @JsonProperty("host")
  @Builder.Default
  public List<String> host = new ArrayList<>();

  @JsonProperty("path")
  @Builder.Default
  public List<String> path = new ArrayList<>();

  @JsonProperty("query")
  @Builder.Default
  public List<Query> query = new ArrayList<>();
}
