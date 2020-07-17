package ${packageName};

import com.dt.europe.hal.api.common.model.ValidatorContexEnum;
import ${modelImport};
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

@Slf4j
public class ${className} {

  public static void validate(${modelName} entity, ValidatorContexEnum context) {
    Assert.notNull(entity, "entity is mandatory input");
    Assert.notNull(context, "Context is mandatory input");

  }
}
