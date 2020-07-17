package com.dt.europe.hal.api.documentmanagement.service;

import com.dt.europe.hal.api.common.model.RelationEntityEnum;
import com.dt.europe.hal.api.common.repository.RelationRepository;
import com.dt.europe.hal.api.common.model.RepositoryContextEnum;
import com.dt.europe.hal.api.common.repository.FileRepository;
import com.dt.europe.hal.api.documentmanagement.model.DocumentRelationship;
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
public class DocumentRelationshipService {

  private final FileRepository fileRepository;
  private final RelationRepository relationRepository;
  private final ObjectMapper objectMapper;

  private final DocumentRefService documentRefService;

  private void populateRelations(DocumentRelationship entity, String entityId) {

    entity.setDocument(Optional.ofNullable(documentRefService
      .getTemplate(relationRepository.getChildren(RelationEntityEnum.DOCUMENT_RELATIONSHIP,
        "document",
        RelationEntityEnum.DOCUMENT_REF,
        entityId)))
    .orElse(entity.getDocument()));

  }

  private List<DocumentRelationship> getEntityList(List<String> entityIds, RepositoryContextEnum context) {
      if (CollectionUtils.isEmpty(entityIds)) {
        return null;
      }

      List<DocumentRelationship> entities = new ArrayList<>();
        entityIds.forEach(id -> entities.add(this.getEntity(id, context)));
      return entities;
  }

  private DocumentRelationship getEntity(String entityId, RepositoryContextEnum context) {
      if (entityId == null) {
        return null;
      }

      String jsonFile = fileRepository.findExampleByPath(entityId, context);
      ObjectReader objectReader = objectMapper.reader()
          .forType(new TypeReference<DocumentRelationship>() {});
      DocumentRelationship entity;
      try {
          entity = objectReader.readValue(jsonFile);
        } catch (IOException e) {
          log.error("Json mapper exception", e);
          throw new RuntimeException(e);
        }
      this.populateRelations(entity, entityId);
      return entity;
  }

  public List<DocumentRelationship> getResponseList(String entityIds) {
    List<String> examples = new ArrayList<>(Arrays.asList(entityIds.split(",")));
    return this.getEntityList(examples, RepositoryContextEnum.RESPONSE);
  }

  public DocumentRelationship getResponse(String entityId) {
    List<DocumentRelationship> entities = this.getResponseList(entityId);
    return entities.get(0);
  }

  public List<DocumentRelationship> getRequestList(String entityIds) {
    List<String> examples = new ArrayList<>(Arrays.asList(entityIds.split(",")));
    return this.getEntityList(examples, RepositoryContextEnum.REQUEST);
  }

  public DocumentRelationship getRequest(String entityId) {
    List<DocumentRelationship> entities = this.getRequestList(entityId);
    return entities.get(0);
  }

  public List<DocumentRelationship> getTemplateList(String entityIds) {
    if (entityIds == null) {
      return null;
    }
    List<String> examples = new ArrayList<>(Arrays.asList(entityIds.split(",")));
      return this.getEntityList(examples, RepositoryContextEnum.TEMPLATE);
  }

  public DocumentRelationship getTemplate(String entityId) {
    if (entityId == null) {
      return null;
    }
    List<DocumentRelationship> entities = this.getTemplateList(entityId);
    return entities.get(0);
  }

}
