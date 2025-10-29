package cl.getnet.jsontoapi.client;

import cl.getnet.jsontoapi.audit.AuditContext;
import cl.getnet.jsontoapi.audit.AuditModels;
import cl.getnet.jsontoapi.config.LocalSettings;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class EstateManagerClient {

  private static final Logger log = LoggerFactory.getLogger(EstateManagerClient.class);
  private static final ObjectMapper MAPPER = new ObjectMapper();

  private final RestTemplate rest;
  private final String baseUrl;

  private final ThreadLocal<String> lastError = new ThreadLocal<>();
  private void setLastError(String msg) { lastError.set(msg); }
  public String consumeLastError() { String s = lastError.get(); lastError.remove(); return s; }

  public EstateManagerClient(LocalSettings settings, RestTemplateBuilder builder) {
    this.baseUrl = trimTrailingSlash(settings.getBaseUrl());
    this.rest = builder
        .rootUri(this.baseUrl)
        .basicAuthentication(
            settings.getAuth().getUsername(),
            settings.getAuth().getPassword()
        )
        .setConnectTimeout(Duration.ofSeconds(10))
        .setReadTimeout(Duration.ofSeconds(30))
        .build();
  }

  private static String trimTrailingSlash(String s) {
    if (s == null) return null;
    return s.endsWith("/") ? s.substring(0, s.length() - 1) : s;
  }

  private JsonNode asJsonNode(Object o) {
    try {
      if (o == null) return null;
      if (o instanceof JsonNode jn) return jn;
      if (o instanceof String s) {
        try { return MAPPER.readTree(s); }
        catch (Exception e) { return MAPPER.getNodeFactory().textNode(s); }
      }
      return MAPPER.valueToTree(o);
    } catch (Exception e) {
      return MAPPER.getNodeFactory().textNode(String.valueOf(o));
    }
  }

  private void addCall(String method, String url, Object reqBody, int status, Object respBody, long ms) {
    var rec = AuditContext.get();
    if (rec == null) return;
    AuditModels.ApiCall call = new AuditModels.ApiCall();
    call.method = method;
    call.url = this.baseUrl + url;
    call.requestBody = asJsonNode(reqBody);
    call.status = status;
    call.responseBody = asJsonNode(respBody);
    rec.calls.add(call);
  }

  private String fmt(HttpClientErrorException ex) {
    return "HTTP " + ex.getRawStatusCode() + " " + ex.getStatusText()
        + (ex.getResponseBodyAsString() != null && !ex.getResponseBodyAsString().isBlank()
           ? " - " + ex.getResponseBodyAsString()
           : "");
  }

  public Optional<JsonNode> getFolder(String signature) {
    String uri = UriComponentsBuilder.fromPath("/emapi/dms/terminals/criterias/")
        .queryParam("category", 0)
        .queryParam("precise", false)
        .queryParam("recursive", true)
        .queryParam("signature", signature)
        .toUriString();

    long t0 = System.currentTimeMillis();
    try {
      ResponseEntity<JsonNode> resp = rest.exchange(uri, HttpMethod.GET, null, JsonNode.class);
      addCall("GET", uri, null, resp.getStatusCodeValue(), resp.getBody(), System.currentTimeMillis() - t0);

      JsonNode node = resp.getBody();
      if (node == null || node.isNull()) return Optional.empty();
      if (node.isArray()) {
        if (node.size() == 0) return Optional.empty();
        return Optional.ofNullable(node.get(0));
      }
      if (node.has("elements") && node.get("elements").isArray() && node.get("elements").size() > 0) {
        return Optional.of(node.get("elements").get(0));
      }
      return Optional.of(node);

    } catch (HttpClientErrorException.NotFound nf) {
      String alt = UriComponentsBuilder.fromPath("/emapi/dms/terminals/criterias/")
          .queryParam("precise", false)
          .queryParam("recursive", true)
          .queryParam("signature", signature)
          .toUriString();
      try {
        ResponseEntity<JsonNode> resp = rest.exchange(alt, HttpMethod.GET, null, JsonNode.class);
        addCall("GET", alt, null, resp.getStatusCodeValue(), resp.getBody(), System.currentTimeMillis() - t0);

        JsonNode node = resp.getBody();
        if (node == null) return Optional.empty();
        if (node.isArray() && node.size() > 0) return Optional.of(node.get(0));
        if (node.has("elements") && node.get("elements").isArray() && node.get("elements").size() > 0) {
          return Optional.of(node.get("elements").get(0));
        }
        return Optional.empty();

      } catch (HttpClientErrorException ex2) {
        addCall("GET", alt, null, ex2.getRawStatusCode(), ex2.getResponseBodyAsString(), System.currentTimeMillis() - t0);
        setLastError(fmt(ex2));
        log.error("getFolder fallback HTTP {}: {}", ex2.getRawStatusCode(), ex2.getResponseBodyAsString());
        return Optional.empty();
      } catch (Exception e2) {
        addCall("GET", alt, null, 599, e2.getMessage(), System.currentTimeMillis() - t0);
        setLastError("Exception: " + e2.getMessage());
        log.error("getFolder fallback error: {}", e2.getMessage());
        return Optional.empty();
      }

    } catch (HttpClientErrorException ex) {
      addCall("GET", uri, null, ex.getRawStatusCode(), ex.getResponseBodyAsString(), System.currentTimeMillis() - t0);
      setLastError(fmt(ex));
      log.error("getFolder HTTP {}: {}", ex.getRawStatusCode(), ex.getResponseBodyAsString());
      return Optional.empty();
    } catch (Exception e) {
      addCall("GET", uri, null, 599, e.getMessage(), System.currentTimeMillis() - t0);
      setLastError("Exception: " + e.getMessage());
      log.error("getFolder error: {}", e.getMessage());
      return Optional.empty();
    }
  }

  public Optional<JsonNode> getTerminalBySignature(String terminalSignature) {
    String uri = "/emapi/dms/terminals/signature/" + terminalSignature;
    long t0 = System.currentTimeMillis();
    try {
      ResponseEntity<JsonNode> resp = rest.exchange(uri, HttpMethod.GET, null, JsonNode.class);
      addCall("GET", uri, null, resp.getStatusCodeValue(), resp.getBody(), System.currentTimeMillis() - t0);
      return Optional.ofNullable(resp.getBody());
    } catch (HttpClientErrorException.NotFound nf) {
      addCall("GET", uri, null, 404, null, System.currentTimeMillis() - t0);
      return Optional.empty();
    } catch (HttpClientErrorException ex) {
      addCall("GET", uri, null, ex.getRawStatusCode(), ex.getResponseBodyAsString(), System.currentTimeMillis() - t0);
      setLastError(fmt(ex));
      log.error("getTerminalBySignature HTTP {}: {}", ex.getRawStatusCode(), ex.getResponseBodyAsString());
      return Optional.empty();
    } catch (Exception e) {
      addCall("GET", uri, null, 599, e.getMessage(), System.currentTimeMillis() - t0);
      setLastError("Exception: " + e.getMessage());
      log.error("getTerminalBySignature error: {}", e.getMessage());
      return Optional.empty();
    }
  }

  public Optional<JsonNode> createTerminal(String name, String parentId) {
    String uri = "/emapi/dms/terminals";
    long t0 = System.currentTimeMillis();
    try {
      Map<String, Object> payload = new LinkedHashMap<>();
      payload.put("active", true);
      payload.put("aesStatus", 0);
      payload.put("attributes", null);
      payload.put("category", 1);
      payload.put("commDetails", null);
      payload.put("description", "");
      payload.put("merchantId", "null");
      payload.put("name", name);
      payload.put("nextCallDate", null);
      payload.put("signature", name);
      payload.put("status", 0);
      payload.put("target", true);
      payload.put("type", "AXIUMNX");
      payload.put("parent", parentId);

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

      ResponseEntity<JsonNode> resp = rest.exchange(uri, HttpMethod.POST, entity, JsonNode.class);
      addCall("POST", uri, payload, resp.getStatusCodeValue(), resp.getBody(), System.currentTimeMillis() - t0);
      return Optional.ofNullable(resp.getBody());

    } catch (HttpClientErrorException ex) {
      addCall("POST", uri, null, ex.getRawStatusCode(), ex.getResponseBodyAsString(), System.currentTimeMillis() - t0);
      setLastError(fmt(ex));
      log.error("createTerminal HTTP {}: {}", ex.getRawStatusCode(), ex.getResponseBodyAsString());
      return Optional.empty();
    } catch (Exception e) {
      addCall("POST", uri, null, 599, e.getMessage(), System.currentTimeMillis() - t0);
      setLastError("Exception: " + e.getMessage());
      log.error("createTerminal error: {}", e.getMessage());
      return Optional.empty();
    }
  }

  public boolean moveToFolder(String terminalId, String folderId) {
    String uri = "/emapi/dms/terminals/" + terminalId + "/parent/" + folderId;
    long t0 = System.currentTimeMillis();
    try {
      ResponseEntity<Void> resp = rest.postForEntity(uri, null, Void.class);
      addCall("POST", uri, null, resp.getStatusCodeValue(), null, System.currentTimeMillis() - t0);
      return resp.getStatusCode().is2xxSuccessful();
    } catch (HttpClientErrorException ex) {
      addCall("POST", uri, null, ex.getRawStatusCode(), ex.getResponseBodyAsString(), System.currentTimeMillis() - t0);
      setLastError(fmt(ex));
      log.error("moveToFolder HTTP {}: {}", ex.getRawStatusCode(), ex.getResponseBodyAsString());
      return false;
    } catch (Exception e) {
      addCall("POST", uri, null, 599, e.getMessage(), System.currentTimeMillis() - t0);
      setLastError("Exception: " + e.getMessage());
      log.error("moveToFolder error: {}", e.getMessage());
      return false;
    }
  }

  public boolean updateTerminalParams(String terminalId, String template, String version, JsonNode body) {
    String uri = "/emapi/pms/terminals/" + terminalId + "/" + template + "/" + version;
    long t0 = System.currentTimeMillis();
    try {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      HttpEntity<JsonNode> entity = new HttpEntity<>(body, headers);

      ResponseEntity<Void> resp = rest.exchange(uri, HttpMethod.PUT, entity, Void.class);
      addCall("PUT", uri, body, resp.getStatusCodeValue(), null, System.currentTimeMillis() - t0);
      return resp.getStatusCode().is2xxSuccessful();

    } catch (HttpClientErrorException ex) {
      addCall("PUT", uri, body, ex.getRawStatusCode(), ex.getResponseBodyAsString(), System.currentTimeMillis() - t0);
      setLastError(fmt(ex));
      log.error("updateTerminalParams HTTP {}: {}", ex.getRawStatusCode(), ex.getResponseBodyAsString());
      return false;
    } catch (Exception e) {
      addCall("PUT", uri, body, 599, e.getMessage(), System.currentTimeMillis() - t0);
      setLastError("Exception: " + e.getMessage());
      log.error("updateTerminalParams error: {}", e.getMessage());
      return false;
    }
  }
}
