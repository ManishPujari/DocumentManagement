package com.dt.europe.hal.api.common.utils;

import static java.util.stream.Collectors.toSet;

import com.dt.europe.hal.api.common.model.GeneratorParams;
import com.dt.europe.hal.api.common.model.Variable;
import com.google.common.base.CaseFormat;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;

@Slf4j
@SuppressWarnings("Duplicates")
public class Generator {

  private static String HAL_PACKAGE = "com.dt.europe.hal.api";
  private static String LIST_IMPORT_PACKAGE = "import java.util.List;";
  private static String ARRAY_IMPORT_PACKAGE = "import java.util.ArrayList;";
  private static Class mainClass;

  public static String fixWorkingFolder(String workingFolder) {
    if (File.separator.equalsIgnoreCase("/")) {
      return workingFolder;
    }

    if (workingFolder.charAt(0) == '/') {
      workingFolder = workingFolder.substring(1);
    }

    StringBuilder sb = new StringBuilder();
    for (char c : workingFolder.toCharArray()) {
      if (c == '/') {
        sb.append('\\');
      } else {
        sb.append(c);
      }
    }
    return sb.toString();
  }

  private static String getProjectFolder() {
    String workingFolder = mainClass.getProtectionDomain().getCodeSource().getLocation().getPath();
    workingFolder = fixWorkingFolder(workingFolder);
    if (workingFolder.contains(File.separator + "build" + File.separator)) {
      workingFolder =
        workingFolder.substring(
          0, workingFolder.indexOf(File.separator + "build" + File.separator));
    } else {
      workingFolder =
        workingFolder.substring(
          0, workingFolder.indexOf(File.separator + "out" + File.separator));
    }
    return workingFolder + File.separator + "src" + File.separator;
  }

  private static boolean isCoreJavaType(String typeName) {
    return typeName.equalsIgnoreCase("java.lang.String")
      || typeName.equalsIgnoreCase("java.time.OffsetDateTime")
      || typeName.equalsIgnoreCase("java.lang.Integer")
      || typeName.equalsIgnoreCase("java.lang.Boolean")
      || typeName.equalsIgnoreCase("java.lang.Double")
      || typeName.equalsIgnoreCase("java.lang.Object")
      || typeName.equalsIgnoreCase("java.lang.Float")
      || typeName.equalsIgnoreCase("java.lang.Long")
      || typeName.equalsIgnoreCase("java.time.LocalDate")
      || typeName.equalsIgnoreCase("java.math.BigDecimal");
  }

  private static boolean isCoreJava(String varName) {
    return varName.equalsIgnoreCase("String")
      || varName.equalsIgnoreCase("OffsetDateTime")
      || varName.equalsIgnoreCase("Integer")
      || varName.equalsIgnoreCase("Boolean")
      || varName.equalsIgnoreCase("Double")
      || varName.equalsIgnoreCase("Object")
      || varName.equalsIgnoreCase("Float")
      || varName.equalsIgnoreCase("Long")
      || varName.equalsIgnoreCase("LocalDate")
      || varName.equalsIgnoreCase("BigDecimal");
  }

  private static Class getArrayClass(Field f) {
    ParameterizedType stringListType = (ParameterizedType) f.getGenericType();
    return (Class<?>) stringListType.getActualTypeArguments()[0];
  }

  private static boolean isArray(Field f) {
    return f.getType().getName().equalsIgnoreCase("java.util.List");
  }

  private static Variable populateFromObject(Field f) {

    String className = f.getType().getSimpleName();
    String packageName = f.getType().getPackage().getName();
    return getVariable(f, className, packageName, false);
  }

  public static void clean(String projectName, String modelPackageName) throws IOException {
    mainClass = Generator.class;

    GeneratorParams generatorParams = getParamsFromPackage(projectName, modelPackageName);
    log.debug("{}", generatorParams);

    String serviceFolder =
      generatorParams
        .getServicePackageName()
        .replace(File.separator + "model", File.separator + "service");
    File[] files = new File(serviceFolder).listFiles();
    if (files != null && files.length > 0) {
      for (File file : files) {
        if (!file.isDirectory()) {
          file.delete();
        }
      }
    }

    String validatorFolder =
      generatorParams
        .getServicePackageName()
        .replace(File.separator + "model", File.separator + "validator");
    files = new File(validatorFolder).listFiles();
    if (files != null && files.length > 0) {
      for (File file : files) {
        if (!file.isDirectory()) {
          file.delete();
        }
      }
    }
  }

