package com.dt.europe.hal.api.documentmanagement.validator.business;

import com.dt.europe.hal.api.common.model.ValidatorContexEnum;
import com.dt.europe.hal.api.documentmanagement.model.Document;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

@Slf4j
public class DocumentRulesValidator {

  public static void validate(Document entity, ValidatorContexEnum context) {
    Assert.notNull(entity, "entity is mandatory input");
    Assert.notNull(context, "Context is mandatory input");

  }
}
