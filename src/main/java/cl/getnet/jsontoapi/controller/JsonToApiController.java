package cl.getnet.jsontoapi.controller;

import cl.getnet.jsontoapi.dto.StandardResponse;
import cl.getnet.jsontoapi.model.JsonData;
import cl.getnet.jsontoapi.service.JsonOrchestratorService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class JsonToApiController {
  private static final Logger log = LoggerFactory.getLogger(JsonToApiController.class);
  private final JsonOrchestratorService service;

  public JsonToApiController(JsonOrchestratorService service) {
    this.service = service;
  }

  @PostMapping(value = "/jsonToApi", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public StandardResponse jsonToApi(@RequestBody @Valid JsonData json) {
    try {
      var result = service.run(json);
      if (result.isOk()) {
        return StandardResponse.success();
      } else {
        return StandardResponse.fail(result.getMessage());
      }
    } catch (Exception e) {
      log.error("Error en /jsonToApi: {}", e.toString());
      return StandardResponse.fail("error inesperado: " + e.getMessage());
    }
  }
}
