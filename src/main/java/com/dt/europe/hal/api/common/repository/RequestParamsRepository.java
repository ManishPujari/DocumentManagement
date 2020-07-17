package com.dt.europe.hal.api.common.repository;

import com.dt.europe.hal.api.common.model.RequestParams;
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
public class RequestParamsRepository {

  private final ObjectMapper objectMapper;

  public RequestParams findByExampleId(String exampleId) {
    Assert.notNull(exampleId, "ExampleId for RequestParamsRepository is null");

    RequestParams params = new RequestParams();
    String jsonFile;
    try {
      jsonFile = FileReaderUtil.getResource("requests" + File.separator + exampleId);

      ObjectReader objectReader = objectMapper.reader()
                                              .forType(new TypeReference<RequestParams>() {});
      params = objectReader.readValue(jsonFile);
    } catch (IOException e) {
      log.debug("RequestParams not found", e);
    }
    return params;
  }

  public String findBodyByRefId(String bodyRef) {
    Assert.notNull(bodyRef, "BodyRef for RequestParamsRepository is null");

    String jsonFile = "";
    try {
      jsonFile = FileReaderUtil.getResource("entities" + File.separator + bodyRef);
    } catch (IOException e) {
      log.debug("RequestParams body not found", e);
    }
    return jsonFile;
  }

}
