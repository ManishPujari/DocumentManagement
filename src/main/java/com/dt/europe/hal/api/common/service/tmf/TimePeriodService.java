package com.dt.europe.hal.api.common.service.tmf;

import com.dt.europe.hal.api.common.model.RepositoryContextEnum;
import com.dt.europe.hal.api.common.model.tmf.TimePeriod;
import com.dt.europe.hal.api.common.repository.FileRepository;
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
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SuppressWarnings("Duplicates")
@Profile("common")
public class TimePeriodService {

  private final FileRepository fileRepository;
  private final ObjectMapper objectMapper;


  private void populateRelations(TimePeriod entity, String entityId) {

  }

  private List<TimePeriod> getEntityList(List<String> entityIds, RepositoryContextEnum context) {
      if (CollectionUtils.isEmpty(entityIds)) {
        return null;
      }

      List<TimePeriod> entities = new ArrayList<>();
        entityIds.forEach(id -> entities.add(this.getEntity(id, context)));
      return entities;
  }

  private TimePeriod getEntity(String entityId, RepositoryContextEnum context) {
      if (entityId == null) {
        return null;
      }

      String jsonFile = fileRepository.findExampleByPath(entityId, context);
      ObjectReader objectReader = objectMapper.reader()
          .forType(new TypeReference<TimePeriod>() {});
      TimePeriod entity;
      try {
          entity = objectReader.readValue(jsonFile);
        } catch (IOException e) {
          log.error("Json mapper exception", e);
          throw new RuntimeException(e);
        }
      this.populateRelations(entity, entityId);
      return entity;
  }

  public List<TimePeriod> getResponseList(String entityIds) {
    List<String> examples = new ArrayList<>(Arrays.asList(entityIds.split(",")));
    return this.getEntityList(examples, RepositoryContextEnum.RESPONSE);
  }

  public TimePeriod getResponse(String entityId) {
    List<TimePeriod> entities = this.getResponseList(entityId);
    return entities.get(0);
  }

  public List<TimePeriod> getRequestList(String entityIds) {
    List<String> examples = new ArrayList<>(Arrays.asList(entityIds.split(",")));
    return this.getEntityList(examples, RepositoryContextEnum.REQUEST);
  }

  public TimePeriod getRequest(String entityId) {
    List<TimePeriod> entities = this.getRequestList(entityId);
    return entities.get(0);
  }

  public List<TimePeriod> getTemplateList(String entityIds) {
    if (entityIds == null) {
      return null;
    }
    List<String> examples = new ArrayList<>(Arrays.asList(entityIds.split(",")));
      return this.getEntityList(examples, RepositoryContextEnum.TEMPLATE);
  }

  public TimePeriod getTemplate(String entityId) {
    if (entityId == null) {
      return null;
    }
    List<TimePeriod> entities = this.getTemplateList(entityId);
    return entities.get(0);
  }

}
