package  com.dt.europe.hal.api.documentmanagement.validator;

import com.dt.europe.hal.api.common.exceptions.InputParamInvalidException;
import com.dt.europe.hal.api.common.model.ValidatorContexEnum;
import com.dt.europe.hal.api.common.validator.tmf.TimePeriodValidatorService;
import com.dt.europe.hal.api.documentmanagement.model.RelatedPartyRef;
import com.dt.europe.hal.api.documentmanagement.validator.business.RelatedPartyRefRulesValidator;
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
public class  RelatedPartyRefValidatorService {

private final Validator validator;
private final TimePeriodValidatorService timePeriodValidatorService;

  public void validate(RelatedPartyRef entity, ValidatorContexEnum context) throws InputParamInvalidException {
    Assert.notNull(entity, "entity is mandatory input");
    Assert.notNull(context, "Context is mandatory input");

    Set<ConstraintViolation<RelatedPartyRef>> violations = validator.validate(entity);
    if (!violations.isEmpty()) {
      log.error("validation error\n{}", violations);
      throw new InputParamInvalidException(violations.toString());
    }

    RelatedPartyRefRulesValidator.validate(entity, context);

    if (entity.getValidFor() != null) {
      timePeriodValidatorService.validate(entity.getValidFor(), context);
    }

  }

  public void validateAll(List<RelatedPartyRef> entities, ValidatorContexEnum context) throws InputParamInvalidException {
    Assert.notNull(entities, "entities is mandatory input");
    Assert.notEmpty(entities, "entities must not be empty");
    Assert.notNull(context, "Context is mandatory input");

    for (RelatedPartyRef entity : entities) {
      validate(entity, context);
    }
  }
}
