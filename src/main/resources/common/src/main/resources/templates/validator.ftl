package  ${packageName};

import com.dt.europe.hal.api.common.exceptions.InputParamInvalidException;
import com.dt.europe.hal.api.common.model.ValidatorContexEnum;
<#list serviceImports as si>
import ${si};
</#list>
import ${modelImport};
import ${businessValidatorImport};
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
<#if hasArray>
import org.springframework.util.CollectionUtils;
</#if>

@Slf4j
@org.springframework.stereotype.Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SuppressWarnings("Duplicates")
public class  ${className} {

private final Validator validator;
<#list serviceBeans as sb>
private final ${sb};
</#list>

  public void validate(${modelName} entity, ValidatorContexEnum context) throws InputParamInvalidException {
    Assert.notNull(entity, "entity is mandatory input");
    Assert.notNull(context, "Context is mandatory input");

    Set${hibernateValidator} violations = validator.validate(entity);
    if (!violations.isEmpty()) {
      log.error("validation error\n{}", violations);
      throw new InputParamInvalidException(violations.toString());
    }

    ${businessValidator}.validate(entity, context);

  <#list validators as v>
    if (entity.get${v.upperName}() != null) {
      ${v.validatorName}.validate(entity.get${v.upperName}(), context);
    }

  </#list>
  <#list validatorsArr as v>
    if (!CollectionUtils.isEmpty(entity.get${v.upperName}())) {
      ${v.validatorName}.validateAll(entity.get${v.upperName}(), context);
    }

  </#list>
  }

  public void validateAll(List${modelBracket} entities, ValidatorContexEnum context) throws InputParamInvalidException {
    Assert.notNull(entities, "entities is mandatory input");
    Assert.notEmpty(entities, "entities must not be empty");
    Assert.notNull(context, "Context is mandatory input");

    for (${modelName} entity : entities) {
      validate(entity, context);
    }
  }
}
