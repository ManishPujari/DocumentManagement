package com.dt.europe.hal.api.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileReaderUtil {

  public static String getResource(String exampleName) throws IOException {
    String lines;
    String workingDir =
      System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "init"
        + File.separator;
    InputStream resource = new FileInputStream(workingDir + exampleName + ".json");
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource))) {
      lines = reader.lines()
        .collect(Collectors.joining("\n"));
    }
    return lines;
  }

  public static List<String> readRelations() {
    List<String> records = new ArrayList<>();

    String workingDir =
      System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "init"
        + File.separator + "relations.csv";

    try (BufferedReader br = new BufferedReader(new FileReader(workingDir))) {
      String line;
      int i = 0;
      while ((line = br.readLine()) != null) {
        if (i++ != 0) {
          records.add(line);
        }
      }
    } catch (FileNotFoundException e) {
      log.error("Relations file not found", e);
    } catch (IOException e) {
      log.error("Relations file IO problem", e);
    }
    return records;
  }

  public static byte[] getObject(String objectName) throws IOException {
    String workingDir =
      System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "init"
        + File.separator + "objects" + File.separator + objectName;
    return Files.readAllBytes(Paths.get(workingDir));
  }

  public static String getJsonFile(String fileName) throws IOException {
    String lines;
    InputStream resource = new FileInputStream(fileName);
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource))) {
      lines = reader.lines()
        .collect(Collectors.joining("\n"));
    }
    return lines;
  }

  private static String fixWorkingFolder(String workingFolder) {
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

  public static String getProjectFolder(Class clazz) {
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

}
