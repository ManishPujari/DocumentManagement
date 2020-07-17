package  com.dt.europe.hal.api.documentmanagement.validator;

import com.dt.europe.hal.api.common.exceptions.InputParamInvalidException;
import com.dt.europe.hal.api.common.model.ValidatorContexEnum;
import com.dt.europe.hal.api.documentmanagement.model.RelatedObject;
import com.dt.europe.hal.api.documentmanagement.validator.business.RelatedObjectRulesValidator;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

@Slf4j
@org.springframework.stereotype.Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SuppressWarnings("Duplicates")
public class  RelatedObjectValidatorService {

private final Validator validator;

  public void validate(RelatedObject entity, ValidatorContexEnum context) throws InputParamInvalidException {
    Assert.notNull(entity, "entity is mandatory input");
    Assert.notNull(context, "Context is mandatory input");

    Set<ConstraintViolation<RelatedObject>> violations = validator.validate(entity);
    if (!violations.isEmpty()) {
      log.error("validation error\n{}", violations);
      throw new InputParamInvalidException(violations.toString());
    }

    RelatedObjectRulesValidator.validate(entity, context);

  }

  public void validateAll(List<RelatedObject> entities, ValidatorContexEnum context) throws InputParamInvalidException {
    Assert.notNull(entities, "entities is mandatory input");
    Assert.notEmpty(entities, "entities must not be empty");
    Assert.notNull(context, "Context is mandatory input");

    for (RelatedObject entity : entities) {
      validate(entity, context);
    }
  }
}
