package ${packageName};

<#if hasRelations>
import com.dt.europe.hal.api.common.model.RelationEntityEnum;
import com.dt.europe.hal.api.common.repository.RelationRepository;
</#if>
import com.dt.europe.hal.api.common.model.RepositoryContextEnum;
import com.dt.europe.hal.api.common.repository.FileRepository;
<#list serviceImports as si>
import ${si};
</#list>
import ${modelImport};
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
<#if hasRelations>
import java.util.Optional;
</#if>

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

@org.springframework.stereotype.Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SuppressWarnings("Duplicates")
public class ${className} {

  private final FileRepository fileRepository;
<#if hasRelations>
  private final RelationRepository relationRepository;
</#if>
  private final ObjectMapper objectMapper;

  <#list serviceBeans as sb>
  private final ${sb};
  </#list>

  private void populateRelations(${modelName} entity, String entityId) {

  <#list relatedServices as rs>
    entity.set${rs.upperName}(Optional.ofNullable(${rs.serviceName}
      .getTemplate(relationRepository.getChildren(RelationEntityEnum.${modelEnumName},
        "${rs.name}",
        RelationEntityEnum.${rs.enumName},
        entityId)))
    .orElse(entity.get${rs.upperName}()));

  </#list>
  <#list relatedServicesArr as rs>
    entity.set${rs.upperName}(Optional.ofNullable(${rs.serviceName}
    .getTemplateList(relationRepository.getChildren(RelationEntityEnum.${modelEnumName},
    "${rs.name}",
    RelationEntityEnum.${rs.enumName},
    entityId)))
    .orElse(entity.get${rs.upperName}()));

  </#list>
  }

  private List${modelBracket} getEntityList(List${stringBracket} entityIds, RepositoryContextEnum context) {
      if (CollectionUtils.isEmpty(entityIds)) {
        return null;
      }

      List${modelBracket} entities = new ArrayList<>();
        entityIds.forEach(id -> entities.add(this.getEntity(id, context)));
      return entities;
  }

  private ${modelName} getEntity(String entityId, RepositoryContextEnum context) {
      if (entityId == null) {
        return null;
      }

      String jsonFile = fileRepository.findExampleByPath(entityId, context);
      ObjectReader objectReader = objectMapper.reader()
          .forType(new TypeReference${modelBracket}() {});
      ${modelName} entity;
      try {
          entity = objectReader.readValue(jsonFile);
        } catch (IOException e) {
          log.error("Json mapper exception", e);
          throw new RuntimeException(e);
        }
      this.populateRelations(entity, entityId);
      return entity;
  }

  public List${modelBracket} getResponseList(String entityIds) {
    List${stringBracket} examples = new ArrayList<>(Arrays.asList(entityIds.split(",")));
    return this.getEntityList(examples, RepositoryContextEnum.RESPONSE);
  }

  public ${modelName} getResponse(String entityId) {
    List${modelBracket} entities = this.getResponseList(entityId);
    return entities.get(0);
  }

  public List${modelBracket} getRequestList(String entityIds) {
    List${stringBracket} examples = new ArrayList<>(Arrays.asList(entityIds.split(",")));
    return this.getEntityList(examples, RepositoryContextEnum.REQUEST);
  }

  public ${modelName} getRequest(String entityId) {
    List${modelBracket} entities = this.getRequestList(entityId);
    return entities.get(0);
  }

  public List${modelBracket} getTemplateList(String entityIds) {
    if (entityIds == null) {
      return null;
    }
    List${stringBracket} examples = new ArrayList<>(Arrays.asList(entityIds.split(",")));
      return this.getEntityList(examples, RepositoryContextEnum.TEMPLATE);
  }

  public ${modelName} getTemplate(String entityId) {
    if (entityId == null) {
      return null;
    }
    List${modelBracket} entities = this.getTemplateList(entityId);
    return entities.get(0);
  }

}
