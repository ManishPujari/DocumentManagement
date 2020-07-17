package com.dt.europe.hal.api.common.validator;

import com.dt.europe.hal.api.common.exceptions.InputParamInvalidException;
import com.dt.europe.hal.api.common.model.RequestParams;
import com.dt.europe.hal.api.common.repository.RequestParamsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExampleValidatorService {

  private final RequestParamsRepository requestParamsRepository;

  public void validateGetParams(String exampleId, RequestParams parsedParams) throws InputParamInvalidException {
    Assert.notNull(exampleId, "ExampleId must be provided");
    Assert.notNull(parsedParams, "Parsed params must be provided");

    RequestParams expectedParams = requestParamsRepository.findByExampleId(exampleId);

    if (expectedParams.getPathParams() != null && !expectedParams.getPathParams()
                                                                 .isEmpty()) {
      if (parsedParams.getPathParams() == null || parsedParams.getPathParams()
                                                              .isEmpty()) {
        log.error("Parsed path params empty");
        throw new InputParamInvalidException("Parsed path params empty");
      } else {
        if (!parsedParams.getPathParams()
                         .containsAll(expectedParams.getPathParams())) {
          log.error("Parsed path params not equal\nParsed:\n{} \nExpected:\n{}", parsedParams.getPathParams(), expectedParams.getPathParams());
          throw new InputParamInvalidException("Path params not equal");
        }
      }
    }

    if (expectedParams.getQueryParams() != null && !expectedParams.getQueryParams()
                                                                  .isEmpty()) {
      if (parsedParams.getQueryParams() == null || parsedParams.getQueryParams()
                                                               .isEmpty()) {
        log.error("Parsed query params not equal");
        throw new InputParamInvalidException("Parsed query params empty");
      } else {
        if (!parsedParams.getQueryParams()
                         .containsAll(expectedParams.getQueryParams())) {
          log.error("Parsed query params not equal\nParsed:\n{} \nExpected:\n{}", parsedParams.getQueryParams(), expectedParams.getQueryParams());
          throw new InputParamInvalidException("Parsed query not equal");
        }
      }
    }
  }
}
