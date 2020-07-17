package com.dt.europe.hal.api.common.repository;

import com.dt.europe.hal.api.common.model.RepositoryContextEnum;
import com.dt.europe.hal.api.common.utils.FileReaderUtil;
import java.io.File;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

@Repository
@Slf4j
@SuppressWarnings("Duplicates")
public class FileRepository {

  private String findByPath(String path) {
    Assert.notNull(path, "Path is null");

    String jsonFile;
    try {
      jsonFile = FileReaderUtil.getResource(path);
    } catch (IOException e) {
      log.error("File read exception", e);
      throw new RuntimeException(e);
    }
    return jsonFile;
  }

  public String findExampleByPath(String path, RepositoryContextEnum context) {
    switch (context) {

      case REQUEST:
      case RESPONSE:
        path = "entities" + File.separator + path;
        break;
      case TEMPLATE:
        path = "templates" + File.separator + path;
        break;
    }
    return this.findByPath(path);
  }

  public byte[] findObjectByName(String name) throws IOException {
    Assert.notNull(name, "Name is null");

   return FileReaderUtil.getObject(name);
  }

}
