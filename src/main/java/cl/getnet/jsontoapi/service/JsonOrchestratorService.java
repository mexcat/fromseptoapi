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
      return OrchestrationResult.fail("carpeta no encontrada");
    }
    JsonNode folder = folderOpt.get();
    String folderId = folder.hasNonNull("id") ? folder.get("id").asText() : null;
    if (folderId == null || folderId.isBlank()) {
      return OrchestrationResult.fail("carpeta no encontrada (sin id)");
    }

    String terminalSignature = normalizeTerminal(data.getTerminal_code());
    Optional<JsonNode> termOpt = client.getTerminalBySignature(terminalSignature);

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
        if (!moved) return OrchestrationResult.fail("no se pudo mover terminal");
      }
    } else {
      Optional<JsonNode> created = client.createTerminal(terminalSignature, folderId);
      if (created.isEmpty() || created.get() == null || !created.get().hasNonNull("id")) {
        return OrchestrationResult.fail("no se pudo crear terminal");
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
    if (!updated) return OrchestrationResult.fail("no se pudo actualizar parámetros (paso D)");

    return OrchestrationResult.success();
  }

  private JsonNode buildUpdateBody(JsonData data) {
    try {
      String path = System.getenv("APP_PARAM_MAPPING");
      if (path == null || path.isBlank()) path = "config/param-mapping.json";
      Map<String, String> mapping = mapper.readValue(new java.io.File(path), mapper.getTypeFactory().constructMapType(Map.class, String.class, String.class));

      ObjectNode root = mapper.createObjectNode();
      ArrayNode values = mapper.createArrayNode();
      root.set("values", values);

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
        String out = entry.getValue();
        Object val = kv.get(in);
        if (val != null) {
          ObjectNode item = mapper.createObjectNode();
          item.put("name", out);
          item.put("value", String.valueOf(val));
          values.add(item);
        }
      }
      root.put("source", "jsonToApi");
      root.put("ts", java.time.OffsetDateTime.now().toString());
      return root;
    } catch (Exception e) {
      ObjectNode root = mapper.createObjectNode();
      root.set("data", mapper.valueToTree(data));
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
