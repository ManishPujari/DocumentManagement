package com.dt.europe.hal.api.documentmanagement.service;

import com.dt.europe.hal.api.common.model.RelationEntityEnum;
import com.dt.europe.hal.api.common.repository.RelationRepository;
import com.dt.europe.hal.api.common.model.RepositoryContextEnum;
import com.dt.europe.hal.api.common.repository.FileRepository;
import com.dt.europe.hal.api.common.service.tmf.CharacteristicService;
import com.dt.europe.hal.api.documentmanagement.model.Document;
import com.dt.europe.hal.api.documentmanagement.model.RelatedObject;
import com.dt.europe.hal.api.documentmanagement.model.enums.DocumentTypeEnum;
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
public class DocumentService {

  private final FileRepository fileRepository;
  private final RelationRepository relationRepository;
  private final ObjectMapper objectMapper;

  private final AttachmentService attachmentService;
  private final CharacteristicService characteristicService;
  private final DocumentRelationshipService documentRelationshipService;
  private final DocumentSpecificationService documentSpecificationService;
  private final RelatedObjectService relatedObjectService;
  private final RelatedPartyRefService relatedPartyRefService;

  private void populateRelations(Document entity, String entityId) {

    entity.setRelatedObject(Optional.ofNullable(relatedObjectService
      .getTemplate(relationRepository.getChildren(RelationEntityEnum.DOCUMENT,
        "relatedObject",
        RelationEntityEnum.RELATED_OBJECT,
        entityId)))
    .orElse(entity.getRelatedObject()));

    entity.setRelatedParty(Optional.ofNullable(relatedPartyRefService
      .getTemplate(relationRepository.getChildren(RelationEntityEnum.DOCUMENT,
        "relatedParty",
        RelationEntityEnum.RELATED_PARTY_REF,
        entityId)))
    .orElse(entity.getRelatedParty()));

    entity.setAttachments(Optional.ofNullable(attachmentService
    .getTemplateList(relationRepository.getChildren(RelationEntityEnum.DOCUMENT,
    "attachments",
    RelationEntityEnum.ATTACHMENT,
    entityId)))
    .orElse(entity.getAttachments()));

    entity.setCharacteristics(Optional.ofNullable(characteristicService
    .getTemplateList(relationRepository.getChildren(RelationEntityEnum.DOCUMENT,
    "characteristics",
    RelationEntityEnum.CHARACTERISTIC,
    entityId)))
    .orElse(entity.getCharacteristics()));

    entity.setDocumentRelationships(Optional.ofNullable(documentRelationshipService
    .getTemplateList(relationRepository.getChildren(RelationEntityEnum.DOCUMENT,
    "documentRelationships",
    RelationEntityEnum.DOCUMENT_RELATIONSHIP,
    entityId)))
    .orElse(entity.getDocumentRelationships()));

    entity.setDocumentSpecifications(Optional.ofNullable(documentSpecificationService
    .getTemplateList(relationRepository.getChildren(RelationEntityEnum.DOCUMENT,
    "documentSpecifications",
    RelationEntityEnum.DOCUMENT_SPECIFICATION,
    entityId)))
    .orElse(entity.getDocumentSpecifications()));

  }

  private List<Document> getEntityList(List<String> entityIds, RepositoryContextEnum context) {
      if (CollectionUtils.isEmpty(entityIds)) {
        return null;
      }

      List<Document> entities = new ArrayList<>();
        entityIds.forEach(id -> entities.add(this.getEntity(id, context)));
      return entities;
  }

  private Document getEntity(String entityId, RepositoryContextEnum context) {
      if (entityId == null) {
        return null;
      }

      String jsonFile = fileRepository.findExampleByPath(entityId, context);
      ObjectReader objectReader = objectMapper.reader()
          .forType(new TypeReference<Document>() {});
      Document entity;
      try {
          entity = objectReader.readValue(jsonFile);
        } catch (IOException e) {
          log.error("Json mapper exception", e);
          throw new RuntimeException(e);
        }
      this.populateRelations(entity, entityId);
      return entity;
  }

  public List<Document> getResponseList(String entityIds) {
   // List<String> examples = new ArrayList<>(Arrays.asList(entityIds.split(",")));
   // return this.getEntityList(examples, RepositoryContextEnum.RESPONSE);
	  List<Document> examples = new ArrayList<>();
	  
	  Document doc = new Document();
  	doc.setId("2492-be78-4beb-a4fc-ed1012c22320");
  	doc.setDocumentType(DocumentTypeEnum.CONTRACT);
  	doc.setRelatedObject(new RelatedObject("19892b9e-a181-4a07-917c-1ad6932b5797", "product", null, null));
  	examples.add(doc);
return examples;
  }

  public Document getResponse(String entityId) {
    List<Document> entities = this.getResponseList(entityId);
    return entities.get(0);
	   
  }

  public List<Document> getRequestList(String entityIds) {
    List<String> examples = new ArrayList<>(Arrays.asList(entityIds.split(",")));
    return this.getEntityList(examples, RepositoryContextEnum.REQUEST);
  }

  public Document getRequest(String entityId) {
    List<Document> entities = this.getRequestList(entityId);
    return entities.get(0);
  }

  public List<Document> getTemplateList(String entityIds) {
    if (entityIds == null) {
      return null;
    }
    List<String> examples = new ArrayList<>(Arrays.asList(entityIds.split(",")));
      return this.getEntityList(examples, RepositoryContextEnum.TEMPLATE);
  }

  public Document getTemplate(String entityId) {
    if (entityId == null) {
      return null;
    }
    List<Document> entities = this.getTemplateList(entityId);
    return entities.get(0);
  }

}
