package  com.dt.europe.hal.api.documentmanagement.validator;

import com.dt.europe.hal.api.common.exceptions.InputParamInvalidException;
import com.dt.europe.hal.api.common.model.ValidatorContexEnum;
import com.dt.europe.hal.api.documentmanagement.model.DocumentRelationship;
import com.dt.europe.hal.api.documentmanagement.validator.business.DocumentRelationshipRulesValidator;
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
public class  DocumentRelationshipValidatorService {

private final Validator validator;
private final DocumentRefValidatorService documentRefValidatorService;

  public void validate(DocumentRelationship entity, ValidatorContexEnum context) throws InputParamInvalidException {
    Assert.notNull(entity, "entity is mandatory input");
    Assert.notNull(context, "Context is mandatory input");

    Set<ConstraintViolation<DocumentRelationship>> violations = validator.validate(entity);
    if (!violations.isEmpty()) {
      log.error("validation error\n{}", violations);
      throw new InputParamInvalidException(violations.toString());
    }

    DocumentRelationshipRulesValidator.validate(entity, context);

    if (entity.getDocument() != null) {
      documentRefValidatorService.validate(entity.getDocument(), context);
    }

  }

  public void validateAll(List<DocumentRelationship> entities, ValidatorContexEnum context) throws InputParamInvalidException {
    Assert.notNull(entities, "entities is mandatory input");
    Assert.notEmpty(entities, "entities must not be empty");
    Assert.notNull(context, "Context is mandatory input");

    for (DocumentRelationship entity : entities) {
      validate(entity, context);
    }
  }
}
