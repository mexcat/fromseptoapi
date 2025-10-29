package cl.getnet.jsontoapi.audit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AuditLogger {

  private static final Logger AUDIT = LoggerFactory.getLogger("cl.getnet.jsontoapi.audit");
  private final ObjectMapper mapper;

  public AuditLogger(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  private String pretty(JsonNode n) {
    try {
      if (n == null) return "null";
      ObjectWriter w = mapper.writerWithDefaultPrettyPrinter();
      return w.writeValueAsString(n);
    } catch (Exception e) {
      return String.valueOf(n);
    }
  }

  public void logAndClear(AuditModels.AuditRecord rec) {
    try {
      boolean stepLines = Boolean.parseBoolean(System.getenv("AUDIT_STEP_LINES"));

      if (stepLines) {
        AUDIT.info("[{}] INITIAL @{} \n{}", rec.correlationId, rec.ts, pretty(rec.initial));

        int i = 1;
        for (AuditModels.ApiCall c : rec.calls) {
          String header = String.format("[%s] CALL %d %s %s (status=%d, %dms)",
              rec.correlationId, i++, c.method, c.url, c.status);
          String req = (c.requestBody != null) ? "\nrequest:\n" + pretty(c.requestBody) : "";
          String res = (c.responseBody != null) ? "\nresponse:\n" + pretty(c.responseBody) : "";
          AUDIT.info("{}{}{}", header, req, res);
        }

        AUDIT.info("[{}] FINAL status={} error={}", rec.correlationId, rec.finalStatus, rec.finalError);

      } else {
        ObjectWriter w = mapper.writerWithDefaultPrettyPrinter();
        AUDIT.info(w.writeValueAsString(rec));
      }
    } catch (Exception ignore) {
    } finally {
      AuditContext.clear();
    }
  }
}
