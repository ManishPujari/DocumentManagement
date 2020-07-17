package com.dt.europe.hal.api.common.filters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonFilter {

  private static String DELIMITER = "/";
  private static String SIMPLE_MARKER = "$SO;";
  private static String OBJECT_MARKER = "$OO;";
  private static String ARRAY_MARKER = "$AO;";
  private static String NOT_MARKER = "$NOT;";

  public static void doFilter(Object dataNode, JsonNode filterNode, String dataPath, String filterPath) {

    JsonNode workingFilterNode = filterNode.at(filterPath);
    JsonNode workingDataNode = ((JsonNode) dataNode).at(dataPath);

    Iterator<String> filterFieldNameIterator = workingFilterNode.fieldNames();
    Set<String> filterFieldNameSet = new HashSet<>();

    while (filterFieldNameIterator.hasNext()) {
      filterFieldNameSet.add(filterFieldNameIterator.next());
    }
    if (filterFieldNameSet.isEmpty()) {
      return;
    }

    Iterator<String> dataFieldNameIterator = workingDataNode.fieldNames();
    Set<String> dataFieldNameSet = new HashSet<>();
    while (dataFieldNameIterator.hasNext()) {
      dataFieldNameSet.add(dataFieldNameIterator.next());
    }
    if (dataFieldNameSet.isEmpty()) {
      return;
    }

    if (filterFieldNameSet.contains(SIMPLE_MARKER) || filterFieldNameSet.contains(OBJECT_MARKER) || filterFieldNameSet.contains(ARRAY_MARKER)) {
      Iterator<Entry<String, JsonNode>> dataNodes = workingDataNode.fields();
      while (dataNodes.hasNext()) {
        Entry<String, JsonNode> entry = dataNodes.next();
        if ((filterFieldNameSet.contains(SIMPLE_MARKER) || filterFieldNameSet.contains(OBJECT_MARKER) || filterFieldNameSet.contains(ARRAY_MARKER))
          && workingDataNode.get(entry.getKey())
                            .isValueNode()) {
          filterFieldNameSet.add(entry.getKey());
          ((ObjectNode) filterNode).set(entry.getKey(), entry.getValue());

        }
        if ((filterFieldNameSet.contains(OBJECT_MARKER) || filterFieldNameSet.contains(ARRAY_MARKER)) && workingDataNode.get(entry.getKey())
                                                                                                                        .isObject()) {
          filterFieldNameSet.add(entry.getKey());
          ((ObjectNode) filterNode).set(entry.getKey(), entry.getValue());
        }
        if (filterFieldNameSet.contains(ARRAY_MARKER) && workingDataNode.get(entry.getKey())
                                                                        .isArray()) {
          filterFieldNameSet.add(entry.getKey());
          ((ObjectNode) filterNode).set(entry.getKey(), entry.getValue());
        }
      }
      filterFieldNameSet.remove(SIMPLE_MARKER);
      ((ObjectNode) filterNode).remove(SIMPLE_MARKER);
      filterFieldNameSet.remove(OBJECT_MARKER);
      ((ObjectNode) filterNode).remove(OBJECT_MARKER);
      filterFieldNameSet.remove(ARRAY_MARKER);
      ((ObjectNode) filterNode).remove(ARRAY_MARKER);
    }

    // remove all filter keys and the left will be the attribute name that we should remove
    dataFieldNameSet.removeAll(filterFieldNameSet);
    // remove the rest, except field(s) in filter key(s)
    Set<String> negativeFilterFieldNameSet = filterFieldNameSet.stream()
                                                               .filter(s -> s.startsWith(NOT_MARKER))
                                                               .map(s -> s.substring(NOT_MARKER.length()))
                                                               .collect(Collectors.toSet());

    if (!negativeFilterFieldNameSet.isEmpty()) {
      dataFieldNameSet.addAll(negativeFilterFieldNameSet);
      negativeFilterFieldNameSet.forEach(s -> ((ObjectNode) filterNode).remove(NOT_MARKER + s));
    }

    if (dataPath == null) {
      ((ObjectNode) dataNode).remove(dataFieldNameSet);
    } else {
      ((ObjectNode) ((JsonNode) dataNode).at(dataPath)).remove(dataFieldNameSet);
    }

//    filter sub objects
    Iterator<Entry<String, JsonNode>> dataNodes = workingDataNode.fields();
    while (dataNodes.hasNext()) {
      Entry<String, JsonNode> entry = dataNodes.next();
      log.trace("key --> " + entry.getKey() + " value-->" + entry.getValue());
      if (workingDataNode.get(entry.getKey())
                         .isObject() && workingFilterNode.get(entry.getKey())
                                                         .isObject()) {
        String workingDataPath = (dataPath == null) ? DELIMITER + entry.getKey() : dataPath + DELIMITER + entry.getKey();
        String workingFilterPath = (filterPath == null) ? DELIMITER + entry.getKey() : filterPath + DELIMITER + entry.getKey();
        doFilter(dataNode, filterNode, workingDataPath, workingFilterPath);
      }
      if (workingDataNode.get(entry.getKey())
                         .isArray() && workingFilterNode.get(entry.getKey())
                                                        .isObject()) {
        for (int i = 0; i < entry.getValue()
                                 .size(); i++) {
          String workingPath =
            (dataPath == null) ? DELIMITER + entry.getKey() + DELIMITER + i : dataPath + DELIMITER + entry.getKey() + DELIMITER + i;
          String workingFilterPath = (filterPath == null) ? DELIMITER + entry.getKey() : filterPath + DELIMITER + entry.getKey();
          doFilter(dataNode, filterNode, workingPath, workingFilterPath);
        }
      }
    }
  }
}
