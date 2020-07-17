package com.dt.europe.hal.api.documentmanagement.configuration;

import static com.google.common.collect.Lists.newArrayList;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.service.StringVendorExtension;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

  public final static String TAG_DOCUMENT = "Document";
  public final static String TAG_ATTACHMENT = "Attachment";

  @Value("${swagger.version}")
  private String version;

  @Value("${swagger.schemes}")
  private String schemes;

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
      .title("Document Management API")
      .description(
        "Document Management API is used for Document fetching from a Document Management system. "
          + "The API will be used for fetching PDF bills or any other document on customer's request.")
      .termsOfServiceUrl("")
      .license("Proprietary license")
      .licenseUrl("")
      .version(version)
      .contact(new Contact("HAL Team", "", "HAL.Team@t.ht.hr"))
      .build();
  }

  private SecurityScheme token() {
    StringVendorExtension ve = new StringVendorExtension("x-description",
      "Access token in JWT or opaque format sent in HTTP Authorization header, Bearer schema");
    return new ApiKey("accessToken", "Authorization", "header", newArrayList(ve));
  }

  private SecurityScheme apiKey() {
    StringVendorExtension ve = new StringVendorExtension("x-description",
      "API key for technical accounts (opaque format) sent in HTTP Authorization header, no "
        + "schema");
    return new ApiKey("apiKey", "Authorization", "header", newArrayList(ve));
  }

  @Bean
  public Docket apiConfig() {

    return new Docket(DocumentationType.SWAGGER_2)
      .apiInfo(apiInfo())
      .useDefaultResponseMessages(false)
      .consumes(Sets.newHashSet("application/json"))
      .produces(Sets.newHashSet("application/json"))
      .protocols(Sets.newHashSet(schemes.split(",")))
      .securitySchemes(newArrayList(apiKey(), token()))
      .ignoredParameterTypes(ResponseEntity.class)
      .tags(new Tag(TAG_DOCUMENT, "", 0), new Tag(TAG_ATTACHMENT, "", 1))
      .select()
      .apis(
        RequestHandlerSelectors.basePackage(
          "com.dt.europe.hal.api.documentmanagement.controller"))
      .paths(PathSelectors.any())
      .build();
  }

}
