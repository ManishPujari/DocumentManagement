package com.dt.europe.hal.api.common.utils;

import com.dt.europe.hal.api.common.model.ClassParams;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReflectionUtils {

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

  private static String getProjectFolder(Class clazz) {
    String workingFolder = clazz.getProtectionDomain().getCodeSource().getLocation().getPath();
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
    return workingFolder;
  }

  private static String getSimpleNameForCoreJavaType(String typeName) {
    switch (typeName) {
      case "java.lang.String":
        return "String";
      case "java.time.OffsetDateTime":
        return "OffsetDateTime";
      case "java.lang.Integer":
        return "Integer";
      case "java.lang.Boolean":
        return "Boolean";
      case "java.lang.Double":
        return "Double";
      case "java.lang.Float":
        return "Float";
      case "java.time.LocalDate":
        return "LocalDate";
      case "java.math.BigDecimal":
        return "BigDecimal";
      default:
        return typeName;
    }
  }

  private static Class getArrayClass(Field f) {
    ParameterizedType stringListType = (ParameterizedType) f.getGenericType();
    return (Class<?>) stringListType.getActualTypeArguments()[0];
  }

  private static boolean isArray(Field f) {
    return f.getType().getName().equalsIgnoreCase("java.util.List");
  }

  private static List<String> getEnumValues(Field f) {
    String constants = Arrays.toString(f.getType().getEnumConstants());
    return Arrays.asList(constants.substring(1, constants.length() - 1).split(","));
  }

  static List<Field> getAllFields(Class clazz) {
    if (clazz == null) {
      return Collections.emptyList();
    }

    if (clazz.getName().equalsIgnoreCase("com.dt.europe.hal.api.common.model.BaseTemplate")) {
      return Collections.emptyList();
    }

    List<Field> result = new ArrayList<>(getAllFields(clazz.getSuperclass()));
    List<Field> filteredFields = Arrays.stream(clazz.getDeclaredFields())
      .collect(Collectors.toList());
    result.addAll(filteredFields);
    return result;
  }

  private static ClassParams transform(Field f) {
    ClassParams classParams = new ClassParams();

    if (f.getType().getName().startsWith("com.dt.europe.hal.api.")) {
      classParams.setFullName(f.getType().getSimpleName() + ":" + f.getName());
    } else {
      classParams.setFullName(getSimpleNameForCoreJavaType(f.getType().getName()) + ":" + f.getName());
    }

    if (isArray(f)) {
      Class arrayClass = getArrayClass(f);
      classParams.setFullName(f.getType().getSimpleName() + "<" + arrayClass.getSimpleName() + ">:" + f.getName());
      if (Enum.class.isAssignableFrom(arrayClass)) {
        String constants = Arrays.toString(arrayClass.getEnumConstants());
        classParams.setEnumValues(Arrays.asList(constants.substring(1, constants.length() - 1).split(",")));
      }
    }

    if (Enum.class.isAssignableFrom(f.getType())) {
      classParams.setFullName(f.getType().getSimpleName() + ":" + f.getName());
      classParams.setEnumValues(getEnumValues(f));
    }

    return classParams;
  }

  public static Map<String, ClassParams> getClassParams(Class clazz) {
    return ReflectionUtils.getAllFields(clazz).stream().map(ReflectionUtils::transform)
      .collect(Collectors.toMap(ClassParams::getFullName, classParams -> classParams));
  }

  public static String getFolderForWrite(Class clazz) {
    return getProjectFolder(clazz);
  }

}
