package com.dt.europe.hal.api.common.validator.tmf.business;

import com.dt.europe.hal.api.common.model.ValidatorContexEnum;
import com.dt.europe.hal.api.common.model.tmf.Characteristic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

@Slf4j
public class CharacteristicRulesValidator {

  public static void validate(Characteristic entity, ValidatorContexEnum context) {
    Assert.notNull(entity, "entity is mandatory input");
    Assert.notNull(context, "Context is mandatory input");

  }
}
