package com.dt.europe.hal.api.common.filters;

import com.dt.europe.hal.api.common.model.filters.Example;
import com.dt.europe.hal.api.common.model.filters.Request;
import com.dt.europe.hal.api.common.model.filters.Response;
import com.dt.europe.hal.api.common.model.tmf.Characteristic;
import com.dt.europe.hal.api.common.utils.SwaggerUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

/**
 * A filter which logs web requests that lead to an error in the system.
 */
//@Component
@Slf4j
@SuppressWarnings("Duplicates")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LogRequestFilter extends OncePerRequestFilter implements Ordered {

  private final ObjectMapper mapper;

  // put filter at the end of all other filters to make sure we are processing after all others
  private int order = Ordered.LOWEST_PRECEDENCE - 8;

  @Override
  public int getOrder() {
    return order;
  }


  private String getReqBody(ContentCachingRequestWrapper request) {
    ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
    String payload = null;
    if (wrapper != null) {
      byte[] buf = wrapper.getContentAsByteArray();
      if (buf.length > 0) {
        try {
          payload = new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
        } catch (UnsupportedEncodingException ex) {
          log.error("{}" + ex);
        }

      }
    }
    return payload;
  }

  private String getRespBody(ContentCachingResponseWrapper response) {
    ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
    String payload = null;
    if (wrapper != null) {
      byte[] buf = wrapper.getContentAsByteArray();
      if (buf.length > 0) {
        try {
          payload = new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
        } catch (UnsupportedEncodingException ex) {
          log.error("{}" + ex);
        }

      }
    }
    return payload;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
    ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
    ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(res);

    // pass through filter chain to do the actual request handling
    filterChain.doFilter(wrappedRequest, wrappedResponse);
    Boolean isSaved = wrappedRequest.getHeader("X-Postman-Id") != null;

    if (isSaved) {

      String exampleName = wrappedRequest.getHeader("X-Postman-Id").replaceAll("\\.[^.]*$", "");
      if (exampleName.equalsIgnoreCase("swagger")) {
        SwaggerUtil.saveSwagger(getRespBody(wrappedResponse));
        wrappedResponse.copyBodyToResponse();
        return;
      }
      String queryString = (wrappedRequest.getQueryString() != null) ? "?" + wrappedRequest.getQueryString() : "";
      queryString = queryString.replace("%5B", "[")
        .replace("%5D", "]");
      String workingDir = System.getProperty("user.dir");
      final String FILE_SEPARATOR = System.getProperty("file.separator");
      String basePath = workingDir.substring(0, workingDir.lastIndexOf(FILE_SEPARATOR));
      String appPath = workingDir.substring(workingDir.lastIndexOf(FILE_SEPARATOR) + 1);
      Path directoryPath = Paths.get(basePath + FILE_SEPARATOR + "public" + FILE_SEPARATOR + "examples" + FILE_SEPARATOR + appPath);

      Example example = new Example();
      Request requestData = new Request();
      Response responseData = new Response();

      example.setVersion(wrappedRequest.getHeader("X-Postman-Id"));

      requestData.setUrl(wrappedRequest.getRequestURL() + queryString.replace("%2C", ",")
        .replace("%28", "(")
        .replace("%29", ")")
        .replace("%40", "@")
        .replace("%3D", "=")
      );
      requestData.setMethod(wrappedRequest.getMethod());
      requestData.setHeaders(new ArrayList<>());
      requestData.setCookies(new ArrayList<>());

      if (wrappedRequest.getHeader("X-MSISDN") != null) {
        requestData.getHeaders()
          .add(new Characteristic("X-MSISDN", wrappedRequest.getHeader("X-MSISDN")));
      }

      if (wrappedRequest.getHeader("Cookie") != null) {
        String[] cookies = wrappedRequest.getHeader("Cookie").split("; ");
        for (String cookie : cookies) {
          String[] data = cookie.split("=");
          requestData.getCookies()
            .add(new Characteristic(data[0], data[1]));
        }
      }

      if (wrappedRequest.getHeader("X-HTTP-Method-Override") != null) {
        requestData.getHeaders()
          .add(new Characteristic("X-HTTP-Method-Override", wrappedRequest.getHeader("X-HTTP-Method-Override")));
      }

      if (wrappedRequest.getHeader("X-otp") != null) {
        requestData.getHeaders()
          .add(new Characteristic("X-otp", wrappedRequest.getHeader("X-otp")));
      }

      if (wrappedRequest.getHeader("X-nonce") != null) {
        requestData.getHeaders()
          .add(new Characteristic("X-nonce", wrappedRequest.getHeader("X-nonce")));
      }

      if ("POST".equalsIgnoreCase(wrappedRequest.getMethod()) || "PATCH".equalsIgnoreCase(wrappedRequest.getMethod()) || "PUT".equalsIgnoreCase(
        wrappedRequest.getMethod())) {
        if ((wrappedRequest.getHeader("Accept") != null && wrappedRequest.getHeader("Accept")
          .toLowerCase()
          .contains("application/json")) || (
          wrappedRequest.getHeader("Content-Type") != null && wrappedRequest.getHeader("Content-Type")
            .toLowerCase()
            .contains("application/json"))) {
          requestData.setBody(SwaggerUtil.beautifyJson(getReqBody(wrappedRequest)));
        } else {
          requestData.setBody("{\"bodyType\" : \"" + wrappedRequest.getHeader("Content-Type") + "\"}");
        }
      }

      responseData.setStatus("" + res.getStatus());
      responseData.setCookies(new ArrayList<>());
      responseData.setHeaders(new ArrayList<>());

      if (res.getHeader("X-Content-Range") != null || res.getHeader("Content-Disposition") != null) {

      }
      if (res.getHeader("X-Content-Range") != null) {
        responseData.getHeaders()
          .add(new Characteristic("X-Content-Range", wrappedResponse.getHeader("X-Content-Range")));
      }
      if (res.getHeader("Location") != null) {
        responseData.getHeaders()
          .add(new Characteristic("Location", wrappedResponse.getHeader("Location")));
      }
      if (res.getHeader("Content-Disposition") != null) {
        responseData.getHeaders()
          .add(new Characteristic("Content-Disposition", wrappedResponse.getHeader("Content-Disposition")));
      }

      if (res.getHeader("Set-Cookie") != null) {
        res.getHeaders("Set-Cookie")
          .forEach(c -> {
              String[] data = c.split("=");
              responseData.getCookies()
                .add(new Characteristic(data[0], data[1]));
            }
          );
      }

      if ("GET".equalsIgnoreCase(wrappedRequest.getMethod()) || "POST".equalsIgnoreCase(wrappedRequest.getMethod()) || "PATCH".equalsIgnoreCase(
        wrappedRequest.getMethod()) || "DELETE".equalsIgnoreCase(wrappedRequest.getMethod()) || "PUT".equalsIgnoreCase(wrappedRequest.getMethod())) {
        try {
          if ((wrappedRequest.getHeader("Accept") != null && wrappedRequest.getHeader("Accept")
            .toLowerCase()
            .contains("application/json")) || (
            wrappedRequest.getHeader("Content-Type") != null && wrappedRequest.getHeader("Content-Type")
              .toLowerCase()
              .contains("application/json"))) {
            responseData.setBody(SwaggerUtil.beautifyJson(getRespBody(wrappedResponse)));
          } else {
            responseData.setBody("{\"bodyType\" : \"" + wrappedRequest.getHeader("Content-Type") + "\"}");
          }
        } catch (Exception e) {
          log.warn("Exception parsing body: {}", e);
        }
      }
      example.setRequest(requestData);
      example.setResponse(responseData);
      ObjectWriter writer = mapper.writer();

      String output = SwaggerUtil.beautifyJson(writer.withDefaultPrettyPrinter().writeValueAsString(example));
      File jsonDump = new File(directoryPath + File.separator + exampleName + ".json");
      jsonDump.createNewFile();

      FileWriter fileWriter = null;
      try {
        fileWriter = new FileWriter(directoryPath + File.separator + exampleName + ".json");
      } catch (IOException e) {
        log.error("Example writer exception: {}", e);
      }
      PrintWriter printWriter = new PrintWriter(fileWriter);
      printWriter.println(output);
      printWriter.close();
    }
    wrappedResponse.copyBodyToResponse();
  }


}
