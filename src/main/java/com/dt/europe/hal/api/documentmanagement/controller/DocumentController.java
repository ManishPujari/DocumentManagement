package com.dt.europe.hal.api.documentmanagement.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dt.europe.hal.api.common.exceptions.ExampleException;
import com.dt.europe.hal.api.common.exceptions.InputParamInvalidException;
import com.dt.europe.hal.api.common.repository.FileRepository;
import com.dt.europe.hal.api.common.repository.ResponseParamsRepository;
import com.dt.europe.hal.api.common.service.ErrorService;
import com.dt.europe.hal.api.common.service.HeaderService;
import com.dt.europe.hal.api.common.validator.ExampleValidatorService;
import com.dt.europe.hal.api.documentmanagement.model.Document;
import com.dt.europe.hal.api.documentmanagement.service.DocumentService;
import com.dt.europe.hal.api.documentmanagement.validator.DocumentValidatorService;

import lombok.RequiredArgsConstructor;

@RestController
public class DocumentController /*extends DocumentApiController*/ {


	/*public DocumentController(ResponseParamsRepository responseParamsRepository, FileRepository fileRepository,
			ErrorService errorService, HeaderService headerService, ExampleValidatorService exampleValidatorService,
			DocumentService documentService, DocumentValidatorService documentValidatorService) {
		super(responseParamsRepository, fileRepository, errorService, headerService, exampleValidatorService, documentService,
				documentValidatorService);
		// TODO Auto-generated constructor stub
	}
	*/
	
	//@Override
	
	public ResponseEntity<Document> getDocument(String documentId, String exampleId)
			throws ExampleException, InputParamInvalidException, IOException {
		
		return new ResponseEntity<Document>(new Document(), HttpStatus.OK);
	}
	
	
	
}
