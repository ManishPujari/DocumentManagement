package com.dt.europe.hal.api.common.configuration;


import com.dt.europe.hal.api.common.exceptions.ExampleException;
import com.dt.europe.hal.api.common.model.tmf.Error;
import com.dt.europe.hal.api.common.model.tmf.enums.SeverityEnum;
import com.dt.europe.hal.api.common.service.ErrorService;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

//@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RestExceptionHandler extends ResponseEntityExceptionHandler {


  private final ErrorService errorService;

  private ResponseEntity<Object> buildResponseEntity(Error error) {
    HttpStatus httpStatus = error.getHttpStatus();
    error.setHttpStatus(null);
    return new ResponseEntity<>(error, httpStatus);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                HttpHeaders headers,
                                                                HttpStatus status,
                                                                WebRequest request) {
    List<Object> details = Collections.singletonList(ex.getBindingResult()
                                                       .getAllErrors());
    return buildResponseEntity(new Error("",
                                         HttpStatus.BAD_REQUEST,
                                         "BAD_REQUEST",
                                         OffsetDateTime.now(),
                                         ex.getMessage(),
                                         false,
                                         SeverityEnum.FATAL,
                                         details));
  }

  @ExceptionHandler(ExampleException.class)
  protected ResponseEntity<Object> handleExampleException(ExampleException ex) {

    return buildResponseEntity(errorService.getErrorOnExample(ex.getMessage()));
  }


}
