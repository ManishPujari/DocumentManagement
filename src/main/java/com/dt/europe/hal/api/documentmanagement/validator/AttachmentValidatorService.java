package  com.dt.europe.hal.api.documentmanagement.validator;

import com.dt.europe.hal.api.common.exceptions.InputParamInvalidException;
import com.dt.europe.hal.api.common.model.ValidatorContexEnum;
import com.dt.europe.hal.api.common.validator.tmf.TimePeriodValidatorService;
import com.dt.europe.hal.api.documentmanagement.model.Attachment;
import com.dt.europe.hal.api.documentmanagement.validator.business.AttachmentRulesValidator;
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
public class  AttachmentValidatorService {

private final Validator validator;
private final TimePeriodValidatorService timePeriodValidatorService;

  public void validate(Attachment entity, ValidatorContexEnum context) throws InputParamInvalidException {
    Assert.notNull(entity, "entity is mandatory input");
    Assert.notNull(context, "Context is mandatory input");

    Set<ConstraintViolation<Attachment>> violations = validator.validate(entity);
    if (!violations.isEmpty()) {
      log.error("validation error\n{}", violations);
      throw new InputParamInvalidException(violations.toString());
    }

    AttachmentRulesValidator.validate(entity, context);

    if (entity.getValidFor() != null) {
      timePeriodValidatorService.validate(entity.getValidFor(), context);
    }

  }

  public void validateAll(List<Attachment> entities, ValidatorContexEnum context) throws InputParamInvalidException {
    Assert.notNull(entities, "entities is mandatory input");
    Assert.notEmpty(entities, "entities must not be empty");
    Assert.notNull(context, "Context is mandatory input");

    for (Attachment entity : entities) {
      validate(entity, context);
    }
  }
}
