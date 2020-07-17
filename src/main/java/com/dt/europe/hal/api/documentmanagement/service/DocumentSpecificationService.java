package com.dt.europe.hal.api.documentmanagement.service;

import com.dt.europe.hal.api.common.model.RepositoryContextEnum;
import com.dt.europe.hal.api.common.repository.FileRepository;
import com.dt.europe.hal.api.documentmanagement.model.DocumentSpecification;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

@org.springframework.stereotype.Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SuppressWarnings("Duplicates")
public class DocumentSpecificationService {

  private final FileRepository fileRepository;
  private final ObjectMapper objectMapper;


  private void populateRelations(DocumentSpecification entity, String entityId) {

  }

  private List<DocumentSpecification> getEntityList(List<String> entityIds, RepositoryContextEnum context) {
      if (CollectionUtils.isEmpty(entityIds)) {
        return null;
      }

      List<DocumentSpecification> entities = new ArrayList<>();
        entityIds.forEach(id -> entities.add(this.getEntity(id, context)));
      return entities;
  }

  private DocumentSpecification getEntity(String entityId, RepositoryContextEnum context) {
      if (entityId == null) {
        return null;
      }

      String jsonFile = fileRepository.findExampleByPath(entityId, context);
      ObjectReader objectReader = objectMapper.reader()
          .forType(new TypeReference<DocumentSpecification>() {});
      DocumentSpecification entity;
      try {
          entity = objectReader.readValue(jsonFile);
        } catch (IOException e) {
          log.error("Json mapper exception", e);
          throw new RuntimeException(e);
        }
      this.populateRelations(entity, entityId);
      return entity;
  }

  public List<DocumentSpecification> getResponseList(String entityIds) {
    List<String> examples = new ArrayList<>(Arrays.asList(entityIds.split(",")));
    return this.getEntityList(examples, RepositoryContextEnum.RESPONSE);
  }

  public DocumentSpecification getResponse(String entityId) {
    List<DocumentSpecification> entities = this.getResponseList(entityId);
    return entities.get(0);
  }

  public List<DocumentSpecification> getRequestList(String entityIds) {
    List<String> examples = new ArrayList<>(Arrays.asList(entityIds.split(",")));
    return this.getEntityList(examples, RepositoryContextEnum.REQUEST);
  }

  public DocumentSpecification getRequest(String entityId) {
    List<DocumentSpecification> entities = this.getRequestList(entityId);
    return entities.get(0);
  }

  public List<DocumentSpecification> getTemplateList(String entityIds) {
    if (entityIds == null) {
      return null;
    }
    List<String> examples = new ArrayList<>(Arrays.asList(entityIds.split(",")));
      return this.getEntityList(examples, RepositoryContextEnum.TEMPLATE);
  }

  public DocumentSpecification getTemplate(String entityId) {
    if (entityId == null) {
      return null;
    }
    List<DocumentSpecification> entities = this.getTemplateList(entityId);
    return entities.get(0);
  }

}
