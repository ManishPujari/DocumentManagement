package com.dt.europe.hal.api.common.repository;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import com.dt.europe.hal.api.common.model.Relation;
import com.dt.europe.hal.api.common.model.RelationEntityEnum;
import com.dt.europe.hal.api.common.utils.FileReaderUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

@Repository
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RelationRepository {

  private final ObjectMapper objectMapper;

  private List<Relation> getRelations(RelationEntityEnum parent, String parentId) {
    Assert.notNull(parent, "Parent cannot be null");
    Assert.notNull(parentId, "ParentId cannot be null");

    List<Relation> relations = new ArrayList<>();

    List<String> relationCsv = FileReaderUtil.readRelations();
    relationCsv.forEach(line -> {
      String[] values = line.split(",");
      Relation relation = new Relation(RelationEntityEnum.fromValue(values[0]),
        values[1],
        RelationEntityEnum.fromValue(values[2]),
        values[3],
        values[4]);
      relations.add(relation);
    });

    return relations.stream()
      .filter(r -> (r.getParent()
        .compareTo(parent) == 0 && r.getParentId()
        .compareTo(parentId) == 0))
      .collect(collectingAndThen(toList(), ImmutableList::copyOf));
  }

  private List<Relation> getRelationData(RelationEntityEnum parent, String variableName, RelationEntityEnum child, String parentId) {
    Assert.notNull(parent, "Parent cannot be null");
    Assert.notNull(variableName, "Parent variable name cannot be null");
    Assert.notNull(child, "Child cannot be null");
    Assert.notNull(parentId, "ParentId cannot be null");

    return this.getRelations(parent, parentId)
      .stream()
      .filter(r -> (r.getChild()
        .compareTo(child) == 0) && r.getVariableName()
        .compareTo(variableName) == 0)
      .collect(toList());
  }

  public String getChildren(RelationEntityEnum parent, String variableName, RelationEntityEnum child, String parentId) {
    List<Relation> relations = this.getRelationData(parent, variableName, child, parentId);
    if (relations.isEmpty()) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    int i = 0;
    for (String s : relations.stream().map(Relation::getChildId).collect(toList())) {
      if (i++ > 0) {
        sb.append(",");
      }
      sb.append(s);
    }
    return sb.toString();
  }

}
