package com.dt.europe.hal.api.documentmanagement;

import java.net.URI;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import com.dt.europe.hal.api.common.repository.FileRepository;
import com.dt.europe.hal.api.common.repository.ResponseParamsRepository;
import com.dt.europe.hal.api.common.service.ErrorService;
import com.dt.europe.hal.api.common.service.HeaderService;
import com.dt.europe.hal.api.common.validator.ExampleValidatorService;
import com.dt.europe.hal.api.documentmanagement.controller.DocumentApiController;
import com.dt.europe.hal.api.documentmanagement.model.Document;
import com.dt.europe.hal.api.documentmanagement.service.DocumentService;
import com.dt.europe.hal.api.documentmanagement.validator.DocumentValidatorService;

import junit.framework.Assert;

@SpringBootTest(classes=DocumentManagementApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class DocumentManagementControllerTest {
	
	//@LocalServerPort
	//private int port;
	
	public MockMvc mvc;
	   @Autowired
	   WebApplicationContext webApplicationContext;

	   @Before
	   public void setUp() {
	      mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	   }
	
	@MockBean
	 private  ResponseParamsRepository responseParamsRepository;
	@MockBean
	  private  FileRepository fileRepository;
	@MockBean
	  private  ErrorService errorService;
	@MockBean
	  private  HeaderService headerService;
	@MockBean
	  private  ExampleValidatorService exampleValidatorService;
	@MockBean
	  private  DocumentService documentService;
	@MockBean
	  private  DocumentValidatorService documentValidatorService;
	
	@Test
	public void getDocumentsTest_404() throws Exception{
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("X-Postman-Id", "DM-5");
		
		
			String url=	"http://localhost:8004/documentManagement/v1/documents?relatedObject.id=19892b9e-a181-4a07-917c-1ad6932b5797&relatedObject.entityType=product";
			
				
		MvcResult res=
				mvc.perform(MockMvcRequestBuilders.get(URI.create(url)).contentType(MediaType.APPLICATION_JSON).header("X-Postman-Id", "DM-5")).andReturn();
		
		Assert.assertEquals(404,res.getResponse().getStatus());
		
		
	}
	
	@Test
	public void getDocumentsTest() throws Exception{
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("X-Postman-Id", "DM-5");
		
		
			String url=	"/documentManagement/v1/documents?relatedObject.id=19892b9e-a181-4a07-917c-1ad6932b5797&relatedObject.entityType=product";
			
				
		MvcResult res=
				mvc.perform(MockMvcRequestBuilders.get(URI.create(url)).contentType(MediaType.APPLICATION_JSON).header("X-Postman-Id", "DM-5")).andReturn();
		
		Assert.assertEquals(404,res.getResponse().getStatus());
		
		
	}

}
