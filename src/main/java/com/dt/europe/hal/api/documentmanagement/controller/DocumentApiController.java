package com.dt.europe.hal.api.documentmanagement.controller;

import com.dt.europe.hal.api.common.exceptions.ExampleException;
import com.dt.europe.hal.api.common.exceptions.InputParamInvalidException;
import com.dt.europe.hal.api.common.model.RequestParams;
import com.dt.europe.hal.api.common.model.ResponseParams;
import com.dt.europe.hal.api.common.model.ValidatorContexEnum;
import com.dt.europe.hal.api.common.model.tmf.Characteristic;
import com.dt.europe.hal.api.common.model.tmf.Error;
import com.dt.europe.hal.api.common.repository.FileRepository;
import com.dt.europe.hal.api.common.repository.ResponseParamsRepository;
import com.dt.europe.hal.api.common.service.ErrorService;
import com.dt.europe.hal.api.common.service.HeaderService;
import com.dt.europe.hal.api.common.templates.HalMessages;
import com.dt.europe.hal.api.common.validator.ExampleValidatorService;
import com.dt.europe.hal.api.documentmanagement.configuration.SwaggerConfig;
import com.dt.europe.hal.api.documentmanagement.model.Attachment;
import com.dt.europe.hal.api.documentmanagement.model.Document;
import com.dt.europe.hal.api.documentmanagement.service.DocumentService;
import com.dt.europe.hal.api.documentmanagement.validator.DocumentValidatorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.io.IOException;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("documents")
@Api(tags = "Document", description = "API over Document domain")
@ApiResponses(
  value = {
    @ApiResponse(code = 206, message = HalMessages.HTTP_206, response = Error.class),
    @ApiResponse(code = 207, message = HalMessages.HTTP_207, response = Error.class),
    @ApiResponse(code = 400, message = HalMessages.HTTP_400, response = Error.class),
    @ApiResponse(code = 401, message = HalMessages.HTTP_401, response = Error.class),
    @ApiResponse(code = 403, message = HalMessages.HTTP_403, response = Error.class),
    @ApiResponse(code = 404, message = HalMessages.HTTP_404, response = Error.class),
    @ApiResponse(code = 408, message = HalMessages.HTTP_408, response = Error.class),
    @ApiResponse(code = 409, message = HalMessages.HTTP_409, response = Error.class),
    @ApiResponse(code = 422, message = HalMessages.HTTP_422, response = Error.class),
    @ApiResponse(code = 429, message = HalMessages.HTTP_429, response = Error.class),
    @ApiResponse(code = 500, message = HalMessages.HTTP_500, response = Error.class),
    @ApiResponse(code = 504, message = HalMessages.HTTP_504, response = Error.class)
  })
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DocumentApiController {

  private static final String TAG = SwaggerConfig.TAG_DOCUMENT;

  private final ResponseParamsRepository responseParamsRepository;
  private final FileRepository fileRepository;
  private final ErrorService errorService;
  private final HeaderService headerService;
  private final ExampleValidatorService exampleValidatorService;
  private final DocumentService documentService;
  private final DocumentValidatorService documentValidatorService;


  /*
   * GET /documentManagement/v1//documents
   * */
  @ApiOperation(
    value = "Retrieve a documents",
    nickname = "getDocuments",
    notes = "Retrieve a documents",
    response = Document.class,
    responseContainer = "List",
    tags = {TAG})
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Ok", response = Document.class, responseContainer = "List")})
  @GetMapping(value = "")
  public ResponseEntity<List<Document>> getDocuments(
    @ApiParam(value = "Related Party ID") @RequestParam(value = "relatedParty.id", required = false) String relatedPartyId,
    @ApiParam(value = "Related Party role") @RequestParam(value = "relatedParty.role", required = false) String relatedPartyRole,
    @ApiParam(value = "Related object ID") @RequestParam(value = "relatedObject.id", required = false) String relatedObjectId,
    @ApiParam(value = "Related object entity type") @RequestParam(value = "relatedObject.entityType", required = false) String relatedObjectEntityType,
    @ApiParam(value = "Document Type") @RequestParam(value = "documentType", required = false) String documentType,
    @ApiParam(value = HalMessages.PARAM_FIELDS) @Valid @RequestParam(value = "fields", required = false) String fields,
    @ApiParam(value = HalMessages.PARAM_QUERY) @Valid @RequestParam(value = "query", required = false) String query,
    @ApiParam(value = HalMessages.PARAM_PAGE) @Valid @RequestParam(value = "page", required = false) String page,
    @ApiParam(value = HalMessages.PARAM_SIZE) @Valid @RequestParam(value = "size", required = false) String size,
    @ApiParam(value = HalMessages.PARAM_SORT) @Valid @RequestParam(value = "sort", required = false) String sort,
    @ApiParam(hidden = true) @RequestHeader(name = HalMessages.POSTMAN_HEADER) String exampleId) throws ExampleException, InputParamInvalidException {
    {
      RequestParams parsedParams = new RequestParams();
      if (fields != null && !fields.isEmpty()) {
        parsedParams.getQueryParams()
          .add(Characteristic.builder()
            .name("fields")
            .value(fields)
            .build());
      }

      if (query != null && !query.isEmpty()) {
        parsedParams.getQueryParams()
          .add(Characteristic.builder()
            .name("query")
            .value(query)
            .build());
      }
      if (page != null && !page.isEmpty()) {
        parsedParams.getQueryParams()
          .add(Characteristic.builder()
            .name("page")
            .value(page)
            .build());
      }
      if (size != null && !size.isEmpty()) {
        parsedParams.getQueryParams()
          .add(Characteristic.builder()
            .name("size")
            .value(size)
            .build());
      }
      if (sort != null && !sort.isEmpty()) {
        parsedParams.getQueryParams()
          .add(Characteristic.builder()
            .name("sort")
            .value(sort)
            .build());
      }

      if (relatedPartyId != null && !relatedPartyId.isEmpty()) {
        parsedParams.getQueryParams()
          .add(Characteristic.builder()
            .name("relatedParty.id")
            .value(relatedPartyId)
            .build());
      }
      if (relatedPartyRole != null && !relatedPartyRole.isEmpty()) {
        parsedParams.getQueryParams()
          .add(Characteristic.builder()
            .name("relatedParty.role")
            .value(relatedPartyRole)
            .build());
      }
      if (relatedObjectId != null && !relatedObjectId.isEmpty()) {
        parsedParams.getQueryParams()
          .add(Characteristic.builder()
            .name("relatedObject.id")
            .value(relatedObjectId)
            .build());
      }
      if (relatedObjectEntityType != null && !relatedObjectEntityType.isEmpty()) {
        parsedParams.getQueryParams()
          .add(Characteristic.builder()
            .name("relatedObject.entityType")
            .value(relatedObjectEntityType)
            .build());
      }
      if (documentType != null && !documentType.isEmpty()) {
        parsedParams.getQueryParams()
          .add(Characteristic.builder()
            .name("documentType")
            .value(documentType)
            .build());
      }

      this.exampleValidatorService.validateGetParams(exampleId, parsedParams);
    }

    this.errorService.hasException(exampleId);
    ResponseParams responseParams = responseParamsRepository.findByExampleId(exampleId);
    List<Document> response = this.documentService.getResponseList(responseParams.getBodyRefs());

    documentValidatorService.validateAll(response, ValidatorContexEnum.RESPONSE);
    HttpHeaders httpHeaders = headerService.getResponseHeaders(exampleId);
    return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
  }

  /*
   * GET /documentManagement/v1/documents/{documentId}/attachments/{attachmentId}
   * */
  @ApiOperation(
    value = "Retrieve a document in PDF format",
    nickname = "getPdfFile",
    //notes = "",
    response = Object.class,
    tags = {
      TAG
    })
  @ApiResponses(
    value = {
      @ApiResponse(
        code = 200,
        message = "Document with the specified id",
        response = Object.class)
    })
  @GetMapping(value = "/{documentId}/attachments/{attachmentId}", produces = {"application/pdf"})
  public ResponseEntity<byte[]> getDocumentPdfFile(
    @ApiParam(value = "Document ID", required = true) @PathVariable("documentId") String documentId,
    @ApiParam(value = "Attachment ID", required = true) @PathVariable("attachmentId") String attachmentId,
    @ApiParam(hidden = true) @RequestHeader(name = HalMessages.POSTMAN_HEADER) String exampleId)
    throws ExampleException, InputParamInvalidException, IOException {
    {
      RequestParams parsedParams = new RequestParams();
      if (documentId != null && !documentId.isEmpty()) {
        parsedParams.getPathParams()
          .add(Characteristic.builder()
            .name("documentId")
            .value(documentId)
            .build());
      }

      if (attachmentId != null && !attachmentId.isEmpty()) {
        parsedParams.getPathParams()
          .add(Characteristic.builder()
            .name("attachmentId")
            .value(attachmentId)
            .build());
      }

      this.exampleValidatorService.validateGetParams(exampleId, parsedParams);
    }

    this.errorService.hasException(exampleId);
    ResponseParams responseParams = responseParamsRepository.findByExampleId(exampleId);
    byte[] response = fileRepository.findObjectByName(responseParams.getObjectRef());

    HttpHeaders httpHeaders = headerService.getResponseHeaders(exampleId);
    return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
  }

  @ApiOperation(
    value = "Retrieve a document meta data",
    nickname = "getDocMetaData",
    hidden = true,
    //notes = "",
    response = Document.class)
  @ApiResponses(
    value = {
      @ApiResponse(
        code = 200,
        message = "Document with the specified id",
        response = Document.class)
    })
  @GetMapping(value = "{documentId}")
  public ResponseEntity<Document> getDocument(
    @ApiParam(value = "Document ID", required = true) @PathVariable("documentId")
      String documentId,
    @ApiParam(hidden = true) @RequestHeader(name = HalMessages.POSTMAN_HEADER) String exampleId)
    throws ExampleException, InputParamInvalidException, IOException {
    {
      RequestParams parsedParams = new RequestParams();
      if (documentId != null && !documentId.isEmpty()) {
        parsedParams.getPathParams()
          .add(Characteristic.builder()
            .name("documentId")
            .value(documentId)
            .build());
      }

      this.exampleValidatorService.validateGetParams(exampleId, parsedParams);
    }

    this.errorService.hasException(exampleId);
    ResponseParams responseParams = responseParamsRepository.findByExampleId(exampleId);
    Document response = this.documentService.getResponse(responseParams.getBodyRefs());

    documentValidatorService.validate(response, ValidatorContexEnum.RESPONSE);
    HttpHeaders httpHeaders = headerService.getResponseHeaders(exampleId);
    return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
  }

  /*
   * POST /documentManagement/v1/documents/{documentId}/attachments
   * */
  @ApiOperation(value = "Upload an attachment in PDF format", nickname = "savePdfFile",
    //notes = "",
    consumes = "multipart/form-data", response = Attachment.class, tags = {TAG})
  @ApiResponses(value = {@ApiResponse(code = 201, message = "Attachment with the specified id", response = Attachment.class)})
  @PostMapping(value = "/{documentId}/attachments", consumes = {"multipart/form-data"})
  public ResponseEntity<Attachment> saveDocumentPdfFile(
    @ApiParam(value = "Document ID", required = true) @PathVariable("documentId") String documentId, @RequestParam("file") MultipartFile uploadedFile,
    @ApiParam(hidden = true) @RequestHeader(name = HalMessages.POSTMAN_HEADER) String exampleId)
    throws ExampleException, InputParamInvalidException, IOException {

    return new ResponseEntity<>(new Attachment(), null, HttpStatus.NOT_IMPLEMENTED);

  }

  /*
   * POST /documentManagement/v1/documents
   */
  @ApiOperation(value = "Creates a document", nickname = "createDocument", notes = "", response = Document.class, tags = {TAG})
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Created", response = Document.class)})
  @PostMapping(value = "")
  public ResponseEntity<Document> createDocument(
    @ApiParam(value = "The document to be created", required = true) @Valid @RequestBody Document body,
    @ApiParam(hidden = true) @RequestHeader(name = HalMessages.POSTMAN_HEADER) String exampleId) throws ExampleException, InputParamInvalidException {

    documentValidatorService.validate(body, ValidatorContexEnum.POST_REQUEST);

    this.errorService.hasException(exampleId);
    ResponseParams responseParams = responseParamsRepository.findByExampleId(exampleId);
    Document response = this.documentService.getResponse(responseParams.getBodyRefs());
    documentValidatorService.validate(response, ValidatorContexEnum.RESPONSE);
    HttpHeaders httpHeaders = headerService.getResponseHeaders(exampleId);
    return new ResponseEntity<>(response, httpHeaders, HttpStatus.CREATED);
  }

  /*
   * PATCH /documentManagement/v1/documents
   */
  @ApiOperation(value = "Updates a document", nickname = "patchDocument", notes = "", response = Document.class, tags = {TAG})
  @ApiResponses(value = {@ApiResponse(code = 200, message = "updated", response = Document.class)})
  @PatchMapping(value = "{id}")
  public ResponseEntity<Document> updateDocument(
    @ApiParam(value = "", required = true) @Valid @RequestBody Document body,
    @ApiParam(value = "Document ID", required = true) @PathVariable("id") String id,
    @ApiParam(value = HalMessages.PARAM_PATCH_FIELDS, required = true) @Valid @RequestParam(value = "fields", required = true) String fields,
    @ApiParam(hidden = true) @RequestHeader(name = HalMessages.POSTMAN_HEADER) String exampleId) throws ExampleException, InputParamInvalidException {

    {
      RequestParams parsedParams = new RequestParams();

      if (id != null && !id.isEmpty()) {
        parsedParams.getPathParams()
          .add(Characteristic.builder().name("id").value(id).build());
      }

      if (fields != null && !fields.isEmpty()) {
        parsedParams.getQueryParams()
          .add(Characteristic.builder().name("fields").value(fields).build());
      }

      this.exampleValidatorService.validateGetParams(exampleId, parsedParams);
    }

    this.errorService.hasException(exampleId);
    ResponseParams responseParams = responseParamsRepository.findByExampleId(exampleId);
    Document response = this.documentService.getResponse(responseParams.getBodyRefs());
    documentValidatorService.validate(response, ValidatorContexEnum.RESPONSE);
    HttpHeaders httpHeaders = headerService.getResponseHeaders(exampleId);
    return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
  }
}
