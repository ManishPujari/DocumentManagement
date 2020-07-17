package com.dt.europe.hal.api.documentmanagement.service;

import com.dt.europe.hal.api.common.model.RelationEntityEnum;
import com.dt.europe.hal.api.common.repository.RelationRepository;
import com.dt.europe.hal.api.common.model.RepositoryContextEnum;
import com.dt.europe.hal.api.common.repository.FileRepository;
import com.dt.europe.hal.api.common.service.tmf.TimePeriodService;
import com.dt.europe.hal.api.documentmanagement.model.RelatedPartyRef;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

@org.springframework.stereotype.Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SuppressWarnings("Duplicates")
public class RelatedPartyRefService {

  private final FileRepository fileRepository;
  private final RelationRepository relationRepository;
  private final ObjectMapper objectMapper;

  private final TimePeriodService timePeriodService;

  private void populateRelations(RelatedPartyRef entity, String entityId) {

    entity.setValidFor(Optional.ofNullable(timePeriodService
      .getTemplate(relationRepository.getChildren(RelationEntityEnum.RELATED_PARTY_REF,
        "validFor",
        RelationEntityEnum.TIME_PERIOD,
        entityId)))
    .orElse(entity.getValidFor()));

  }

  private List<RelatedPartyRef> getEntityList(List<String> entityIds, RepositoryContextEnum context) {
      if (CollectionUtils.isEmpty(entityIds)) {
        return null;
      }

      List<RelatedPartyRef> entities = new ArrayList<>();
        entityIds.forEach(id -> entities.add(this.getEntity(id, context)));
      return entities;
  }

  private RelatedPartyRef getEntity(String entityId, RepositoryContextEnum context) {
      if (entityId == null) {
        return null;
      }

      String jsonFile = fileRepository.findExampleByPath(entityId, context);
      ObjectReader objectReader = objectMapper.reader()
          .forType(new TypeReference<RelatedPartyRef>() {});
      RelatedPartyRef entity;
      try {
          entity = objectReader.readValue(jsonFile);
        } catch (IOException e) {
          log.error("Json mapper exception", e);
          throw new RuntimeException(e);
        }
      this.populateRelations(entity, entityId);
      return entity;
  }

  public List<RelatedPartyRef> getResponseList(String entityIds) {
    List<String> examples = new ArrayList<>(Arrays.asList(entityIds.split(",")));
    return this.getEntityList(examples, RepositoryContextEnum.RESPONSE);
  }

  public RelatedPartyRef getResponse(String entityId) {
    List<RelatedPartyRef> entities = this.getResponseList(entityId);
    return entities.get(0);
  }

  public List<RelatedPartyRef> getRequestList(String entityIds) {
    List<String> examples = new ArrayList<>(Arrays.asList(entityIds.split(",")));
    return this.getEntityList(examples, RepositoryContextEnum.REQUEST);
  }

  public RelatedPartyRef getRequest(String entityId) {
    List<RelatedPartyRef> entities = this.getRequestList(entityId);
    return entities.get(0);
  }

  public List<RelatedPartyRef> getTemplateList(String entityIds) {
    if (entityIds == null) {
      return null;
    }
    List<String> examples = new ArrayList<>(Arrays.asList(entityIds.split(",")));
      return this.getEntityList(examples, RepositoryContextEnum.TEMPLATE);
  }

  public RelatedPartyRef getTemplate(String entityId) {
    if (entityId == null) {
      return null;
    }
    List<RelatedPartyRef> entities = this.getTemplateList(entityId);
    return entities.get(0);
  }

}
