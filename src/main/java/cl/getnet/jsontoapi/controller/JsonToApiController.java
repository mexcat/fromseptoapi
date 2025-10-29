package cl.getnet.jsontoapi.controller;

import cl.getnet.jsontoapi.audit.AuditContext;
import cl.getnet.jsontoapi.audit.AuditLogger;
import cl.getnet.jsontoapi.audit.AuditModels;
import cl.getnet.jsontoapi.dto.StandardResponse;
import cl.getnet.jsontoapi.model.JsonData;
import cl.getnet.jsontoapi.service.JsonOrchestratorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class JsonToApiController {

  private static final Logger log = LoggerFactory.getLogger(JsonToApiController.class);

  private final JsonOrchestratorService orchestrator;
  private final ObjectMapper mapper;
  private final AuditLogger auditLogger;

  public JsonToApiController(
      JsonOrchestratorService orchestrator,
      ObjectMapper mapper,
      AuditLogger auditLogger
  ) {
    this.orchestrator = orchestrator;
    this.mapper = mapper;
    this.auditLogger = auditLogger;
  }

  @PostMapping("/jsonToApi")
  public ResponseEntity<StandardResponse> jsonToApi(@RequestBody JsonData body) {
    String corr = java.util.UUID.randomUUID().toString();

    AuditModels.AuditRecord rec = new AuditModels.AuditRecord();
    rec.ts = java.time.OffsetDateTime.now().toString();
    rec.correlationId = corr;
    rec.initial = mapper.valueToTree(body);
    AuditContext.set(rec);

    try {
      var result = orchestrator.run(body);

      rec.finalStatus = result.isOk() ? "success" : "fail";
      rec.finalError  = result.getMessage() == null ? "" : result.getMessage();
      auditLogger.logAndClear(rec);

      return ResponseEntity.ok(
          result.isOk()
              ? StandardResponse.success()
              : StandardResponse.fail(result.getMessage())
      );
    } catch (Throwable t) {
      log.error("Error inesperado en /jsonToApi: {}", t.getMessage(), t);
      rec.finalStatus = "fail";
      rec.finalError  = "error inesperado: " + t.getMessage();
      auditLogger.logAndClear(rec);

      return ResponseEntity.ok(StandardResponse.fail(rec.finalError));
    }
  }
}