  private static String getEntityEnumName(String className) {
    switch (className) {
      case "SSOToken":
        return "SSO_TOKEN(\"ssoToken\"),";
      case "ServicePIN":
        return "SERVICE_PIN(\"servicePIN\"),";
      case "OTP":
        return "OTP(\"OTP\"),";
      case "ServicesOTP":
        return "SERVICES_OTP(\"servicesOTP\"),";
      case "Details2FA":
        return "DETAILS_2FA(\"details2FA\"),";
      case "ServicesUP":
        return "SERVICES_UP(\"servicesUP\"),";
      case "ServicesPIN":
        return "SERVICES_PIN(\"servicesPIN\"),";
      case "PIN":
        return "PIN(\"PIN\"),";
      default:
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, className)
          + "(\""
          + CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, className)
          + "\"),";
    }
  }

  public static void populateEnum(String projectName, String modelPackageName) throws IOException {
    mainClass = Generator.class;

    GeneratorParams generatorParams = getParamsFromPackage(projectName, modelPackageName);
    log.debug("{}", generatorParams);
    List<String> newEnums =
      generatorParams.getVariables().stream()
        .map(variable -> getEntityEnumName(variable.getName()))
        .collect(Collectors.toList());

    String relationFile =
      getProjectFolder()
        + "main.java.com.dt.europe.hal.api.common.model".replace(".", File.separator)
        + File.separator
        + "RelationEntityEnum.java";

        List<String> fileLines = Files.readAllLines(Paths.get(relationFile));
        boolean modifyOn = false;
        List<String> filteredLines = new ArrayList<>();
        for (String s : fileLines) {
          if (s.contains("modifyOff")) {
            modifyOn = false;
          }
          if (modifyOn) {
            filteredLines.add(s.trim());
          }
          if (s.contains("modifyOn")) {
            modifyOn = true;
          }
        }
        Stream<String> combinedStream = Stream.of(filteredLines, newEnums).flatMap(Collection::stream);
        List<String> collectionCombined =
          combinedStream.distinct().map(s -> "  " + s).collect(Collectors.toList());

        List<String> newLines = new ArrayList<>();
        boolean skip = false;
        for (String s : fileLines) {
          if (!skip) {
            newLines.add(s);
          }
          if (s.contains("modifyOn")) {
            skip = true;
            newLines.addAll(collectionCombined);
          }
          if (s.contains("modifyOff")) {
            skip = false;
            newLines.add(s);
          }
        }

        try (FileWriter fileWriter = new FileWriter(relationFile)) {
          PrintWriter printWriter = new PrintWriter(fileWriter);
          for (String s : newLines) {
            printWriter.println(s);
          }
          printWriter.close();
    }
  }

  private static Variable getVariable(
    Field f, String className, String packageName, boolean isList) {
    boolean isImported = !mainClass.getPackage().getName().equalsIgnoreCase(packageName);

    return Variable.builder()
      .name(f.getName())
      .upperName(CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, f.getName()))
      .type(className)
      .enumName(getModelEnumName(className))
      .serviceName(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, className) + "Service")
      .serviceClass(className + "Service")
      .serviceImportPackage(packageName.replace(".model.", ".service."))
      .validatorName(
        CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, className) + "ValidatorService")
      .validatorClass(className + "ValidatorService")
      .validatorImportPackage(packageName.replace(".model.", ".validator."))
      .isList(isList)
      .isImported(isImported)
      .build();
  }

  private static Variable populateFromArray(Field f) {
    Class arrayClass = getArrayClass(f);
    String className = arrayClass.getSimpleName();
    String packageName = arrayClass.getPackage().getName();

    return getVariable(f, className, packageName, true);
  }

  private static GeneratorParams getParamsFromClass(Class clazz) {
    mainClass = clazz;
    GeneratorParams generatorParams = GeneratorParams.builder().build();

    generatorParams.setClassName(clazz.getSimpleName());
    generatorParams.setModelPackageName(clazz.getPackage().getName());
    generatorParams.setServicePackageName(
      clazz.getPackage().getName().replace(".model", ".service"));
    generatorParams.setValidatorPackageName(
      clazz.getPackage().getName().replace(".model", ".validator"));

    List<Field> fields = ReflectionUtils.getAllFields(clazz);

    for (Field f : fields) {
      Class fieldClass = f.getType();

      if (fieldClass.isEnum()) {
        continue;
      }

      if (isCoreJavaType(fieldClass.getName())) {
        continue;
      }

      if (isCoreJava(fieldClass.getTypeName())) {
        continue;
      }

      Variable classVar;
      if (isArray(f)) {
        Class<?> stringListClass = getArrayClass(f);
        if (!stringListClass.getPackage().getName().startsWith(HAL_PACKAGE)) {
          continue;
        }
        if (stringListClass.isEnum()) {
          continue;
        }
        classVar = populateFromArray(f);
      } else {
        classVar = populateFromObject(f);
      }

      generatorParams.getVariables().add(classVar);
    }

    return generatorParams;
  }

  public static void generate(Class clazz, Configuration cfg)
    throws IOException, TemplateException {

    GeneratorParams generatorParams = getParamsFromClass(clazz);

    generateService(generatorParams, cfg);
    generateValidator(generatorParams, cfg);
  }

  public static void init(String projectName, String modelPackageName, Configuration cfg)
    throws IOException, TemplateException {
    mainClass = Generator.class;

    GeneratorParams generatorParams = getParamsFromPackage(projectName, modelPackageName);

    generateGenerator(generatorParams, cfg);
    generateTemplate(generatorParams, cfg);
  }

  public static void fixController(String filePath) throws IOException {

    if (!Files.exists(Paths.get(filePath))) {
      return;
    }

    reformatFile(filePath);
    fixBasic(filePath);

  }

  private static void reformatFile(String filePath) throws IOException {
    if (!Files.exists(Paths.get(filePath))) {
      return;
    }

    List<String> fileLines = Files.readAllLines(Paths.get(filePath));
    List<String> reformatedLines = new ArrayList<>();
    StringBuilder sb = new StringBuilder();
    boolean isBuffering = false;
    for (String s : fileLines) {

//      if(s.startsWith("public class")){
//        reformatedLines.add("@SuppressWarnings(\"Duplicates\")");
//      }

      if (s.contains("@ApiParam")) {
        if (!isBuffering) {
          isBuffering = true;
          sb = new StringBuilder();
          if (s.trim().startsWith("@ApiParam")) {
            sb.append(s);
          } else {
            reformatedLines.add(s.substring(0, s.indexOf("@ApiParam")));
            sb.append(s.substring(s.indexOf("@ApiParam")));
          }

          continue;
        } else {
          reformatedLines.add("    " + sb.toString().trim().replaceAll(" +", " "));
          sb = new StringBuilder();
          sb.append(s);
          if (s.contains("{")) {
            isBuffering = false;
            if (s.contains(") {")) {
              reformatedLines.add("    " + sb.toString().replace(") {", "").trim().replaceAll(" +", " ") + ",");
              reformatedLines.add(
                "    @ApiParam(hidden = true) @RequestHeader(name = HalMessages.POSTMAN_HEADER) String exampleId) throws ExampleException, InputParamInvalidException {");
            } else {
              reformatedLines.add("    " + sb.toString().trim().replaceAll(" +", " "));
            }
          }
          continue;
        }
      }
      if (isBuffering) {
        sb.append(" " + s.trim());
        if (s.contains("{")) {
          isBuffering = false;
          if (s.contains(") {")) {
            reformatedLines.add("    " + sb.toString().replace(") {", "").trim().replaceAll(" +", " ") + ",");
            reformatedLines.add(
              "    @ApiParam(hidden = true) @RequestHeader(name = HalMessages.POSTMAN_HEADER) String exampleId) throws ExampleException, InputParamInvalidException {");
          } else {
            reformatedLines.add("    " + sb.toString().trim().replaceAll(" +", " "));
          }

        }
        continue;
      }
      reformatedLines.add(s);
    }

    FileWriter fileWriter = new FileWriter(filePath);
    PrintWriter printWriter = new PrintWriter(fileWriter);
    for (String s : reformatedLines) {
      printWriter.println(s);
    }
    printWriter.close();
    fileWriter.close();

  }

  private static void fixBasic(String filePath) throws IOException {
    if (!Files.exists(Paths.get(filePath))) {
      return;
    }

    List<String> fileLines = Files.readAllLines(Paths.get(filePath));
    List<String> reformatedLines = new ArrayList<>();

    String packageName = filePath.substring(filePath.indexOf(File.separator + "api" + File.separator) + 5);
    packageName = packageName.substring(0, packageName.indexOf(File.separator));

    boolean hasInitBinder = false;
    boolean hasAutowiredLombok = false;
    boolean skipAutowired = false;
    boolean isReturnArr = false;
    boolean methodBodyStarts = false;
    boolean isReturnReached = false;
    HttpMethod method = null;

    Set<String> models = new HashSet<>();
    Set<MethodParam> methodParams = new HashSet<>();

    for (String s : fileLines) {
      if (s.contains("@RequiredArgsConstructor(onConstructor = @__(@Autowired))")) {
        hasAutowiredLombok = true;
      }

      if (s.startsWith("import org.springframework.web.bind.annotation.InitBinder;")) {
        hasInitBinder = true;
      }

      if (isRemovedInController(s) && !s.contains("{")) {
        continue;
      }

      if (s.contains("@ApiParam") && s.contains(" String fields,")) {
        continue;
      }

      if (!hasAutowiredLombok) {
        if (s.startsWith("public class ")) {
          reformatedLines.add("@RequiredArgsConstructor(onConstructor = @__(@Autowired))");
        }

        if (s.contains("@Autowired")) {
          skipAutowired = true;
          continue;
        }

        if (skipAutowired) {
          if (s.contains("}")) {
            skipAutowired = false;
          }
          continue;
        }
      }
      if (s.trim().startsWith("public ResponseEntity<")) {
        String trimmed = s.trim().substring("public ResponseEntity<".length());
        trimmed = trimmed.substring(0, trimmed.indexOf(">"));
        if (trimmed.contains("<")) {
          trimmed = trimmed.substring(trimmed.indexOf("<") + 1);
        }
        if (!trimmed.equalsIgnoreCase("Void")) {
          models.add(trimmed);
        }

      }
      reformatedLines.add(s);
    }
    String previousLine = "";
    List<String> newLines = new ArrayList<>();

    List<String> newImports = new ArrayList<>();

    newImports.add("import com.dt.europe.hal.api.common.templates.HalMessages;");
    newImports.add("import com.dt.europe.hal.api.common.model.tmf.Characteristic;");
    newImports.add("import com.dt.europe.hal.api.common.model.tmf.Error;");
    newImports.add("import com.dt.europe.hal.api.common.model.ResponseParams;");
    newImports.add("import com.dt.europe.hal.api.common.model.RequestParams;");
    newImports.add("import com.dt.europe.hal.api.common.model.ValidatorContexEnum;");
    newImports.add("import com.dt.europe.hal.api.common.exceptions.ExampleException;");
    newImports.add("import com.dt.europe.hal.api.common.exceptions.InputParamInvalidException;");
    newImports.add("import com.dt.europe.hal.api.common.repository.ResponseParamsRepository;");
    newImports.add("import com.dt.europe.hal.api.common.service.ErrorService;");
    newImports.add("import com.dt.europe.hal.api.common.service.HeaderService;");
    newImports.add("import com.dt.europe.hal.api.common.validator.ExampleValidatorService;");

    if (!hasAutowiredLombok) {
      newImports.add("import lombok.RequiredArgsConstructor;");
    }
    packageName = "import com.dt.europe.hal.api." + packageName + ".";
    newImports.add(packageName + "configuration.SwaggerConfig;");
    newImports.add(packageName + "model.enums.*;");
    if (hasInitBinder) {
      newImports.add(packageName + "model.enums.converters.*;");
    }
    for (String s : models) {
      newImports.add(packageName + "model." + s + ";");
      newImports.add(packageName + "service." + s + "Service;");
      newImports.add(packageName + "validator." + s + "ValidatorService;");
    }

    String modelName = "";

    for (String s : reformatedLines) {
      if (previousLine.startsWith("import ") && !s.startsWith("import ")) {
        newLines.addAll(newImports);
      }

      if (s.trim().isEmpty() && previousLine.trim().isEmpty()) {
        continue;
      }

      if (s.startsWith("public class ")) {
        newLines.add(s);
        newLines.add("  ");
        newLines.add("  private final ResponseParamsRepository responseParamsRepository;");
        newLines.add("  private final ErrorService errorService;");
        newLines.add("  private final HeaderService headerService;");
        newLines.add("  private final ExampleValidatorService exampleValidatorService;");

        for (String m : models) {
          newLines.add("  private final " + m + "Service " + CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, m) + "Service;");
          newLines.add("  private final " + m + "ValidatorService " + CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, m) + "ValidatorService;");
        }
        newLines.add("  ");
        previousLine = s;
        continue;
      }

      if (s.contains("public ResponseEntity")) {
//        get response type
        if (s.contains("public ResponseEntity<List<")) {
          isReturnArr = true;
        } else {
          isReturnArr = false;
        }
// get model name
        if (s.trim().startsWith("public ResponseEntity<")) {
          modelName = s.trim().substring("public ResponseEntity<".length());
          modelName = modelName.substring(0, modelName.indexOf(">"));
          if (modelName.contains("<")) {
            modelName = modelName.substring(modelName.indexOf("<") + 1);
          }
        }
// get method
        if (previousLine.contains("@GetMapping")) {
          method = HttpMethod.GET;
        }
        if (previousLine.contains("@PostMapping")) {
          method = HttpMethod.POST;
        }
        if (previousLine.contains("@PutMapping")) {
          method = HttpMethod.PUT;
        }
        if (previousLine.contains("@PatchMapping")) {
          method = HttpMethod.PATCH;
        }
        if (previousLine.contains("@DeleteMapping")) {
          method = HttpMethod.DELETE;
        }
      }
