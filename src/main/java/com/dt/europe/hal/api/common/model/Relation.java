package com.dt.europe.hal.api.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Relation {

  @JsonProperty("p")
  private RelationEntityEnum parent;
  @JsonProperty("var")
  private String variableName;
  @JsonProperty("c")
  private RelationEntityEnum child;
  @JsonProperty("pid")
  private String parentId;
  @JsonProperty("cid")
  private String childId;
}
