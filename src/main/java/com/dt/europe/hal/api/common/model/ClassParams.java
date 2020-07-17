package com.dt.europe.hal.api.common.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassParams {

  private String fullName;
  private List<String> enumValues;
}
