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
public class PostmanCollection {

  @JsonProperty("info")
  public Info info;

  @JsonProperty("item")
  @Builder.Default
  public List<Item> item = new ArrayList<>();
}
