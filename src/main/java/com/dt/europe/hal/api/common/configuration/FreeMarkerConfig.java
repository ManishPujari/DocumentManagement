package com.dt.europe.hal.api.common.configuration;

import com.dt.europe.hal.api.common.utils.Generator;
import freemarker.template.Configuration;
import java.io.File;
import java.io.IOException;
import org.springframework.context.annotation.Bean;

//@org.springframework.context.annotation.Configuration
public class FreeMarkerConfig {


  @Bean
  public Configuration freeMarkerConfiguration() throws IOException {

   Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
    String workingFolder = FreeMarkerConfig.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    workingFolder = Generator.fixWorkingFolder(workingFolder);
    
    workingFolder = workingFolder.substring(0, workingFolder.indexOf(File.separator + "common" + File.separator));
    workingFolder = workingFolder + File.separator + "common" + File.separator + "src" + File.separator + "main" + File.separator + "resources"
      + File.separator + "templates";
    
    cfg.setDirectoryForTemplateLoading(new File(workingFolder));
    cfg.setDefaultEncoding("UTF-8");
	  
	 
    return cfg;
  }

}
