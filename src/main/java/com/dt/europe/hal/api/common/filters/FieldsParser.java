package com.dt.europe.hal.api.common.filters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FieldsParser {

  private static String NEW_LINE = "\n";
  private static String TAB = " ";
  private static String DELIMITER = ": ";
  private static String DOT = ".";

  private final String fields;
  private final int limit;
  private final StringBuilder sb = new StringBuilder();
  private final StringBuilder yB = new StringBuilder();

  private HashMap<String, List<String>> tree = new HashMap<>();

  private int pointer = 0;
  private int stackOffset = 0;

  public FieldsParser(String fields) {
    this.fields = fields;

    if (this.fields == null || this.fields.isEmpty()) {
      throw new IllegalArgumentException("Input param is empty.");
    }

    this.limit = fields.length() - 1;
  }

  private static SpecialCharacterEnum isSpecialCharacter(char character) {
    switch (character) {
      case '[':
        return SpecialCharacterEnum.OB;
      case ']':
        return SpecialCharacterEnum.CB;
      case '.':
        return SpecialCharacterEnum.DOT;
      case ',':
        return SpecialCharacterEnum.COMMA;
      default:
        return SpecialCharacterEnum.NONE;
    }
  }

  private void appendMultiple(int counter) {
    for (int i = 0; i < counter; i++) {
      this.sb.append(TAB);
    }
  }

  public String parse() {
    return this.parse(0);
  }

  private String parse(int offset) {

    if (this.pointer > this.limit) {
      return this.sb.toString();
    }

    SpecialCharacterEnum sce;
    StringBuilder word = new StringBuilder();
    do {
      char temp = fields.charAt(this.pointer++);
      sce = isSpecialCharacter(temp);
      if (sce == SpecialCharacterEnum.NONE) {
        word.append(temp);
      }
    } while (sce == SpecialCharacterEnum.NONE && this.pointer <= this.limit);

    if (word.toString()
            .length() > 0) {
      this.sb.append(word.toString())
             .append(DELIMITER);
    }

    if (this.pointer > this.limit) {
      return this.sb.toString();
    }

    if (sce == SpecialCharacterEnum.COMMA) {
      this.sb.append(NEW_LINE);
      this.appendMultiple(this.stackOffset);
      return this.parse(this.stackOffset);
    }

    if (sce == SpecialCharacterEnum.DOT) {
      this.sb.append(NEW_LINE);
      this.appendMultiple(offset + 1);
      return this.parse(offset + 1);
    }

    if (sce == SpecialCharacterEnum.OB) {
      this.sb.append(NEW_LINE);
      this.stackOffset++;
      this.appendMultiple(offset + 1);
      return this.parse(offset + 1);
    }

    if (sce == SpecialCharacterEnum.CB) {
      this.stackOffset--;
      return this.parse(offset - 1);
    }

    return null;
  }

  private int calculateDepth(String value) {
    for (int i = 0; i < value.length(); i++) {
      if (value.charAt(i) != ' ') {
        return i;
      }
    }
    return 0;
  }

  private String calculatePath(String oldPath, int depth) {

    StringBuilder tempSB = new StringBuilder();
    String tempS = oldPath;
    for (int i = 0; i < depth; i++) {
      tempSB.append(DOT)
            .append(tempS, 0, tempS.indexOf(DOT));
      tempS = tempS.substring(tempS.indexOf(DOT) + 1);
    }
    return tempSB.toString()
                 .substring(1);
  }

  private void translateToTree(String parsed) {
    if (parsed == null || parsed.isEmpty()) {
      return;
    }
    String temp = parsed;
    StringBuilder path = new StringBuilder();
    do {
      String name = temp.substring(0, temp.indexOf(DELIMITER));

      int depth = this.calculateDepth(name);
      if (depth == 0) {
        path.delete(0, path.length());
        path.append(name);
        log.debug("{}:{}->{}", depth, path.toString(), name);
        tree.computeIfAbsent(name, k -> new ArrayList<>());
      } else {
        String value = name.trim();
        path.append(DOT)
            .append(value);
        String pathS = path.toString();
        String key = pathS.substring(0, pathS.lastIndexOf(DOT));
        if (getDepthByDots(pathS) != depth) {
          path.delete(0, path.length());
          path.append(calculatePath(key, depth))
              .append(DOT)
              .append(value);
          pathS = path.toString();
          key = pathS.substring(0, pathS.lastIndexOf(DOT));
        }
        tree.computeIfAbsent(key, k -> new ArrayList<>());
        List<String> values = tree.get(key);
        if (!values.contains(value)) {
          values.add(value);
        }
        log.debug("{}:{}->{}", depth, path.toString(), name);
      }
      try {
        temp = temp.substring(name.length() + DELIMITER.length() + 1);
      } catch (StringIndexOutOfBoundsException e) {
        temp = "";
      }

      if (temp.startsWith(NEW_LINE)) {
        temp = temp.substring(1);
      }
    } while (temp.length() > 0);

  }

  private int getDepthByDots(String key) {
    int counter = 0;
    for (int i = 0; i < key.length(); i++) {
      if (key.charAt(i) == '.') {
        counter++;
      }
    }
    return counter;
  }

  private void generateParsed(String key) {
    String name = key.substring(key.lastIndexOf(DOT) + 1);
    for (int i = 0; i < this.getDepthByDots(key); i++) {
      yB.append(TAB);
    }
    yB.append(name)
      .append(DELIMITER)
      .append(NEW_LINE);
    if (tree.get(key) != null) {
      tree.get(key)
          .forEach(value -> generateParsed(key + "." + value));
    }
  }

  public String optimizeParsed(String parsed) {
    if (parsed == null || parsed.isEmpty()) {
      return parsed;
    }
    String specialChars = parsed;
    if (specialChars.contains("-:")) {
      specialChars = specialChars.replace("-:", "$SO;:");
    }
    if (specialChars.contains("+:")) {
      specialChars = specialChars.replace("+:", "$OO;:");
    }
    if (specialChars.contains("*:")) {
      specialChars = specialChars.replace("*:", "$AO;:");
    }
    if (specialChars.contains("!")) {
      specialChars = specialChars.replace("!", "$NOT;");
    }
    this.translateToTree(specialChars);
    if (tree == null || tree.size() == 0) {
      return parsed;
    }
    tree.forEach((key, value) -> {
      if (!key.contains(DOT)) {
        yB.append(key)
          .append(DELIMITER)
          .append(NEW_LINE);

        value.forEach(name -> generateParsed(key + "." + name));
      }
    });
    String temp = yB.toString();
    return temp.substring(0, temp.lastIndexOf(NEW_LINE));
  }
}



