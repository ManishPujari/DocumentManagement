package com.dt.europe.hal.api.common.model;

import com.dt.europe.hal.api.common.utils.FileReaderUtil;
import com.dt.europe.hal.api.common.utils.ReflectionUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import io.swagger.annotations.ApiParam;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

@NoArgsConstructor
@Getter
@Setter
@Slf4j
public abstract class BaseTemplate {

  @JsonIgnore
  @ApiParam(hidden = true)
  private ValidationModeEnum classValidationType = ValidationModeEnum.IMPLEMENTS;

  @JsonIgnore
  @ApiParam(hidden = true)
  private EnumValidationModeEnum enumValidationType = EnumValidationModeEnum.IMPLEMENTS;


  private String findByPath(String path) {
    Assert.notNull(path, "Path is null");

    String rootPath =
      FileReaderUtil.getProjectFolder(BaseTemplate.class) + ".src.main.resources.object-templates.".replace(".", File.separator) + path + ".json";

    String jsonFile;
    try {
      jsonFile = FileReaderUtil.getJsonFile(rootPath);
    } catch (IOException e) {
      log.error("File read exception", e);
      throw new RuntimeException(e);
    }
    return jsonFile;
  }

  private Map<String, ClassParams> getTemplateParams(String jsonFile) {
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectReader objectReader = objectMapper.reader()
      .forType(new TypeReference<List<ClassParams>>() {
      });
    List<ClassParams> classParams;
    try {
      classParams = objectReader.readValue(jsonFile);
    } catch (IOException e) {
      log.error("Json mapper exception", e);
      throw new RuntimeException(e);
    }
    return classParams.stream().collect(Collectors.toMap(ClassParams::getFullName, Function.identity()));
  }

  private void validateEnumValues(final String key, final Class clazz, final ClassParams templateParams, final ClassParams objectParams) {
    if (enumValidationType.compareTo(EnumValidationModeEnum.OVERRIDES) == 0) {
      return;
    }
    List<String> templateEnums = templateParams.getEnumValues();
    List<String> objectEnums = objectParams.getEnumValues();
    Assert.isTrue(templateEnums.containsAll(objectEnums),
      "Enum " + key + " for class " + clazz.getName() + " does not contain all values from template " + templateParams);

    if (enumValidationType.compareTo(EnumValidationModeEnum.IMPLEMENTS) == 0) {
      Assert
        .isTrue(objectEnums.containsAll(templateEnums), "Enum " + key + " for class " + clazz.getName() + " has more values then template " + templateParams);
    }
  }

  public void validate(Class clazz, String templatePath) {
    Assert.notNull(templatePath, "Template path must not be null");
    Assert.notNull(clazz, "Class must not be null");

    Map<String, ClassParams> objectFieldMap = ReflectionUtils.getClassParams(clazz);
    Map<String, ClassParams> templateFieldMap = getTemplateParams(findByPath(templatePath));

    templateFieldMap.keySet().forEach(key -> Assert.isTrue(objectFieldMap.containsKey(key),
      "Field " + key + " from template " + templatePath + " not found in reference " + clazz));

    if (classValidationType.compareTo(ValidationModeEnum.IMPLEMENTS) == 0) {
      objectFieldMap.keySet().forEach(key -> Assert.isTrue(templateFieldMap.containsKey(key),
        "Field " + key + " from object " + clazz + " not found in template " + templatePath));
    }

    objectFieldMap.values().stream().filter(classParams -> classParams.getEnumValues() != null).forEach(classParams -> {
      if (enumValidationType.compareTo(EnumValidationModeEnum.IMPLEMENTS) == 0 || enumValidationType.compareTo(EnumValidationModeEnum.EXTENDS) == 0) {
        Assert.isTrue(templateFieldMap.containsKey(classParams.getFullName()),
          "Enum field " + classParams.getFullName() + "for class " + clazz.getName() + " tno found in template " + templatePath);
      }

      validateEnumValues(classParams.getFullName(), clazz, classParams, templateFieldMap.get(classParams.getFullName()));

    });
  }

  public abstract void validateInit();

}
