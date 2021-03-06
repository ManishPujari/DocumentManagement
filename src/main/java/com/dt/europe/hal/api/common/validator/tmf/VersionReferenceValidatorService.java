package  com.dt.europe.hal.api.common.validator.tmf;

import com.dt.europe.hal.api.common.exceptions.InputParamInvalidException;
import com.dt.europe.hal.api.common.model.ValidatorContexEnum;
import com.dt.europe.hal.api.common.model.tmf.VersionReference;
import com.dt.europe.hal.api.common.validator.tmf.business.VersionReferenceRulesValidator;
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
public class  VersionReferenceValidatorService {

private final Validator validator;

  public void validate(VersionReference entity, ValidatorContexEnum context) throws InputParamInvalidException {
    Assert.notNull(entity, "entity is mandatory input");
    Assert.notNull(context, "Context is mandatory input");

    Set<ConstraintViolation<VersionReference>> violations = validator.validate(entity);
    if (!violations.isEmpty()) {
      log.error("validation error\n{}", violations);
      throw new InputParamInvalidException(violations.toString());
    }

    VersionReferenceRulesValidator.validate(entity, context);

  }

  public void validateAll(List<VersionReference> entities, ValidatorContexEnum context) throws InputParamInvalidException {
    Assert.notNull(entities, "entities is mandatory input");
    Assert.notEmpty(entities, "entities must not be empty");
    Assert.notNull(context, "Context is mandatory input");

    for (VersionReference entity : entities) {
      validate(entity, context);
    }
  }
}
