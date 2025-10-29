package cl.getnet.jsontoapi.service;

import cl.getnet.jsontoapi.client.EstateManagerClient;
import cl.getnet.jsontoapi.config.LocalSettings;
import cl.getnet.jsontoapi.model.JsonData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class JsonOrchestratorService {
  private static final Logger log = LoggerFactory.getLogger(JsonOrchestratorService.class);
  private final LocalSettings settings;
  private final EstateManagerClient client;
  private final ObjectMapper mapper;

  public JsonOrchestratorService(LocalSettings settings, EstateManagerClient client, ObjectMapper mapper) {
    this.settings = settings;
    this.client = client;
    this.mapper = mapper;
  }

  public String normalizeTerminal(String terminal) {
    if (terminal == null) return null;
    terminal = terminal.trim();
    return terminal.endsWith("A") ? terminal : terminal + "A";
  }

  public LocalSettings.ChannelInfo findChannelInfo(Integer channel) {
    if (settings.getChannelData() == null) return null;
    return settings.getChannelData().stream()
        .filter(ci -> ci.getChannel() != null && ci.getChannel().equals(channel))
        .findFirst().orElse(null);
  }

  public OrchestrationResult run(JsonData data) {
    var channelInfo = findChannelInfo(data.getChannel());
    if (channelInfo == null || channelInfo.getFolder() == null || channelInfo.getFolder().isBlank()) {
      return OrchestrationResult.fail("channel no encontrado");
    }

    Optional<JsonNode> folderOpt = client.getFolder(channelInfo.getFolder());
    if (folderOpt.isEmpty()) {
      String detail = client.consumeLastError();
      String msg = "carpeta no encontrada";
      if (detail != null && !detail.isBlank()) msg += " - " + detail;
      return OrchestrationResult.fail(msg);
    }
    JsonNode folder = folderOpt.get();
    String folderId = folder.hasNonNull("id") ? folder.get("id").asText() : null;
    if (folderId == null || folderId.isBlank()) {
      return OrchestrationResult.fail("carpeta no encontrada (sin id)");
    }

    String terminalSignature = normalizeTerminal(data.getTerminal_code());
    Optional<JsonNode> termOpt = client.getTerminalBySignature(terminalSignature);
    String termErr = client.consumeLastError();
    if (termOpt.isEmpty() && termErr != null && !termErr.isBlank()) {
      return OrchestrationResult.fail("error consultando terminal - " + termErr);
    }

    JsonNode terminalNode = null;
    String terminalId = null;

    if (termOpt.isPresent() && termOpt.get() != null && termOpt.get().hasNonNull("id") && termOpt.get().hasNonNull("signature")) {
      terminalNode = termOpt.get();
      terminalId = terminalNode.get("id").asText();

      boolean inFolder = false;
      if (terminalNode.has("parent") && terminalNode.get("parent").hasNonNull("id")) {
        inFolder = folderId.equals(terminalNode.get("parent").get("id").asText());
      } else {
        inFolder = terminalNode.toString().contains(folderId);
      }
      if (!inFolder) {
        boolean moved = client.moveToFolder(terminalId, folderId);
        if (!moved) {
          String detail = client.consumeLastError();
          String msg = "no se pudo mover terminal";
          if (detail != null && !detail.isBlank()) msg += " - " + detail;
          return OrchestrationResult.fail(msg);
        }
      }
    } else {
      Optional<JsonNode> created = client.createTerminal(terminalSignature, folderId);
      String createErr = client.consumeLastError();
      if (created.isEmpty() || created.get() == null || !created.get().hasNonNull("id")) {
        String msg = "no se pudo crear terminal";
        if (createErr != null && !createErr.isBlank()) msg += " - " + createErr;
        return OrchestrationResult.fail(msg);
      }
      terminalNode = created.get();
      terminalId = terminalNode.get("id").asText();
    }

    String template = channelInfo.getTemplate();
    String version = channelInfo.getVersion();
    if (template == null || template.isBlank() || version == null || version.isBlank()) {
      log.warn("Template/version no definidos para channel {}", data.getChannel());
    }

    JsonNode updateBody = buildUpdateBody(data);
    boolean updated = client.updateTerminalParams(terminalId, template, version, updateBody);
    if (!updated) {
      String detail = client.consumeLastError();
      String msg = "no se pudo actualizar parámetros (paso D)";
      if (detail != null && !detail.isBlank()) msg += " - " + detail;
      return OrchestrationResult.fail(msg);
    }

    return OrchestrationResult.success();
  }

  private JsonNode buildUpdateBody(JsonData data) {
    try {
      String path = System.getenv("APP_PARAM_MAPPING");
      if (path == null || path.isBlank()) path = "config/param-mapping.json";
      Map<String, String> mapping = mapper.readValue(
          new java.io.File(path),
          mapper.getTypeFactory().constructMapType(Map.class, String.class, String.class)
      );

      class Spec { String tipo; int largo; Spec(String t, int l){ this.tipo=t; this.largo=l; } }
      java.util.Map<String, Spec> spec = new java.util.HashMap<>();
      spec.put("key_trade", new Spec("num", 6));
      spec.put("min_zero_rate_fees", new Spec("num", 2));
      spec.put("max_zero_rate_fees", new Spec("num", 2));
      spec.put("min_instalments_Rate", new Spec("num", 2));
      spec.put("max_instalments_Rate", new Spec("num", 2));

      java.util.Map<String, String> rename = new java.util.HashMap<>();
      rename.put("credential01", "Credential01");
      rename.put("credential02", "Credential02");
      rename.put("credential03", "Credential03");
      rename.put("credential04", "Credential04");

      ArrayNode root = mapper.createArrayNode();

      java.lang.reflect.Method[] methods = JsonData.class.getMethods();
      java.util.Map<String, Object> kv = new java.util.HashMap<>();
      for (java.lang.reflect.Method m : methods) {
        if (m.getName().startsWith("get")) {
          String field = Character.toLowerCase(m.getName().charAt(3)) + m.getName().substring(4);
          if (field.equals("class")) continue;
          Object val = m.invoke(data);
          kv.put(field, val);
        }
      }

      for (var entry : mapping.entrySet()) {
        String in = entry.getKey();
        String out = rename.getOrDefault(entry.getValue(), entry.getValue());
        Object val = kv.get(in);
        if (val == null) continue;

        Spec s = spec.get(out);
        String normalized;
        if (s != null && "num".equalsIgnoreCase(s.tipo)) {
          String digits = String.valueOf(val).replaceAll("\\D", "");
          if (digits.isEmpty()) digits = "0";
          if (s.largo > 0) {
            if (digits.length() > s.largo) digits = digits.substring(0, s.largo);
            else digits = String.format("%0" + s.largo + "d", Integer.parseInt(digits));
          }
          normalized = digits;
        } else {
          String sVal = String.valueOf(val).trim();
          if (s != null && s.largo > 0 && sVal.length() > s.largo) {
            sVal = sVal.substring(0, s.largo);
          }
          normalized = sVal;
        }

        ObjectNode item = mapper.createObjectNode();
        item.put("key", out);
        item.put("value", normalized);
        root.add(item);
      }

      return root;
    } catch (Exception e) {
      ArrayNode root = mapper.createArrayNode();
      var node = mapper.valueToTree(data);
      node.fieldNames().forEachRemaining(fn -> {
        ObjectNode item = mapper.createObjectNode();
        item.put("key", fn);
        var v = node.get(fn);
        if (v != null && v.isNumber()) item.set("value", v);
        else if (v != null && v.isBoolean()) item.set("value", v);
        else item.put("value", v != null ? v.asText() : "");
        root.add(item);
      });
      return root;
    }
  }

  public static class OrchestrationResult {
    private final boolean ok;
    private final String message;
    private OrchestrationResult(boolean ok, String message) { this.ok = ok; this.message = message; }
    public static OrchestrationResult success() { return new OrchestrationResult(true, ""); }
    public static OrchestrationResult fail(String msg) { return new OrchestrationResult(false, msg); }
    public boolean isOk() { return ok; }
    public String getMessage() { return message; }
  }
}
