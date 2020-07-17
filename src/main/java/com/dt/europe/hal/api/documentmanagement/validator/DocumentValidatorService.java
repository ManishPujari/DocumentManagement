package  com.dt.europe.hal.api.documentmanagement.validator;

import com.dt.europe.hal.api.common.exceptions.InputParamInvalidException;
import com.dt.europe.hal.api.common.model.ValidatorContexEnum;
import com.dt.europe.hal.api.common.validator.tmf.CharacteristicValidatorService;
import com.dt.europe.hal.api.documentmanagement.model.Document;
import com.dt.europe.hal.api.documentmanagement.validator.business.DocumentRulesValidator;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

@Slf4j
@org.springframework.stereotype.Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SuppressWarnings("Duplicates")
public class  DocumentValidatorService {

private final Validator validator;
private final AttachmentValidatorService attachmentValidatorService;
private final CharacteristicValidatorService characteristicValidatorService;
private final DocumentRelationshipValidatorService documentRelationshipValidatorService;
private final DocumentSpecificationValidatorService documentSpecificationValidatorService;
private final RelatedObjectValidatorService relatedObjectValidatorService;
private final RelatedPartyRefValidatorService relatedPartyRefValidatorService;

  public void validate(Document entity, ValidatorContexEnum context) throws InputParamInvalidException {
    Assert.notNull(entity, "entity is mandatory input");
    Assert.notNull(context, "Context is mandatory input");

    Set<ConstraintViolation<Document>> violations = validator.validate(entity);
    if (!violations.isEmpty()) {
      log.error("validation error\n{}", violations);
      throw new InputParamInvalidException(violations.toString());
    }

    DocumentRulesValidator.validate(entity, context);

    if (entity.getRelatedObject() != null) {
      relatedObjectValidatorService.validate(entity.getRelatedObject(), context);
    }

    if (entity.getRelatedParty() != null) {
      relatedPartyRefValidatorService.validate(entity.getRelatedParty(), context);
    }

    if (!CollectionUtils.isEmpty(entity.getAttachments())) {
      attachmentValidatorService.validateAll(entity.getAttachments(), context);
    }

    if (!CollectionUtils.isEmpty(entity.getCharacteristics())) {
      characteristicValidatorService.validateAll(entity.getCharacteristics(), context);
    }

    if (!CollectionUtils.isEmpty(entity.getDocumentRelationships())) {
      documentRelationshipValidatorService.validateAll(entity.getDocumentRelationships(), context);
    }

    if (!CollectionUtils.isEmpty(entity.getDocumentSpecifications())) {
      documentSpecificationValidatorService.validateAll(entity.getDocumentSpecifications(), context);
    }

  }

  public void validateAll(List<Document> entities, ValidatorContexEnum context) throws InputParamInvalidException {
    Assert.notNull(entities, "entities is mandatory input");
    Assert.notEmpty(entities, "entities must not be empty");
    Assert.notNull(context, "Context is mandatory input");

    for (Document entity : entities) {
      validate(entity, context);
    }
  }
}
