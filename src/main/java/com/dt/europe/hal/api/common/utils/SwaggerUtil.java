package com.dt.europe.hal.api.common.utils;

import static com.fasterxml.jackson.databind.DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS;
import static com.fasterxml.jackson.databind.node.JsonNodeFactory.withExactBigDecimals;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.jackson.mixin.ResponseSchemaMixin;
import io.swagger.models.ArrayModel;
import io.swagger.models.Operation;
import io.swagger.models.Response;
import io.swagger.models.SecurityRequirement;
import io.swagger.models.Swagger;
import io.swagger.models.auth.SecuritySchemeDefinition;
import io.swagger.models.parameters.QueryParameter;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.StringProperty;
import io.swagger.parser.SwaggerParser;
import io.swagger.util.Yaml;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("Duplicates")
public class SwaggerUtil {

  private static Swagger fixSwagger(Swagger swagger) {

//    fix schemes
    swagger.getSchemes()
           .removeIf(scheme -> scheme.name()
                                     .equalsIgnoreCase("http"));

//    fix security definitions
    Map<String, SecuritySchemeDefinition> securityDefinitions = swagger.getSecurityDefinitions();
    securityDefinitions.values()
                       .forEach(ssd -> {
                         if (ssd.getVendorExtensions() != null && ssd.getVendorExtensions()
                                                                     .containsKey("x-description")) {
                           ssd.setDescription((String) ssd.getVendorExtensions()
                                                          .get("x-description"));
                           ssd.getVendorExtensions()
                              .clear();

                         }
                       });

    securityDefinitions.keySet()
                       .forEach(sdk -> swagger.addSecurity(new SecurityRequirement().requirement(sdk)));

//    fix content range header on paths

    swagger.getPaths()
           .values()
           .forEach(path -> {

             if (path.getPost() != null && path.getPost()
                                               .getParameters()
                                               .stream()
                                               .anyMatch(p -> p.getName()
                                                               .equals("X-HTTP-Method-Override"))) {
               Operation operation = path.getPost();
               operation.getResponses()
                        .values()
                        .forEach(resp -> {
                          if (resp.getResponseSchema() instanceof ArrayModel) {
                            Property header = new StringProperty();
                            header.setDescription("returns paging info in format {page}/{limit}/{results}|*");
                            resp.addHeader("X-Content-Range", header);
                          }
                        });
             }

             if (path.getGet() != null) {
               Operation operation = path.getGet();
//        fix consumes on GET
               operation.setConsumes(new ArrayList<>());
               operation.getResponses()
                        .values()
                        .forEach(resp -> {
                          if (resp.getResponseSchema() instanceof ArrayModel) {
                            Property header = new StringProperty();
                            header.setDescription("returns paging info in format {page}/{limit}/{results}|*");
                            resp.addHeader("X-Content-Range", header);
                          }
                        });
             }
             if (path.getDelete() != null) {
               Operation operation = path.getDelete();
//        fix consumes on DELETE
               operation.setConsumes(new ArrayList<>());
             }
           });
    return swagger;
  }


  public static String beautifyJson(String json) {
    if (json == null || json.isEmpty()) {
      return json;
    }
    ObjectMapper om = new ObjectMapper(new JsonFactory());
    om.setNodeFactory(withExactBigDecimals(true));
    om.configure(USE_BIG_DECIMAL_FOR_FLOATS, true);
    JsonNode node;
    try {
      node = om.readTree(json);
    } catch (JsonParseException jpe) {
      log.warn("Not JSON format: {}", json);
      return "NOT_JSON";
    } catch (IOException e) {
      log.warn("IO exception: {}", e);
      return json;
    }
    String formattedJson = null;
    try {
      formattedJson = om.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(node);
    } catch (JsonProcessingException e) {
      log.warn("PrettyPrint exception: {}", e);
    }
    return formattedJson;

  }

  public static String beautifySwagger(String swagger) {
    Swagger tempSwagger = new SwaggerParser().parse(swagger);
    String formattedYaml = null;
    try {
//      https://github.com/swagger-api/swagger-parser/issues/772#issuecomment-406345407
      Yaml.mapper()
          .addMixIn(Response.class, ResponseSchemaMixin.class);
      Yaml.mapper()
          .addMixIn(QueryParameter.class, QueryParameterSchemaMixin.class);
      formattedYaml = Yaml.pretty()
                          .writeValueAsString(fixSwagger(tempSwagger));
    } catch (JsonProcessingException e) {
      log.warn("PrettyPrint exception: {}", e);
    }
    return formattedYaml;
  }

  public static void saveSwagger(String body) {
    final String FILE_SEPARATOR = System.getProperty("file.separator");
    //String workingDir = System.getProperty("user.dir").endsWith(FILE_SEPARATOR) ? System.getProperty("user.dir") : System.getProperty("user.dir") + FILE_SEPARATOR;
    String workingDir = System.getProperty("user.dir");
    String basePath = workingDir.substring(0, workingDir.lastIndexOf(FILE_SEPARATOR));
    String appName = workingDir.substring(workingDir.lastIndexOf(FILE_SEPARATOR) + 1);
    Path directoryPath = Paths.get(basePath + FILE_SEPARATOR + "public" + FILE_SEPARATOR + "swagger" + FILE_SEPARATOR);

    FileWriter fileWriter = null;
    try {
      fileWriter = new FileWriter(directoryPath + FILE_SEPARATOR + appName + ".yaml");
    } catch (IOException e) {
      log.error("Swagger writer exception: {}", e);
    }
    PrintWriter printWriter = new PrintWriter(fileWriter);
    printWriter.println(SwaggerUtil.beautifySwagger(body));
    printWriter.close();
  }
}
