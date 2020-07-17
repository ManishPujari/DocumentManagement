package com.dt.europe.hal.api.common.utils;

import com.dt.europe.hal.api.common.filters.FieldsParser;
import com.dt.europe.hal.api.common.filters.JsonFilter;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FieldsJsonFilter {

  private final ObjectMapper objectMapper;
  private final ObjectMapper yamlMapper;

  @Autowired
  public FieldsJsonFilter( @Qualifier("patchObjectMapper") ObjectMapper objectMapper, @Qualifier("yamlObjectMapper") ObjectMapper yamlMapper) {
    this.objectMapper = objectMapper;
    this.yamlMapper = yamlMapper;
  }

  @SuppressWarnings("Duplicates")
  public String applyFilter(String body, String fields) {

    if (body == null || body.isEmpty()) {
      return null;
    }

    if (fields == null || fields.isEmpty()) {
      return body;
    }

    log.debug("Filter: triggered");

    FieldsParser fp = new FieldsParser(fields);
    String parsed = fp.optimizeParsed(fp.parse());

    JsonNode filterNode;
    JsonNode body2;
    try {
      filterNode = yamlMapper.readTree(parsed);
      body2 = objectMapper.readTree(body);
    } catch (JsonParseException jpe) {
      log.warn("Wrong fields format: {}\nparsed:\n{}", fields, parsed);
      return body;
    } catch (IOException e) {
      log.warn("IO exception: {}", e.getMessage());
      return body;
    }
    log.debug("RestFilter: filter keys = {}", parsed);

    JsonFilter.doFilter(body2, filterNode, null, null);
    try {
      return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(body2);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return body;
    }
  }
}