//on last line add standard params
      if (!methodBodyStarts && s.contains("HalMessages.POSTMAN_HEADER")) {
        if (method == HttpMethod.GET && isReturnArr) {
          newLines.add("    @ApiParam(value = HalMessages.PARAM_FIELDS) @Valid @RequestParam(value = \"fields\", required = false) String fields,");
          newLines.add("    @ApiParam(value = HalMessages.PARAM_QUERY) @Valid @RequestParam(value = \"query\", required = false) String query,");
          newLines.add("    @ApiParam(value = HalMessages.PARAM_PAGE) @Valid @RequestParam(value = \"page\", required = false) String page,");
          newLines.add("    @ApiParam(value = HalMessages.PARAM_SIZE) @Valid @RequestParam(value = \"size\", required = false) String size,");
          newLines.add("    @ApiParam(value = HalMessages.PARAM_SORT) @Valid @RequestParam(value = \"sort\", required = false) String sort,");
        }
        if (method == HttpMethod.GET && !isReturnArr) {
          newLines.add("    @ApiParam(value = HalMessages.PARAM_FIELDS) @Valid @RequestParam(value = \"fields\", required = false) String fields,");
        }
        if (method == HttpMethod.PATCH) {
          newLines.add(
            "    @ApiParam(value = HalMessages.PARAM_PATCH_FIELDS, required = true) @Valid @RequestParam(value = \"fields\", required = true) String fields,");
        }
        newLines.add(
          "    @ApiParam(hidden = true) @RequestHeader(name = HalMessages.POSTMAN_HEADER) String exampleId) throws ExampleException, InputParamInvalidException {");
        methodBodyStarts = true;
        previousLine = s;
        continue;
      }

      if (!modelName.isEmpty() && methodBodyStarts) {
        if (s.contains("}") && isReturnReached) {

          newLines.addAll(returnValidatorArr(method, isReturnArr, methodParams, modelName));

          newLines.add("    ");
          newLines.add("    this.errorService.hasException(exampleId);");
          newLines.add("    ResponseParams responseParams = responseParamsRepository.findByExampleId(exampleId);");
          if (method != HttpMethod.DELETE) {
            if (isReturnArr) {
              newLines.add("    List<" + modelName + "> response = this." + CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, modelName)
                + "Service.getResponseList(responseParams.getBodyRefs());");
              newLines.add(
                "    " + CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, modelName)
                  + "ValidatorService.validateAll(response, ValidatorContexEnum.RESPONSE);");
            } else {
              newLines.add("    " + modelName + " response = this." + CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, modelName)
                + "Service.getResponse(responseParams.getBodyRefs());");
              newLines.add(
                "    " + CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, modelName) + "ValidatorService.validate(response, ValidatorContexEnum.RESPONSE);");
            }
          }

          newLines.add("    HttpHeaders httpHeaders = headerService.getResponseHeaders(exampleId);");
          if (method == HttpMethod.POST) {
            newLines.add("    return new ResponseEntity<>(response, httpHeaders, HttpStatus.CREATED);");
          } else if (method == HttpMethod.DELETE) {
            newLines.add("    return new ResponseEntity<>(null, httpHeaders, HttpStatus.NO_CONTENT);");
          } else {
            newLines.add("    return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);");
          }
          newLines.add("  }");
          newLines.add("  ");

          methodBodyStarts = false;
          isReturnReached = false;
          methodParams.clear();
          method = null;
          modelName = "";
        }

        if (s.contains("return ")) {
          isReturnReached = true;
        }
        previousLine = s;
        continue;
      }

      if (method != null && s.trim().startsWith("@ApiParam") && (!s.contains("HalMessages.POSTMAN_HEADER") && !s.contains("HalMessages.GET_HEADER"))) {
        methodParams.add(getMethodParamFromString(s));
      }

      newLines.add(s);
      previousLine = s;
    }

    FileWriter fileWriter = new FileWriter(filePath);
    PrintWriter printWriter = new PrintWriter(fileWriter);
    for (String s : newLines) {
      printWriter.println(s);
    }
    printWriter.close();
    fileWriter.close();

  }

  private static List<String> returnValidatorArr(HttpMethod method, boolean isArray, Set<MethodParam> methodParams, String modelName) {
    List<String> validatorList = new ArrayList<>();
    if (methodParams.isEmpty()) {
      return validatorList;
    }
    final String DELIMITER = "     ";
    if (methodParams.stream().noneMatch(mp -> !mp.isBody())) {
      if (method == HttpMethod.POST) {
        MethodParam methodParam = methodParams.stream().filter(MethodParam::isBody).findFirst().get();
        validatorList.add("    ");
        validatorList.add("    " + CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, modelName) + "ValidatorService.validate(" + methodParam.getName()
          + ", ValidatorContexEnum.POST_REQUEST);");
      }

      return validatorList;
    }

    if (method == HttpMethod.GET) {
      methodParams.add(MethodParam.builder().apiName("fields").type("String").name("fields").build());
      if (isArray) {
        methodParams.add(MethodParam.builder().apiName("query").type("String").name("query").build());
        methodParams.add(MethodParam.builder().apiName("page").type("String").name("page").build());
        methodParams.add(MethodParam.builder().apiName("size").type("String").name("size").build());
        methodParams.add(MethodParam.builder().apiName("sort").type("String").name("sort").build());
      }
    }
    if (method == HttpMethod.PATCH) {
      methodParams.add(MethodParam.builder().apiName("fields").type("String").name("fields").build());
    }

    validatorList.add("    ");
    validatorList.add("    {");
    validatorList.add(DELIMITER + "RequestParams parsedParams = new RequestParams();");
    for (MethodParam m : methodParams) {
      if (m.isBody()) {
        continue;
      }
      validatorList.add(DELIMITER);
      if (m.isEnum()) {
        validatorList.add(DELIMITER + "if (" + m.getName() + " != null) {");
      } else {
        validatorList.add(DELIMITER + "if (" + m.getName() + " != null && !" + m.getName() + ".isEmpty()) {");
      }

      if (m.isPathParam()) {
        validatorList.add(DELIMITER + " parsedParams.getPathParams()");
      } else {
        validatorList.add(DELIMITER + " parsedParams.getQueryParams()");
      }
      if (m.isEnum()) {
        validatorList.add(DELIMITER + "   .add(Characteristic.builder().name(\"" + m.getApiName() + "\").value(" + m.getName() + ".toString()).build());");
      } else {
        validatorList.add(DELIMITER + "   .add(Characteristic.builder().name(\"" + m.getApiName() + "\").value(" + m.getName() + ").build());");
      }

      validatorList.add(DELIMITER + "  }");

    }

    validatorList.add(DELIMITER);
    validatorList.add(DELIMITER + " this.exampleValidatorService.validateGetParams(exampleId, parsedParams);");
    validatorList.add(DELIMITER + "}");

    return validatorList;
  }

  private static MethodParam getMethodParamFromString(String line) {

    MethodParam methodParam = MethodParam.builder().isBody(false).isPathParam(false).isEnum(false).build();
    String regex = "^([\\s]*@ApiParam)(.*)(@RequestParam\\(value = \\\")([a-zA-Z.0-9]+)(\\\",)(.*)(\\s([a-zA-Z.0-9]+|Collection<[a-zA-Z.0-9]+>)\\s([a-zA-Z.0-9]+)(,|\\)\\s\\{))";
    Matcher m;
    if (line.contains("@RequestBody")) {
      methodParam.setBody(true);
      regex = "^([\\s]*@ApiParam)(.*)(\\s([a-zA-Z.0-9]+)\\s([a-zA-Z.0-9]+)(,|\\)\\s\\{))";
      m = Pattern.compile(regex).matcher(line);
      log.info("Matching {}", line);
      m.find();

      methodParam.setType(m.group(4));
      methodParam.setName(m.group(5));

      return methodParam;
    }
    if (line.contains("@PathVariable")) {
      methodParam.setPathParam(true);
      regex = "^([\\s]*@ApiParam)(.*)(@PathVariable\\(\\\")([a-zA-Z.0-9]+)(\\\")(.*)(\\s([a-zA-Z.0-9]+)\\s([a-zA-Z.0-9]+)(,|\\)\\s\\{))";
      m = Pattern.compile(regex).matcher(line);
      log.info("Matching {}", line);
      m.find();

      methodParam.setApiName(m.group(4));
      methodParam.setType(m.group(8));
      methodParam.setName(m.group(9));
      if (methodParam.getType().endsWith("Enum")) {
        methodParam.setEnum(true);
      }

      return methodParam;
    }
    m = Pattern.compile(regex).matcher(line);
    log.info("Matching {}", line);
    m.find();

    methodParam.setApiName(m.group(4));
    methodParam.setType(m.group(8).replace("Collection<", "").replace(">", ""));
    methodParam.setName(m.group(9));
    if (methodParam.getType().endsWith("Enum")) {
      methodParam.setEnum(true);
    }

    return methodParam;
  }

  private static void cleanJavaFile(String filePath) throws IOException {
    if (!Files.exists(Paths.get(filePath))) {
      return;
    }
    boolean isMain = false;
    if (filePath.endsWith("Application.java")) {
      isMain = true;
    }

    boolean hasArray = false;
    boolean hasJsonProperty = false;
    boolean hasApiMProperty = false;
    boolean hasOffsetDateTimeImport = false;
    boolean hasOffsetDateTime = false;
    boolean hasBaseTemplate = false;
    boolean hasNonTmf = false;

    List<String> fileLines = Files.readAllLines(Paths.get(filePath));

    Set<String> enumImports = new HashSet<>();
    for (String s : fileLines) {
      if (s.startsWith("import io.swagger.annotations.ApiModelProperty")) {
        hasApiMProperty = true;
      }
      if (s.startsWith("import com.fasterxml.jackson.annotation.JsonProperty")) {
        hasJsonProperty = true;
      }
      if (s.startsWith("import java.time.OffsetDateTime;")) {
        hasOffsetDateTimeImport = true;
      }
      if (s.trim().startsWith("private OffsetDateTime")) {
        hasOffsetDateTime = true;
      }
      if (s.startsWith("import com.dt.europe.hal.api.common.model.BaseTemplate;")) {
        hasBaseTemplate = true;
      }
      if (s.contains(".model.enums.")) {
        enumImports.add(s);
      }
      if (s.contains("@NonTMF")) {
        hasNonTmf = true;
      }
    }

    List<String> newImports = new ArrayList<>();
    if (!isMain) {
      if (!hasBaseTemplate) {
        newImports.add("import com.dt.europe.hal.api.common.model.BaseTemplate;");
      }
      newImports.add("import lombok.AllArgsConstructor;");
      newImports.add("import lombok.Builder;");
      newImports.add("import lombok.Data;");
      newImports.add("import lombok.EqualsAndHashCode;");
      newImports.add("import lombok.NoArgsConstructor;");
      if (!hasApiMProperty) {
        newImports.add("import io.swagger.annotations.ApiModelProperty;");
      }
      if (!hasJsonProperty) {
        newImports.add("import com.fasterxml.jackson.annotation.JsonProperty;");
      }
      if (hasOffsetDateTime && !hasOffsetDateTimeImport) {
        newImports.add("import java.time.OffsetDateTime;");
      }
      if (hasNonTmf) {
        newImports.add("import com.dt.europe.hal.api.common.annotations.NonTMF;");
      }
    }

    List<String> filteredLines = new ArrayList<>();
    Set<String> enumNames = new HashSet<>();
    for (String s : fileLines) {

      if (s.trim().startsWith("private") && s.contains("Enum ")) {
        String enumName = s.trim().substring("private ".length());
        enumName = enumName.substring(0, enumName.indexOf(" "));
        enumNames.add(enumName);
      }

      if (s.trim().startsWith("public class ") && !isMain) {
        String javaName = filePath.substring(filePath.lastIndexOf(File.separator) + 1, filePath.lastIndexOf(".java"));

        filteredLines.add("@Data");
        filteredLines.add("@EqualsAndHashCode(callSuper = false)");
        filteredLines.add("@AllArgsConstructor");
        filteredLines.add("@NoArgsConstructor");
        filteredLines.add("@Builder");
        filteredLines.add("public class " + javaName + " extends BaseTemplate {");
        continue;
      }

      if (s.trim().startsWith("private List<")) {
        hasArray = true;
        filteredLines.add("  @Builder.Default");

        if (!s.contains(" = ")) {
          String newList = s.substring(0, s.length() - 1) + " = new ArrayList<>();";
          filteredLines.add(newList);

        } else {
          String newList = s.substring(0, s.indexOf(" = ")) + " = new ArrayList<>();";
          filteredLines.add(newList);
        }
        continue;
      }

      if (s.trim().startsWith("private Set<")) {
        hasArray = true;
        filteredLines.add("  @Builder.Default");

        if (!s.contains(" = ")) {
          String newList = s.substring(0, s.length() - 1) + " = new ArrayList<>();";
          filteredLines.add(newList);

        } else {
          filteredLines.add(s.replace(" Set<", " List<").replace(" = new HashSet<>();", " = new ArrayList<>();"));
        }
        continue;
      }

      if (!isRemovedInModel(s)) {
        if (s.contains(" = null;")) {
          filteredLines.add(s.replace(" = null;", ";"));
        } else {
          filteredLines.add(s);
        }
      }
    }

//    fix imports
    if (hasArray) {
      newImports.add("import java.util.List;");
      newImports.add("import java.util.ArrayList;");
    }

    if (!enumNames.isEmpty()) {
      String packageName = filePath.substring(filePath.indexOf("com.dt.europe.hal.api.".replace(".", File.separator)) + "com.dt.europe.hal.api.".length());
      packageName = packageName.substring(0, packageName.indexOf(File.separator));
      String enumPackage = "import com.dt.europe.hal.api." + packageName + ".model.enums.";
      enumNames = enumNames.stream().map(en -> enumPackage + en + ";").collect(toSet());
      enumNames.removeAll(enumImports);
      newImports.addAll(enumNames);
    }

    List<String> finalLines = new ArrayList<>();
    boolean hasValidate = false;
    String previousLine = "";
    for (String s : filteredLines) {
      if (previousLine.startsWith("import ") && !s.startsWith("import ")) {
        finalLines.addAll(newImports);
      }
      if (s.contains("public void validateInit() {")) {
        hasValidate = true;
      }
      if (s.equalsIgnoreCase("}") && !isMain && !hasValidate) {
        finalLines.add("  ");
        finalLines.add("  @Override");
        finalLines.add("  public void validateInit() {");
        finalLines.add("  ");
        finalLines.add("  }");
      }
      finalLines.add(s);
      previousLine = s;
    }

    FileWriter fileWriter = new FileWriter(filePath);
    PrintWriter printWriter = new PrintWriter(fileWriter);
    for (String s : finalLines) {
      printWriter.println(s);
    }
    printWriter.close();
    fileWriter.close();

  }

  private static boolean isRemovedInModel(String s) {
    if (s == null || s.isEmpty()) {
      return false;
    }

    if (s.contains("org.springframework.boot.autoconfigure.domain.EntityScan")) {
      return true;
    }

    if (s.contains("org.springframework.data.jpa")) {
      return true;
    }

    if (s.contains("@EntityScan(")) {
      return true;
    }

    if (s.contains("@EnableJpaRepositories")) {
      return true;
    }

    if (s.contains("javax.persistence.")) {
      return true;
    }

    if (s.contains("@Entity")) {
      return true;
    }

    if (s.contains("@EqualsAndHashCode(")) {
      return true;
    }

    if (s.contains("@Transient")) {
      return true;
    }

    if (s.contains("@NotNull")) {
      return true;
    }

    if (s.contains("@MappedSuperclass")) {
      return true;
    }

    if (s.contains("@Enumerated")) {
      return true;
    }

    if (s.contains("@ElementCollection")) {
      return true;
    }

    if (s.contains("@CollectionTable")) {
      return true;
    }

    if (s.contains("@Lob")) {
      return true;
    }

    if (s.contains("@Column")) {
      return true;
    }

    if (s.contains("@Table")) {
      return true;
    }

    if (s.contains("import lombok.")) {
      return true;
    }

    if (s.contains("@Getter")) {
      return true;
    }

    if (s.contains("@Setter")) {
      return true;
    }

    if (s.contains("@AllArgsConstructor")) {
      return true;
    }

    if (s.contains("@Data")) {
      return true;
    }

    if (s.contains("@NoArgsConstructor")) {
      return true;
    }

    if (s.contains("@Builder")) {
      return true;
    }

    if (s.contains("@Builder.Default")) {
      return true;
    }

    if (s.contains("import com.dt.europe.hal.api.shared")) {
      return true;
    }

    if (s.contains("import javax.validation.constraints.NotNull")) {
      return true;
    }

    if (s.contains("import java.util.Set;")) {
      return true;
    }

    if (s.contains("import java.util.HashSet;")) {
      return true;
    }

    if (s.contains("import java.util.List;")) {
      return true;
    }

    if (s.contains("import java.util.ArrayList;")) {
      return true;
    }

    return false;
  }

  private static boolean isRemovedInController(String s) {
    if (s == null || s.isEmpty()) {
      return false;
    }

    if (s.contains("import com.dt.europe.hal.api")) {
      return true;
    }

    if (s.contains("private final ")) {
      return true;
    }

    if (s.contains("import javax.servlet.http.HttpServletRequest;")) {
      return true;
    }

    if (s.contains("@ApiParam(value = HalMessages.PARAM_FIELDS)")) {
      return true;
    }

    if (s.contains("@ApiParam(value = HalMessages.PARAM_QUERY)")) {
      return true;
    }

    if (s.contains("@ApiParam(value = HalMessages.PARAM_PAGE)")) {
      return true;
    }

    if (s.contains("@ApiParam(value = HalMessages.PARAM_SIZE)")) {
      return true;
    }

    if (s.contains("@ApiParam(value = HalMessages.PARAM_SORT)")) {
      return true;
    }

    return false;
  }

  private static void deleteFolder(String folderName) throws IOException {
    if (!Files.exists(Paths.get(folderName))) {
      return;
    }
    Files.walk(Paths.get(folderName))
      .sorted(Comparator.reverseOrder())
      .map(Path::toFile)
      .forEach(File::delete);
  }

  public static void upgrade(String projectName, String packageName)
    throws IOException {
    mainClass = Generator.class;

    GeneratorParams generatorParams = getParamsFromPackage(projectName, packageName);

    Files.deleteIfExists(Paths.get(generatorParams.getServicePackageName() + File.separator + "configuration" + File.separator + "RepositoryConfig.java"));
    Files.deleteIfExists(Paths.get(
      generatorParams.getServicePackageName().replace(File.separator + "main" + File.separator, File.separator + "test" + File.separator) + File.separator
        + generatorParams.getVariables().get(0).getName() + "Test.java"));
    Files.deleteIfExists(Paths.get(
      generatorParams.getServicePackageName().replace(File.separator + "main" + File.separator, File.separator + "test" + File.separator) + File.separator
        + generatorParams.getVariables().get(0).getName() + "Tests.java"));

    deleteFolder(generatorParams.getServicePackageName() + File.separator + "init");
    deleteFolder(generatorParams.getServicePackageName() + File.separator + "repository");
    deleteFolder(generatorParams.getServicePackageName() + File.separator + "exceptions");
    deleteFolder(generatorParams.getServicePackageName() + File.separator + "service");
    deleteFolder(generatorParams.getServicePackageName().substring(0, generatorParams.getServicePackageName().indexOf(File.separator + "src" + File.separator))
      + File.separator + "db");

    String initPath =
      generatorParams.getServicePackageName().substring(0, generatorParams.getServicePackageName().indexOf(File.separator + "src" + File.separator))
        + ".src.main.resources.init".replace(".", File.separator);
    if (Files.exists(Paths.get(initPath))) {
      deleteFolder(initPath);
      new File(initPath).mkdirs();
    } else {
      new File(initPath).mkdirs();
    }
    String dbPath =
      generatorParams.getServicePackageName().substring(0, generatorParams.getServicePackageName().indexOf(File.separator + "src" + File.separator))
        + ".src.main.resources.".replace(".", File.separator) + "application-db.yaml";
    Files.deleteIfExists(Paths.get(dbPath));

    String testResPath =
      generatorParams.getServicePackageName().substring(0, generatorParams.getServicePackageName().indexOf(File.separator + "src" + File.separator))
        + ".src.test.resources".replace(".", File.separator);
    deleteFolder(testResPath);

    String mainAppPath = generatorParams.getServicePackageName() + File.separator + generatorParams.getVariables().get(0).getName() + ".java";
    cleanJavaFile(mainAppPath);

    String modelPath = generatorParams.getServicePackageName() + File.separator + "model";
    Set<String> fileNames = listJavaFilesUsingFileWalk(modelPath, 1);
    for (String fName : fileNames) {
      cleanJavaFile(generatorParams.getServicePackageName() + File.separator + "model" + File.separator + fName + ".java");
    }
  }

  public static void refresh(String projectName, String packageName)
    throws IOException {
    mainClass = Generator.class;

    GeneratorParams generatorParams = getParamsFromPackage(projectName, packageName);

    String modelPath = generatorParams.getServicePackageName() + File.separator + "model";
    Set<String> fileNames = listJavaFilesUsingFileWalk(modelPath, 1);
//    for (String fName : fileNames) {
//      cleanJavaFile(generatorParams.getServicePackageName() + File.separator + "model" + File.separator + fName + ".java");
//    }
  }

  private static void populatePreparedClasses(Set<String> outerClassImports, String path, String basePath, Map<String, String> baseClasses,
    Map<String, List<String>> classBodies, Map<String, List<String>> classImports, Set<String> enumNames, Set<String> enumImports) throws IOException {
    while (!outerClassImports.isEmpty()) {
      for (String fName : outerClassImports) {
        String modelPackage = fName.substring("com.dt.europe.hal.api.shared.model".length()).replace(".", File.separator);
        String modelPath = path.substring(0, path.lastIndexOf(File.separator)) + modelPackage + ".java";

        prepareForMoving(baseClasses, classBodies, classImports, modelPath);
      }

      while (!baseClasses.isEmpty()) {
        Set<String> keys = baseClasses.keySet();
        Set<String> values = new HashSet<>();
        for (Entry<String, String> entry : baseClasses.entrySet()) {
          if (!entry.getValue().isEmpty()) {
            values.add(entry.getValue());
          }
        }
        values.removeAll(keys);
        if (!values.isEmpty()) {
          for (String v : values) {
            String fileName = basePath + File.separator + v.replace(".", File.separator) + ".java";
            prepareForMoving(baseClasses, classBodies, classImports, fileName);
          }
        } else {
          break;
        }
      }

      enumImports = new HashSet<>();

      outerClassImports.clear();
      for (Entry<String, List<String>> entry : classImports.entrySet()) {
        if (!entry.getValue().isEmpty()) {
          enumImports.addAll(entry.getValue().stream().filter(item -> item.contains(".enums."))
            .map(i -> i.replace("import ", "").replace(";", ""))
            .collect(toSet()));
          outerClassImports.addAll(entry.getValue().stream().filter(item -> !item.contains(".enums."))
            .map(i -> i.replace("import ", "").replace(";", ""))
            .collect(toSet()));
        }
      }
      enumNames = new HashSet<>();
      for (String ei : enumImports) {
        String name = ei.substring(ei.lastIndexOf(".") + 1);
        if (enumNames.contains(name)) {
          log.error("Duplicate Enum name: {}", name);
        }
        enumNames.add(name);
      }

      if (enumNames.size() != enumImports.size()) {
        log.error("Duplicate Enum names!!!");
        log.debug("Enum imports:\n{}", enumImports);
        log.debug("Enum names:\n{}", enumNames);
      }

      String originPackage = path.replace(basePath, "").substring(1).replace(File.separator, ".");
      Set<String> classesToMove = baseClasses.keySet().stream().filter(s -> s.startsWith(originPackage)).collect(toSet());
      Set<String> extendingClasses = baseClasses.keySet().stream().filter(s -> !s.startsWith(originPackage)).collect(toSet());
      outerClassImports.removeAll(classesToMove);
      outerClassImports.removeAll(extendingClasses);

    }
  }

  private static boolean isFoundAsReference(String className, Map<String, String> baseClasses) {
    for (Entry<String, String> entry : baseClasses.entrySet()) {
      if (!entry.getValue().isEmpty()) {
        if (entry.getValue().equalsIgnoreCase(className)) {
          return true;
        }
      }
    }
    return false;
  }

  public static void moveModel(String projectName, String packageName, String sourceModelPackage, Set<String> additionalImports)
    throws IOException {
    mainClass = Generator.class;

    String path = getProjectFolder();
    String basePath = path.substring(0, path.indexOf(File.separator + "common" + File.separator)) + File.separator + "shared.src.main.java".replace(".",
      File.separator);
    path =
      path.substring(0, path.indexOf(File.separator + "common" + File.separator)) + ".shared.src.main.java.".replace(".",
        File.separator) + sourceModelPackage.replace(".", File.separator);

    Map<String, String> baseClasses = new HashMap<>();
    Map<String, List<String>> classBodies = new HashMap<>();
    Map<String, List<String>> classImports = new HashMap<>();
    Set<String> outerClassImports;
    Set<String> enumNames = new HashSet<>();
    Set<String> enumImports = new HashSet<>();

    Set<String> fileNames = listJavaFilesUsingFileWalk(path, 1);
    outerClassImports = fileNames.stream().map(s -> sourceModelPackage + "." + s).collect(toSet());
    outerClassImports.addAll(additionalImports);

    populatePreparedClasses(outerClassImports, path, basePath, baseClasses, classBodies, classImports, enumNames, enumImports);

//    inner imports
    Set<String> bodyVars = new HashSet<>();
    do {
      bodyVars = new HashSet<>();
      Set<String> changedBodyVars = new HashSet<>();
      Set<String> keySet = baseClasses.keySet().stream().filter(s -> !isFoundAsReference(s, baseClasses)).collect(Collectors.toSet());
      for (String ici : keySet) {
        String currentPointer = ici;
        while (!currentPointer.isEmpty()) {
          List<String> extBody = classBodies.get(currentPointer).stream().filter(s -> s.trim().startsWith("private "))
            .map(line -> {
              String s = line.trim().substring("private ".length());
              s = s.substring(0, s.indexOf(" ")).replace(">", "").replace("List<", "").replace("Set<", "");
              if (s.endsWith("Enum")) {
                return "";
              }
              if (isCoreJava(s)) {
                return "";
              }
              return s;
            }).filter(eb -> !eb.isEmpty())
            .distinct().collect(Collectors.toList());

          List<String> extImports = classImports.get(currentPointer).stream().filter(s -> !s.contains(".enums."))
            .map(s -> s.substring(s.lastIndexOf(".") + 1, s.length() - 1))
            .collect(Collectors.toList());
          if (!extBody.isEmpty()) {
            if (!extImports.isEmpty()) {
              extBody.removeAll(extImports);
            }
          }

          if (!extBody.isEmpty()) {
            for (String s : extBody) {
              s = currentPointer.substring(0, currentPointer.lastIndexOf(".") + 1) + s;
              if (!baseClasses.containsKey(s)) {
                bodyVars.add(s);
                changedBodyVars.add(s);
              }
            }
          }

          currentPointer = baseClasses.get(currentPointer);
        }
      }
      if (!changedBodyVars.isEmpty()) {
        populatePreparedClasses(changedBodyVars, path, basePath, baseClasses, classBodies, classImports, enumNames, enumImports);
      }
    } while (!bodyVars.isEmpty());

    Set<String> fileNamesDistinct = baseClasses.keySet().stream().map(ks -> ks.substring(ks.lastIndexOf(".") + 1)).collect(toSet());
    if (fileNamesDistinct.size() != baseClasses.keySet().size()) {
      log.error("Duplicate Model names!!!");
      log.debug("Model names:\n{}", fileNamesDistinct);
      log.debug("Full model names:\n{}", baseClasses.keySet());
    }

    outerClassImports.addAll(baseClasses.keySet());
    for (Entry<String, String> entry : baseClasses.entrySet()) {
      if (!entry.getValue().isEmpty()) {
        outerClassImports.remove(entry.getValue());
      }
    }
    moveModelsToFolder(basePath + File.separator,
      basePath.replace(File.separator + "shared" + File.separator, File.separator + projectName + File.separator) + File.separator + packageName.replace(".",
        File.separator) + File.separator + "model" + File.separator, packageName + ".model", outerClassImports, baseClasses, classBodies);

    moveEnumsToFolder(basePath + File.separator,
      basePath.replace(File.separator + "shared" + File.separator, File.separator + projectName + File.separator) + File.separator + packageName.replace(".",
        File.separator) + File.separator + "model" + File.separator + "enums" + File.separator, packageName + ".model.enums", classImports);
  }

  private static void moveEnumsToFolder(String sourceFolder, String destinationFolder, String destinationPackage, Map<String, List<String>> imports)
    throws IOException {
    if (!Files.exists(Paths.get(destinationFolder))) {
      new File(destinationFolder).mkdirs();
    }

    Set<String> enums = new HashSet<>();
    Map<String, String> migrationMap = new HashMap<>();

    for (Entry<String, List<String>> entry : imports.entrySet()) {
      if (!entry.getValue().isEmpty()) {
        for (String s : entry.getValue()) {
          if (s.contains(".enums.")) {
            enums.add(s.trim().substring("import ".length(), s.trim().length() - 1));
          }
        }
      }
    }

    enums.forEach(
      ks -> migrationMap.put(sourceFolder + ks.replace(".", File.separator) + ".java", destinationFolder + ks.substring(ks.lastIndexOf(".") + 1) + ".java"));

    for (Entry<String, String> entry : migrationMap.entrySet()) {
      List<String> fileLines = Files.readAllLines(Paths.get(entry.getKey()));
      List<String> filteredLines = new ArrayList<>();

      for (String s : fileLines) {

        if (s.startsWith("package ")) {
          filteredLines.add("package " + destinationPackage + ";");
          continue;
        }

        filteredLines.add(s);
      }

      FileWriter fileWriter = new FileWriter(entry.getValue());
      PrintWriter printWriter = new PrintWriter(fileWriter);
      for (String s : filteredLines) {
        printWriter.println(s);
      }
      printWriter.close();
      fileWriter.close();
    }

  }

  private static void moveModelsToFolder(String sourceFolder, String destinationFolder, String destinationPackage, Set<String> importClasses,
    Map<String, String> baseClasses,
    Map<String, List<String>> classBodies) throws IOException {
    if (!Files.exists(Paths.get(destinationFolder))) {
      new File(destinationFolder).mkdirs();
    }

    Map<String, String> migrationMap = new HashMap<>();

    importClasses.forEach(
      ks -> migrationMap.put(sourceFolder + ks.replace(".", File.separator) + ".java", destinationFolder + ks.substring(ks.lastIndexOf(".") + 1) + ".java"));

    for (Entry<String, String> entry : migrationMap.entrySet()) {
      List<String> fileLines = Files.readAllLines(Paths.get(entry.getKey()));
      List<String> filteredLines = new ArrayList<>();

      for (String s : fileLines) {

        if (s.startsWith("package ")) {
          filteredLines.add("package " + destinationPackage + ";");
          continue;
        }

        if (s.startsWith("public class ")) {

          if (s.contains("}")) {
            s.replace("}", "");
          }
          filteredLines.add(s);
          filteredLines.add(" ");
          filteredLines.addAll(getExtendedBodies(entry.getKey().replace(sourceFolder, "").replace(File.separator, ".").replace(".java", ""), baseClasses,
            classBodies));
          break;
        }

        filteredLines.add(s);
      }

      filteredLines.add("}");
      filteredLines.add("");

      FileWriter fileWriter = new FileWriter(entry.getValue());
      PrintWriter printWriter = new PrintWriter(fileWriter);
      for (String s : filteredLines) {
        printWriter.println(s);
      }
      printWriter.close();
      fileWriter.close();
    }


  }

  private static List<String> getExtendedBodies(String key, Map<String, String> baseClasses, Map<String, List<String>> classBodies) {

    if (!baseClasses.get(key).isEmpty()) {
      if (!classBodies.get(key).isEmpty()) {
        List<String> fullBody = new ArrayList<>(getExtendedBodies(baseClasses.get(key), baseClasses, classBodies));
        fullBody.addAll(classBodies.get(key));
        return fullBody;
      } else {
        return getExtendedBodies(baseClasses.get(key), baseClasses, classBodies);
      }
    } else {
      if (!classBodies.get(key).isEmpty()) {
        return new ArrayList<>(classBodies.get(key));
      } else {
        return new ArrayList<>();
      }
    }
  }

  private static List<String> getExtendedImports(String key, Map<String, String> baseClasses, Map<String, List<String>> classImports) {

    if (!baseClasses.get(key).isEmpty()) {
      if (!classImports.get(key).isEmpty()) {
        List<String> fullBody = new ArrayList<>(getExtendedBodies(baseClasses.get(key), baseClasses, classImports));
        fullBody.addAll(classImports.get(key));
        return fullBody;
      } else {
        return getExtendedBodies(baseClasses.get(key), baseClasses, classImports);
      }
    } else {
      if (!classImports.get(key).isEmpty()) {
        return new ArrayList<>(classImports.get(key));
      } else {
        return new ArrayList<>();
      }
    }
  }

  private static void prepareForMoving(Map<String, String> baseClasses, Map<String, List<String>> classBodies, Map<String, List<String>> classImports,
    String filePath) throws IOException {
    if (!Files.exists(Paths.get(filePath))) {
      return;
    }

    List<String> fileLines = Files.readAllLines(Paths.get(filePath));
    String packageName = "";
    String modelName = "";
    List<String> imports = new ArrayList<>();
    List<String> bodyLines = new ArrayList<>();
    boolean isBody = false;

    for (String s : fileLines) {

      if (s.startsWith("package ")) {
        packageName = s.replace("package ", "").replace(";", "").trim();
      }
      if (s.startsWith("import com.dt.europe.hal.api.shared.model.") && !s.contains("import com.dt.europe.hal.api.shared.model.db.BasePk;")) {
        imports.add(s);
      }
      if (s.startsWith("import com.dt.europe.hal.api.shared.enums.")) {
        imports.add(s);
      }

      if (s.startsWith("}")) {
        isBody = false;
      }

      if (isBody) {
        bodyLines.add(s);
      }

      if (s.startsWith("public class ")) {
        if (!s.contains("}")) {
          isBody = true;
        }
        modelName = s.substring("public class ".length(), s.indexOf(" extends")).trim();

        String baseClass = s.substring(s.indexOf(" extends") + 9, s.indexOf(" {")).trim();
        String baseClassPackage = packageName + "." + baseClass;
        if (imports.stream().anyMatch(i -> i.trim().endsWith("." + baseClass + ";"))) {
          baseClassPackage = imports.stream().filter(i -> i.trim().endsWith("." + baseClass + ";")).findFirst().get();
          imports.remove(baseClassPackage);
        }
        if (baseClassPackage.endsWith(".BasePk")) {
          baseClassPackage = "";
        }
        if (baseClassPackage.startsWith("import ")) {
          baseClassPackage = baseClassPackage.replace("import ", "").replace(";", "");
        }
        baseClasses.put(packageName + "." + modelName, baseClassPackage);
      }
    }

    classBodies.put(packageName + "." + modelName, bodyLines);
    classImports.put(packageName + "." + modelName, imports);
  }

  public static void upgradeController(String projectName, String packageName)
    throws IOException {
    mainClass = Generator.class;

    GeneratorParams generatorParams = getParamsFromPackage(projectName, packageName);

    String controllerPath = generatorParams.getServicePackageName() + File.separator + "controller";
    Set<String> fileNames = listJavaFilesUsingFileWalk(controllerPath, 1);
    for (String fName : fileNames) {
      fixController(generatorParams.getServicePackageName() + File.separator + "controller" + File.separator + fName + ".java");
    }
  }

  private static void generateGenerator(GeneratorParams generatorParams, Configuration cfg)
    throws IOException, TemplateException {

    Set<String> modelImports =
      generatorParams.getVariables().stream()
        .map(v -> generatorParams.getModelPackageName() + "." + v.getName())
        .sorted()
        .collect(Collectors.toCollection(LinkedHashSet::new));

    Set<String> generators =
      generatorParams.getVariables().stream()
        .map(Variable::getName)
        .sorted()
        .collect(Collectors.toCollection(LinkedHashSet::new));

    Map<String, Object> data = new HashMap<>();
    data.put("packageName", generatorParams.getModelPackageName().replace(".model", ""));
    data.put("modelPackageName", generatorParams.getModelPackageName());
    data.put("className", generatorParams.getClassName());
    data.put("projectName", generatorParams.getValidatorPackageName());
    data.put("modelImports", modelImports);
    data.put("generators", generators);

    Template template = cfg.getTemplate("generator.ftl");
    StringWriter stringWriter = new StringWriter();
    template.process(data, stringWriter);

    log.info("\n{}", stringWriter.toString());

    String filePath =
      generatorParams
        .getServicePackageName()
        .replace(
          File.separator + "main" + File.separator, File.separator + "test" + File.separator);
    filePath = filePath.replace(File.separator + "model", "");
    createAbsoluteFolders(filePath, generatorParams.getModelPackageName().replace(".model", ""));
    String oldFilePath = filePath + File.separator + generatorParams.getClassName() + "Generator.java";
    String tempFilePath = filePath + File.separator + "Generator.java";
    filePath += File.separator + "ModelGenerator.java";
    Files.deleteIfExists(Paths.get(oldFilePath));
    Files.deleteIfExists(Paths.get(tempFilePath));
    Files.deleteIfExists(Paths.get(filePath));
    try (Writer fileWriter = new FileWriter(new File(filePath))) {
      template.process(data, fileWriter);
    }
  }

  private static void generateTemplate(GeneratorParams generatorParams, Configuration cfg)
    throws IOException, TemplateException {

    Set<String> modelImports =
      generatorParams.getVariables().stream()
        .map(v -> generatorParams.getModelPackageName() + "." + v.getName())
        .sorted()
        .collect(Collectors.toCollection(LinkedHashSet::new));

    Set<String> generators =
      generatorParams.getVariables().stream()
        .map(Variable::getName)
        .sorted()
        .collect(Collectors.toCollection(LinkedHashSet::new));

    Map<String, Object> data = new HashMap<>();
    data.put("packageName", generatorParams.getModelPackageName().replace(".model", ""));
    data.put("className", generatorParams.getClassName());
    data.put("modelImports", modelImports);
    data.put("generators", generators);

    Template template = cfg.getTemplate("template.ftl");
    StringWriter stringWriter = new StringWriter();
    template.process(data, stringWriter);

    log.info("\n{}", stringWriter.toString());

    String filePath =
      generatorParams
        .getServicePackageName()
        .replace(
          File.separator + "main" + File.separator, File.separator + "test" + File.separator);
    filePath = filePath.replace(File.separator + "model", "");
    createAbsoluteFolders(filePath, generatorParams.getModelPackageName().replace(".model", ""));
    filePath += File.separator + generatorParams.getClassName() + "TemplateTests.java";
    Files.deleteIfExists(Paths.get(filePath));
    try (Writer fileWriter = new FileWriter(new File(filePath))) {
      template.process(data, fileWriter);
    }
  }

  private static Set<String> listJavaFilesUsingFileWalk(String dir, int depth) throws IOException {
    String directory = dir;
    if(dir.contains("productorderingmanagementadmin")) directory = dir.replace("productorderingmanagementadmin","productorderingmanagement");
    try (Stream<Path> stream = Files.walk(Paths.get(directory), depth)) {
      return stream
        .filter(file -> !Files.isDirectory(file))
        .map(Path::getFileName)
        .map(Path::toString)
        .map(n -> n.replace(".java", ""))
        .collect(toSet());
    }
  }

  private static GeneratorParams getParamsFromPackage(String projectName, String modelPackageName)
    throws IOException {

    GeneratorParams generatorParams = GeneratorParams.builder().build();
    generatorParams.setModelPackageName(modelPackageName);
    generatorParams.setClassName(CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_CAMEL, projectName));

    String folderName = getProjectFolder();
    folderName =
      folderName.replace(
        File.separator + "common" + File.separator,
        File.separator + projectName + File.separator);
    folderName += "main" + File.separator + "java" + File.separator;
    folderName += modelPackageName.replace(".", File.separator);

    generatorParams.setServicePackageName(folderName);
    generatorParams.setValidatorPackageName(projectName);
    Set<String> fileNames = listJavaFilesUsingFileWalk(folderName, 1);

    fileNames.removeIf(f -> f.equalsIgnoreCase("Error") && projectName.equalsIgnoreCase("common"));
    fileNames.forEach(f -> generatorParams.getVariables().add(Variable.builder().name(f).build()));

    return generatorParams;
  }

  private static void generateValidator(GeneratorParams generatorParams, Configuration cfg)
    throws IOException, TemplateException {
    String packageName = generatorParams.getModelPackageName().replace(".model", ".validator");
    createFolders("main.java." + packageName + ".business");

    Set<String> serviceImports =
      generatorParams.getVariables().stream()
        .filter(Variable::isImported)
        .map(v -> v.getValidatorImportPackage() + "." + v.getValidatorClass())
        .sorted()
        .collect(Collectors.toCollection(LinkedHashSet::new));

    Set<String> serviceBeans =
      generatorParams.getVariables().stream()
        .map(v -> v.getValidatorClass() + " " + v.getValidatorName())
        .sorted()
        .collect(Collectors.toCollection(LinkedHashSet::new));

    List<Variable> validators =
      generatorParams.getVariables().stream()
        .filter(v -> !v.isList())
        .sorted()
        .collect(Collectors.toList());

    List<Variable> validatorsArr =
      generatorParams.getVariables().stream()
        .filter(Variable::isList)
        .sorted()
        .collect(Collectors.toList());

    boolean hasArray = !validatorsArr.isEmpty();

    Map<String, Object> data = new HashMap<>();
    data.put("packageName", packageName);
    data.put("className", generatorParams.getClassName() + "ValidatorService");
    data.put(
      "modelImport",
      generatorParams.getModelPackageName() + "." + generatorParams.getClassName());
    data.put(
      "businessValidatorImport",
      packageName + ".business" + "." + generatorParams.getClassName() + "RulesValidator");
    data.put("businessValidator", generatorParams.getClassName() + "RulesValidator");
    data.put("modelName", generatorParams.getClassName());
    data.put("hibernateValidator", "<ConstraintViolation<" + generatorParams.getClassName() + ">>");
    data.put("modelBracket", "<" + generatorParams.getClassName() + ">");
    data.put("serviceImports", serviceImports);
    data.put("serviceBeans", serviceBeans);
    data.put("validators", validators);
    data.put("validatorsArr", validatorsArr);
    data.put("hasArray", hasArray);

    Template template = cfg.getTemplate("validator.ftl");
    StringWriter stringWriter = new StringWriter();
    template.process(data, stringWriter);

    log.info("\n{}", stringWriter.toString());

    String filePath =
      getProjectFolder()
        + "main"
        + File.separator
        + "java"
        + File.separator
        + packageName.replace(".", File.separator)
        + File.separator;

    filePath += generatorParams.getClassName() + "ValidatorService.java";

    try (Writer fileWriter = new FileWriter(new File(filePath))) {
      template.process(data, fileWriter);

      generateBusinessRules(generatorParams, cfg);
    }
  }

  private static void generateBusinessRules(GeneratorParams generatorParams, Configuration cfg)
    throws IOException, TemplateException {

    String packageName =
      generatorParams.getModelPackageName().replace(".model", ".validator") + ".business";
    String className = generatorParams.getClassName() + "RulesValidator";

    String filePath =
      getProjectFolder()
        + "main"
        + File.separator
        + "java"
        + File.separator
        + packageName.replace(".", File.separator)
        + File.separator
        + className
        + ".java";

    File javaFile = new File(filePath);
    if (javaFile.exists()) {
      log.info("File {} exists, skipping", filePath);
      return;
    }

    Map<String, Object> data = new HashMap<>();
    data.put("packageName", packageName);
    data.put("className", className);
    data.put(
      "modelImport",
      generatorParams.getModelPackageName() + "." + generatorParams.getClassName());
    data.put("modelName", generatorParams.getClassName());

    Template template = cfg.getTemplate("rules.ftl");
    StringWriter stringWriter = new StringWriter();
    template.process(data, stringWriter);

    log.info("\n{}", stringWriter.toString());

    try (Writer fileWriter = new FileWriter(javaFile)) {
      template.process(data, fileWriter);
    }
  }

  private static String getModelEnumName(String className) {
    switch (className) {
      case "SSOToken":
        return "SSO_TOKEN";
      case "ServicePIN":
        return "SERVICE_PIN";
      case "OTP":
        return "OTP";
      case "ServicesOTP":
        return "SERVICES_OTP";
      case "Details2FA":
        return "DETAILS_2FA";
      case "ServicesUP":
        return "SERVICES_UP";
      case "ServicesPIN":
        return "SERVICES_PIN";
      case "PIN":
        return "PIN";
      default:
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, className);
    }
  }

  private static void generateService(GeneratorParams generatorParams, Configuration cfg)
    throws IOException, TemplateException {
    String packageName = generatorParams.getServicePackageName();
    createFolders("main.java." + packageName);

    Set<String> serviceImports =
      generatorParams.getVariables().stream()
        .filter(Variable::isImported)
        .map(v -> v.getServiceImportPackage() + "." + v.getServiceClass())
        .sorted()
        .collect(Collectors.toCollection(LinkedHashSet::new));

    Set<String> serviceBeans =
      generatorParams.getVariables().stream()
        .map(v -> v.getServiceClass() + " " + v.getServiceName())
        .sorted()
        .collect(Collectors.toCollection(LinkedHashSet::new));

    List<Variable> services =
      generatorParams.getVariables().stream()
        .filter(v -> !v.isList())
        .sorted()
        .collect(Collectors.toList());

    List<Variable> servicesArr =
      generatorParams.getVariables().stream()
        .filter(Variable::isList)
        .sorted()
        .collect(Collectors.toList());

    boolean hasRelations = !services.isEmpty() || !servicesArr.isEmpty();

    Map<String, Object> data = new HashMap<>();
    data.put("packageName", generatorParams.getServicePackageName());
    data.put("className", generatorParams.getClassName() + "Service");
    data.put(
      "modelImport",
      generatorParams.getModelPackageName() + "." + generatorParams.getClassName());
    data.put("modelName", generatorParams.getClassName());
    data.put(
      "modelEnumName",
      getModelEnumName(generatorParams.getClassName()));
    data.put("modelBracket", "<" + generatorParams.getClassName() + ">");
    data.put("stringBracket", "<String>");
    data.put("serviceImports", serviceImports);
    data.put("serviceBeans", serviceBeans);
    data.put("relatedServices", services);
    data.put("relatedServicesArr", servicesArr);
    data.put("hasRelations", hasRelations);

    Template template = cfg.getTemplate("service.ftl");
    StringWriter stringWriter = new StringWriter();
    template.process(data, stringWriter);

    log.info("\n{}", stringWriter.toString());

    String filePath =
      getProjectFolder()
        + "main"
        + File.separator
        + "java"
        + File.separator
        + packageName.replace(".", File.separator)
        + File.separator
        + generatorParams.getClassName()
        + "Service.java";
    try (Writer fileWriter = new FileWriter(new File(filePath))) {
      template.process(data, fileWriter);
    }
  }

  private static void createFolders(String path) {
    StringBuilder currentFolder = new StringBuilder(getProjectFolder());
    for (String s : path.split("\\.")) {
      currentFolder.append(s).append(File.separator);
      new File(currentFolder.toString()).mkdirs();
    }
  }

  private static void createAbsoluteFolders(String path, String modelPackage) {
    StringBuilder currentFolder =
      new StringBuilder(
        path.substring(0, path.indexOf(File.separator + "src" + File.separator + "test"))
          + File.separator
          + "src"
          + File.separator
          + "test"
          + File.separator
          + "java"
          + File.separator);
    for (String s : modelPackage.split("\\.")) {
      currentFolder.append(s).append(File.separator);
      new File(currentFolder.toString()).mkdirs();
    }
  }
}
