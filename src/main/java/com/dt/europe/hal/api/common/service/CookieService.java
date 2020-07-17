package com.dt.europe.hal.api.common.service;

import com.dt.europe.hal.api.common.model.ResponseParams;
import com.dt.europe.hal.api.common.repository.ResponseParamsRepository;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CookieService {

  private final ResponseParamsRepository responseParamsRepository;

  public List<Cookie> getResponseCookies(String exampleId) {

    ResponseParams responseParams = responseParamsRepository.findByExampleId(exampleId);

    if (responseParams == null || CollectionUtils.isEmpty(responseParams.getCookies())) {
      return new ArrayList<>();
    }

    List<Cookie> cookies = new ArrayList<>();
    responseParams.getCookies()
                  .forEach(cookie -> cookies.add(new Cookie(cookie.getName(), cookie.getValue())));

    return cookies;
  }
}
