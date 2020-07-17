package com.dt.europe.hal.api.documentmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.dt.europe.hal.api")
public class DocumentManagementApplication {

  public static void main(String[] args) {
    SpringApplication.run(DocumentManagementApplication.class, args);
  }

}
