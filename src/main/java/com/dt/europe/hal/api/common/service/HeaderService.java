package com.dt.europe.hal.api.common.service;

import com.dt.europe.hal.api.common.model.ResponseParams;
import com.dt.europe.hal.api.common.repository.ResponseParamsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HeaderService {

  private final ResponseParamsRepository responseParamsRepository;

  public HttpHeaders getResponseHeaders(String exampleId) {

    ResponseParams responseParams = responseParamsRepository.findByExampleId(exampleId);

    if (responseParams == null || CollectionUtils.isEmpty(responseParams.getHeaders())) {
      return null;
    }

    HttpHeaders httpHeaders = new HttpHeaders();
    responseParams.getHeaders()
                  .forEach(header -> httpHeaders.add(header.getName(), header.getValue()));

    return httpHeaders;
  }
}
