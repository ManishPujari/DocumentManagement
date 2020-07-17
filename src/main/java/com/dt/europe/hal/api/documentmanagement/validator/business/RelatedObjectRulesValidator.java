package com.dt.europe.hal.api.documentmanagement.validator.business;

import com.dt.europe.hal.api.common.model.ValidatorContexEnum;
import com.dt.europe.hal.api.documentmanagement.model.RelatedObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

@Slf4j
public class RelatedObjectRulesValidator {

  public static void validate(RelatedObject entity, ValidatorContexEnum context) {
    Assert.notNull(entity, "entity is mandatory input");
    Assert.notNull(context, "Context is mandatory input");

  }
}
