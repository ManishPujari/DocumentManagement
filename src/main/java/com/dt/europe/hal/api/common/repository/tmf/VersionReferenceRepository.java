package com.dt.europe.hal.api.common.repository.tmf;

import static java.util.stream.Collectors.toList;

import com.dt.europe.hal.api.common.model.tmf.VersionReference;
import com.dt.europe.hal.api.common.utils.FileReaderUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

@Repository
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SuppressWarnings("Duplicates")
public class VersionReferenceRepository {

  private final ObjectMapper objectMapper;

  public VersionReference findByFileId(String fileId) {
    Assert.notNull(fileId, "FileId for is null");

    VersionReference entity;
    String jsonFile;
    try {
      jsonFile = FileReaderUtil.getResource(fileId);

      ObjectReader objectReader = objectMapper.reader()
                                              .forType(new TypeReference<VersionReference>() {});
      entity = objectReader.readValue(jsonFile);
    } catch (IOException e) {
      log.error("Mapping exception", e);
      throw new RuntimeException(e);
    }
    return entity;
  }

  public List<VersionReference> getFiles(List<String> fileIds) {
    Assert.isTrue(!CollectionUtils.isEmpty(fileIds), "List of fileIds is empty");

    return fileIds.stream()
                  .map(this::findByFileId)
                  .collect(toList());
  }

}
