package com.dt.europe.hal.api.common.model;

import com.dt.europe.hal.api.common.model.tmf.Characteristic;
import com.dt.europe.hal.api.common.model.tmf.Error;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseParams {

  @JsonProperty("errorResponse")
  private Error errorResponse;

  @JsonProperty("headers")
  private List<Characteristic> headers = new ArrayList<>();

  @JsonProperty("cookies")
  private List<Characteristic> cookies = new ArrayList<>();

  @JsonProperty("bodyRefs")
  private String bodyRefs;

  @JsonProperty("objectRef")
  private String objectRef;
}
