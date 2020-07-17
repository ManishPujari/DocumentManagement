package com.dt.europe.hal.api.common.utils;

import com.dt.europe.hal.api.common.model.ClassParams;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

@Getter
@Setter
@Slf4j
public class TemplateGeneratorHelper {

  private final ObjectMapper mapper;
  private Class clazz;

  public TemplateGeneratorHelper(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  public void generateTemplate() {
    Assert.notNull(clazz, "Class not set");

    Map<String, ClassParams> templateFieldMap = ReflectionUtils.getClassParams(clazz);

    ObjectWriter writer = mapper.writer();

    String output = null;
    try {
      output = SwaggerUtil.beautifyJson(writer.withDefaultPrettyPrinter().writeValueAsString(templateFieldMap.values()));
      String directoryPath = ReflectionUtils.getFolderForWrite(clazz);
      String coreFolder =
        directoryPath.substring(0, directoryPath.lastIndexOf(File.separator)) + ".common.src.main.resources.object-templates.".replace(".", File.separator);
      String subFolder = directoryPath.substring(directoryPath.lastIndexOf(File.separator) + 1);
      String fileName = coreFolder + subFolder + File.separator + clazz.getSimpleName() + ".json";
      String directoryName = coreFolder + subFolder;
      File directory = new File(directoryName);
      if (!directory.exists()){
        new File(directoryName).mkdirs();
      }
      File jsonDump = new File(fileName);
      jsonDump.createNewFile();

      FileWriter fileWriter = new FileWriter(fileName);
      PrintWriter printWriter = new PrintWriter(fileWriter);
      printWriter.println(output);
      printWriter.close();
    } catch (JsonProcessingException jpe) {
      log.error("Problems in generating JSON: {}", jpe);
    } catch (IOException ioe) {
      log.error("Problems in generating file: {}", ioe);
    }

  }

}
