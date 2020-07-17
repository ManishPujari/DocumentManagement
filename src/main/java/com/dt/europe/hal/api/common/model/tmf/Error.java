package com.dt.europe.hal.api.common.model.tmf;

import com.dt.europe.hal.api.common.model.tmf.enums.SeverityEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@ApiModel(description = "Default error message template")
@AllArgsConstructor
@Data
@RequiredArgsConstructor
public class Error {

  @ApiModelProperty(required = true, value = "Unique error ID, used for logging purposes, UUID format", position = 0)
  @NotNull
  @JsonProperty("id")
  private String id = null;

  @ApiModelProperty(hidden = true)
  private HttpStatus httpStatus;

  @ApiModelProperty(required = true, value = "A string coding the error type. This is given to caller so he can translate them if required.",
    position = 1)
  @NotNull
  @JsonProperty("code")
  private String code = null;

  @ApiModelProperty(required = true, value = "Exact time of error", position = 2)
  @NotNull
  @Valid
  @JsonProperty("timestamp")
  private OffsetDateTime timestamp = null;

  @ApiModelProperty(value = "A short localized string that describes the error.", position = 3)
  @JsonProperty("message")
  private String message = null;

  @ApiModelProperty(value = "A boolean that provides info is request retryable", position = 4)
  @JsonProperty("retryable")
  private Boolean retryable = null;

  @ApiModelProperty(value = "Describes severity of error. Order from most to least critical is: FATAL, ERROR, WARN, INFO, DEBUG, TRACE"
    + "\n* `FATAL` Designates very severe error events that will presumably lead the application to abort.  "
    + "\n* `ERROR` Designates error events that might still allow the application to continue running.  "
    + "\n* `WARN` Designates potentially harmful situations.  "
    + "\n* `INFO` Designates informational messages that highlight the progress of the application at coarse-grained level.  "
    + "\n* `DEBUG` Designates fine-grained informational events that are most useful to debug an application.  "
    + "\n* `TRACE` Designates finer-grained informational events than the DEBUG. ", position = 5)
  @JsonProperty("severity")
  @NotNull
  private SeverityEnum severity = null;

  @ApiModelProperty(value = "Exception detailed info", position = 6)
  @JsonProperty("details")
  @Valid
  private List<Object> details = null;
}
