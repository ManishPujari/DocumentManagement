package com.dt.europe.hal.api.documentmanagement.model;

import com.dt.europe.hal.api.common.model.BaseTemplate;
import com.dt.europe.hal.api.common.model.tmf.TimePeriod;
import com.dt.europe.hal.api.documentmanagement.model.enums.SizeUnitEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@ApiModel(description = "Used to provide info on Attachment.")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
public class Attachment extends BaseTemplate {

  @JsonProperty("id")
  @ApiModelProperty(value = "Id of the attachment")
  private String id;

  @JsonProperty("href")
  @ApiModelProperty(value = "A valid link to the attachment", position = 1)
  private String href;

  @JsonProperty("name")
  @ApiModelProperty(value = "A name of the attachment", position = 2)
  private String name;

  @JsonProperty("description")
  @ApiModelProperty(value = "Description of the attachment", position = 3)
  private String description;

  @JsonProperty("type")
  @ApiModelProperty(value = "Identifies the sub-type of the instance of attachment", position = 4)
  private String type;

  @JsonProperty("mimeType")
  @ApiModelProperty(value = "Indicates attachment mime type  "
    + "\n* list of possible mime types https://www.freeformatter.com/mime-types-list.html", position = 5)
  private String mimeType;

  @JsonProperty("size")
  @ApiModelProperty(value = "The size in sizeUnits of the document or attachment. If this component "
    + "contains the embedded data then the size is the size of the embedded data; if it is a reference "
    + "without the data then it is the size of the referenced document", position = 6)
  private BigDecimal size;

  @JsonProperty("sizeUnit")
  @ApiModelProperty(value = "Indicates size unit  "
    + "\n* `bytes` Indicates bytes as size unit  ", position = 7)
  private SizeUnitEnum sizeUnit;

  @JsonProperty("url")
  @ApiModelProperty(value = "URL of the document attachment", position = 8)
  private String url;

  @JsonProperty("validFor")
  @ApiModelProperty(value = "The time period this attachment is valid for", position = 9)
  private TimePeriod validFor;

  @ApiModelProperty(value = "Base64 encoded content of the attached file", position = 11)
  @JsonProperty("encodedContent")
  private String encodedContent;

  @Override
  public void validateInit() {

  }
}
