package  com.dt.europe.hal.api.common.validator.tmf;

import com.dt.europe.hal.api.common.exceptions.InputParamInvalidException;
import com.dt.europe.hal.api.common.model.ValidatorContexEnum;
import com.dt.europe.hal.api.common.model.tmf.TimePeriod;
import com.dt.europe.hal.api.common.validator.tmf.business.TimePeriodRulesValidator;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SuppressWarnings("Duplicates")
@Profile("common")
public class  TimePeriodValidatorService {

private final Validator validator;

  public void validate(TimePeriod entity, ValidatorContexEnum context) throws InputParamInvalidException {
    Assert.notNull(entity, "entity is mandatory input");
    Assert.notNull(context, "Context is mandatory input");

    Set<ConstraintViolation<TimePeriod>> violations = validator.validate(entity);
    if (!violations.isEmpty()) {
      log.error("validation error\n{}", violations);
      throw new InputParamInvalidException(violations.toString());
    }

    TimePeriodRulesValidator.validate(entity, context);

  }

  public void validateAll(List<TimePeriod> entities, ValidatorContexEnum context) throws InputParamInvalidException {
    Assert.notNull(entities, "entities is mandatory input");
    Assert.notEmpty(entities, "entities must not be empty");
    Assert.notNull(context, "Context is mandatory input");

    for (TimePeriod entity : entities) {
      validate(entity, context);
    }
  }
}
