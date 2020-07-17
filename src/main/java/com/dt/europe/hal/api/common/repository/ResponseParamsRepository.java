package com.dt.europe.hal.api.common.repository;

import com.dt.europe.hal.api.common.model.ResponseParams;
import com.dt.europe.hal.api.common.utils.FileReaderUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import java.io.File;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

@Repository
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ResponseParamsRepository {

  private final ObjectMapper objectMapper;

  public ResponseParams findByExampleId(String exampleId) {
    Assert.notNull(exampleId, "ExampleId for ResponseParamsRepository is null");

    ResponseParams params = new ResponseParams();
    String jsonFile;
    try {
      jsonFile = FileReaderUtil.getResource("responses" + File.separator + (exampleId.indexOf('.') > -1 ? exampleId.substring(0, exampleId.indexOf(".")) : exampleId));
      ObjectReader objectReader = objectMapper.reader()
        .forType(new TypeReference<ResponseParams>() {});
      params = objectReader.readValue(jsonFile);
    } catch (IOException e) {
      log.debug("ResponseParams not found", e);
    }
    return params;
  }

  public String findBodyByRefId(String bodyRef) {
    Assert.notNull(bodyRef, "BodyRef for ResponseParamsRepository is null");

    String jsonFile = "";
    try {
      jsonFile = FileReaderUtil.getResource("entities" + File.separator + bodyRef);
    } catch (IOException e) {
      log.debug("ResponseParams body not found", e);
    }
    return jsonFile;
  }

}
