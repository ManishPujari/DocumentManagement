package com.dt.europe.hal.api.common.service;

import com.dt.europe.hal.api.common.exceptions.ExampleException;
import com.dt.europe.hal.api.common.model.ResponseParams;
import com.dt.europe.hal.api.common.model.tmf.Error;
import com.dt.europe.hal.api.common.repository.ResponseParamsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ErrorService {


  private final ResponseParamsRepository responseParamsRepository;

  public void hasException(String exampleId) throws ExampleException {

    ResponseParams responseParams = responseParamsRepository.findByExampleId(exampleId);

    if (responseParams != null && responseParams.getErrorResponse() != null) {
      throw new ExampleException(exampleId);
    }
  }

  public Error getErrorOnExample(String exampleId) {
    ResponseParams responseParams = responseParamsRepository.findByExampleId(exampleId);

    if (responseParams != null && responseParams.getErrorResponse() != null) {
      return responseParams.getErrorResponse();
    }
    throw new RuntimeException("Error object not valid");
  }
}
