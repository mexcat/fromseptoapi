package cl.getnet.jsontoapi.audit;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;

public class AuditModels {

  public static class ApiCall {
    public String  method;
    public String  url;
    public JsonNode requestBody;
    public int     status;
    public JsonNode responseBody;
  }

  public static class AuditRecord {
    public String ts;
    public String correlationId;
    public JsonNode initial;
    public List<ApiCall> calls = new ArrayList<>();
    public String finalStatus;
    public String finalError;
  }
}
