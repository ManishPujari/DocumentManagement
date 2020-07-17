package com.dt.europe.hal.api.common.filters;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
@Slf4j
public class FieldsFilterAdvice implements ResponseBodyAdvice<Object> {

  private static String FIELDS_PARAM = "fields";

  private final ObjectMapper objectMapper;
  private final ObjectMapper yamlMapper;

  @Autowired
  public FieldsFilterAdvice(ObjectMapper objectMapper, @Qualifier("yamlObjectMapper") ObjectMapper yamlMapper) {
    this.objectMapper = objectMapper;
    this.yamlMapper = yamlMapper;
  }

  @Override
  public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
    boolean isMethodSupported = returnType.hasMethodAnnotation(GetMapping.class);
    return (isMethodSupported);
  }

  @Override
  @SuppressWarnings("Duplicates")
  public Object beforeBodyWrite(Object body,
                                MethodParameter returnType,
                                MediaType selectedContentType,
                                Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                ServerHttpRequest request,
                                ServerHttpResponse response) {

    if (body == null) {
      return null;
    }

    HttpServletResponse httpServletResponse = ((ServletServerHttpResponse) response).getServletResponse();

    if (200 != httpServletResponse.getStatus()) {
      return body;
    }

    HttpServletRequest httpServletRequest = ((ServletServerHttpRequest) request).getServletRequest();

    // check whether the request has filter key or not
    if (httpServletRequest.getParameterMap()
                          .containsKey(FIELDS_PARAM)) {
      String fields = httpServletRequest.getParameter(FIELDS_PARAM)
                                        .replace("\n", "");
      if (fields == null || fields.isEmpty()) {
        return body;
      }
      log.debug("RestFilter: triggered");

      FieldsParser fp = new FieldsParser(fields);
      String parsed = fp.optimizeParsed(fp.parse());

      JsonNode filterNode;
      try {
        filterNode = yamlMapper.readTree(parsed);
      } catch (JsonParseException jpe) {
        log.warn("Wrong fields format: {}\nparsed:\n{}", fields, parsed);
        return body;
      } catch (IOException e) {
        log.warn("IO exception: {}", e.getMessage());
        return body;
      }
      log.debug("RestFilter: filter keys = {}", parsed);

      if (body instanceof Collection) {
        Collection<Object> body2 = (Collection<Object>) body;
        return body2.stream()
                    .map(objectMapper::valueToTree)
                    .map(e -> {
                      JsonFilter.doFilter(e, filterNode, null, null);
                      return e;
                    })
                    .collect(Collectors.toList());
      } else {
        Object body2 = objectMapper.valueToTree(body);
        JsonFilter.doFilter(body2, filterNode, null, null);
        return body2;
      }

    }

    return body;
  }
}
