package com.dt.europe.hal.api.documentmanagement.model;

import com.dt.europe.hal.api.common.model.BaseTemplate;
import com.dt.europe.hal.api.common.model.tmf.Characteristic;
import com.dt.europe.hal.api.documentmanagement.model.enums.DocumentTypeEnum;
import com.dt.europe.hal.api.documentmanagement.model.enums.LifecycleStateEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@ApiModel(description = "Document is a tangible output from an activity.")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
public class Document extends BaseTemplate {

  @JsonProperty("id")
  @ApiModelProperty(
    value =
      "Identifier of an instance of the document. Required to be unique within the "
        + "document type. Used in URIs as the identifier for specific instances of a type")
  private String id;

  @JsonProperty("href")
  @ApiModelProperty(value = "The URI for the object itself", position = 1)
  private String href;

  @JsonProperty("documentType")
  @ApiModelProperty(
    value =
      "Indicates document type  "
        + "\n* `invoice` Indicates invoice document  "
        + "\n* `contract` Indicates contract document  "
        + "\n* `other` Indicates other type of document  ",
    position = 2)
  private DocumentTypeEnum documentType;

  @JsonProperty("lifecycleState")
  @ApiModelProperty(
    value =
      "Indicates lifecycle status  "
        + "\n* `draft` Draft document  "
        + "\n* `inProgress` Document in progress  "
        + "\n* `final` Final document  ",
    position = 3)
  private LifecycleStateEnum lifecycleState;

  @JsonProperty("version")
  @ApiModelProperty(value = "Version of the document", position = 4)
  private String version;

  @JsonProperty("name")
  @ApiModelProperty(value = "Name of the document", position = 5)
  private String name;

  @JsonProperty("creationDate")
  @ApiModelProperty(value = "Date and time the document was created", position = 6)
  private OffsetDateTime creationDate;

  @JsonProperty("description")
  @ApiModelProperty(value = "Description of the document", position = 7)
  private String description;

  @JsonProperty("lastUpdate")
  @ApiModelProperty(value = "Date and time the document was last updated", position = 8)
  private OffsetDateTime lastUpdate;

  @JsonProperty("attachments")
  @ApiModelProperty(value = "Attachments to the document", position = 9)
  private List<Attachment> attachments;

  @JsonProperty("documentCharacteristics")
  @ApiModelProperty(value = "Characteristics of the document", position = 10)
  private List<Characteristic> characteristics;

  @JsonProperty("documentSpecifications")
  @ApiModelProperty(value = "Specifications of the document", position = 11)
  private List<DocumentSpecification> documentSpecifications;

  @JsonProperty("documentRelationships")
  @ApiModelProperty(value = "Relationship to the documents", position = 12)
  private List<DocumentRelationship> documentRelationships;

  @JsonProperty("relatedObject")
  @ApiModelProperty(value = "Related object refs", position = 13)
  private RelatedObject relatedObject;

  @JsonProperty("relatedParty")
  @ApiModelProperty(value = "Related party refs", position = 14)
  private RelatedPartyRef relatedParty;


  @JsonProperty("entityType")
  @ApiModelProperty(value = "Class type of resource", position = 15)
  private String entityType;

  @Override
  public void validateInit() {

  }
}
