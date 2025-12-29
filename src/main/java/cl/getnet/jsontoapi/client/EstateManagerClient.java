package cl.getnet.jsontoapi.client;

import cl.getnet.jsontoapi.audit.AuditContext;
import cl.getnet.jsontoapi.audit.AuditModels;
import cl.getnet.jsontoapi.config.LocalSettings;
import cl.getnet.jsontoapi.helpers.SecurityValidator;

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

  private void setLastError(String msg) {
    lastError.set(msg);
  }

  public String consumeLastError() {
    String s = lastError.get();
    lastError.remove();
    return s;
  }

  public EstateManagerClient(LocalSettings settings, RestTemplateBuilder builder) {
    this.baseUrl = trimTrailingSlash(settings.getBaseUrl());
    this.rest = builder
        .rootUri(this.baseUrl)
        .basicAuthentication(settings.getAuth().getUsername(), settings.getAuth().getPassword())
        .setConnectTimeout(Duration.ofSeconds(10))
        .setReadTimeout(Duration.ofSeconds(30))
        .build();
  }

  private static String trimTrailingSlash(String s) {
    if (s == null)
      return null;
    return s.endsWith("/") ? s.substring(0, s.length() - 1) : s;
  }

  private String safePathSegment(String input, String fieldName) {
    String validated = SecurityValidator.safePathSegment(input, fieldName);
    if (validated == null || validated.isBlank()) {
      throw new IllegalArgumentException("Invalid " + fieldName);
    }
    return validated;
  }

  private JsonNode asJsonNode(Object o) {
    try {
      if (o == null)
        return null;
      if (o instanceof JsonNode jn)
        return jn;
      if (o instanceof String s) {
        try {
          return MAPPER.readTree(s);
        } catch (Exception e) {
          return MAPPER.getNodeFactory().textNode(s);
        }
      }
      return MAPPER.valueToTree(o);
    } catch (Exception e) {
      return MAPPER.getNodeFactory().textNode(String.valueOf(o));
    }
  }

  private void addCall(String method, String url, Object reqBody, int status, Object respBody, long ms) {
    var rec = AuditContext.get();
    if (rec == null)
      return;
    AuditModels.ApiCall call = new AuditModels.ApiCall();
    call.method = method;
    call.url = url;
    call.requestBody = asJsonNode(reqBody);
    call.status = status;
    call.responseBody = asJsonNode(respBody);
    rec.calls.add(call);
  }

  private String fmt(HttpClientErrorException ex) {
    return "HTTP " + ex.getStatusCode().value() + " " + ex.getStatusText();
    // + (ex.getResponseBodyAsString() != null &&
    // !ex.getResponseBodyAsString().isBlank() ? " - " +
    // ex.getResponseBodyAsString() : "");
  }

  public Optional<JsonNode> getFolder(String signature) {
    long t0 = System.currentTimeMillis();
    String endpoint = null;
    String name = "getFolder";

    try {
      String validateSignature = safePathSegment(signature, "Folder Signature");

      String encodedUrl = "https://estate-manager-nar01.preprod.icloud.ingenico.com/emapi/dms/terminals/criterias/";

      endpoint = UriComponentsBuilder.fromUriString(encodedUrl)
          .queryParam("category", 0)
          .queryParam("precise", false)
          .queryParam("recursive", true)
          .queryParam("signature", validateSignature)
          .build()
          .toUriString();

      ResponseEntity<JsonNode> resp = rest.exchange(endpoint, HttpMethod.GET, null, JsonNode.class);
      addCall("GET", endpoint, null, resp.getStatusCode().value(), resp.getBody(), System.currentTimeMillis() - t0);

      JsonNode node = resp.getBody();

      if (node == null || node.isNull())
        return Optional.empty();
      if (node.isArray()) {
        if (node.size() > 0)
          return Optional.of(node.get(0));
        return Optional.empty();
      }
      if (node.has("elements") && node.get("elements").isArray()) {
        if (node.get("elements").size() > 0) {
          return Optional.of(node.get("elements").get(0));
        }
        return Optional.empty();
      }
      return Optional.of(node);

    } catch (IllegalArgumentException ex) {
      endpoint = "dms/terminals/criterias/";
      return IllegalDataShow(endpoint, ex, "GET", name);
    } catch (HttpClientErrorException ex2) {
      endpoint = "dms/terminals/criterias/";
      return HttpDataShow(endpoint, ex2, "GET", name);
    } catch (Exception e) {
      endpoint = "dms/terminals/criterias/";
      return ExceptionDataShow(endpoint, e, "GET", name);
    }
  }

  public Optional<JsonNode> getTerminalBySignature(String terminalSignature) {
    long t0 = System.currentTimeMillis();
    String endpoint = null;
    String name = "getTerminalBySignature";
    try {
      String validatedSignature = safePathSegment(terminalSignature, "Terminal SIgnature");

      String baseUrl = "https://estate-manager-nar01.preprod.icloud.ingenico.com/emapi/dms/terminals/signature";
      endpoint = UriComponentsBuilder.fromUriString(baseUrl)
          .pathSegment(validatedSignature)
          .build()
          .toUriString();

      ResponseEntity<JsonNode> resp = rest.exchange(endpoint, HttpMethod.GET, null, JsonNode.class);

      addCall("GET", endpoint, null, resp.getStatusCode().value(), resp.getBody(), System.currentTimeMillis() - t0);
      return Optional.ofNullable(resp.getBody());

    } catch (IllegalArgumentException ex) {
      endpoint = "dms/terminals/criterias/";
      return IllegalDataShow(endpoint, ex, "GET", name);
    } catch (HttpClientErrorException ex2) {
      if (ex2.getStatusCode() == HttpStatus.NOT_FOUND) {
        return Optional.empty();
      }
      endpoint = "dms/terminals/criterias/";
      return HttpDataShow(endpoint, ex2, "GET", name);
    } catch (Exception e) {
      endpoint = "dms/terminals/criterias/";
      return ExceptionDataShow(endpoint, e, "GET", name);
    }
  }

  public Optional<JsonNode> createTerminal(String tName, String parentId) {
    String endpoint = null;
    long t0 = System.currentTimeMillis();
    String name = "createTerminal";
    try {

      String validatedName = safePathSegment(tName, "Terminal Name");
      String validatedParentId = safePathSegment(parentId, "Parent Id");

      Map<String, Object> payload = new LinkedHashMap<>();
      payload.put("active", true);
      payload.put("aesStatus", 0);
      payload.put("attributes", null);
      payload.put("category", 1);
      payload.put("commDetails", null);
      payload.put("description", "");
      payload.put("merchantId", "null");
      payload.put("name", validatedName);
      payload.put("nextCallDate", null);
      payload.put("signature", validatedName);
      payload.put("status", 0);
      payload.put("target", true);
      payload.put("type", "AXIUMNX");
      payload.put("parent", validatedParentId);

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

      String encodedUrl = "https://estate-manager-nar01.preprod.icloud.ingenico.com/emapi/dms/terminals/";

      ResponseEntity<JsonNode> resp = rest.exchange(encodedUrl, HttpMethod.POST, entity, JsonNode.class);
      addCall("POST", encodedUrl, payload, resp.getStatusCode().value(), resp.getBody(),
          System.currentTimeMillis() - t0);
      return Optional.ofNullable(resp.getBody());

    } catch (IllegalArgumentException ex) {
      endpoint = "dms/terminals/";
      return IllegalDataShow(endpoint, ex, "POST", name);
    } catch (HttpClientErrorException ex2) {
      endpoint = "dms/terminals/";
      return HttpDataShow(endpoint, ex2, "POST", name);
    } catch (Exception e) {
      endpoint = "dms/terminals/";
      return ExceptionDataShow(endpoint, e, "POST", name);
    }
  }

  public boolean moveToFolder(String terminalId, String folderId) {
    long t0 = System.currentTimeMillis();
    String endpoint = null;
    String name = "moveToFolder";

    try {
      String validatedTerminalId = safePathSegment(terminalId, "Terminal Id");
      String validatedFolderId = safePathSegment(folderId, "Folder Id");

      String encodedUrl = "https://estate-manager-nar01.preprod.icloud.ingenico.com/emapi/dms/terminals";
      endpoint = UriComponentsBuilder.fromUriString(encodedUrl)
          .pathSegment(validatedTerminalId, "parent", validatedFolderId)
          .build()
          .toUriString();

      ResponseEntity<Void> resp = rest.postForEntity(endpoint, HttpMethod.PUT, Void.class);
      addCall("PUT", endpoint, null, resp.getStatusCode().value(), null, System.currentTimeMillis() - t0);
      return resp.getStatusCode().is2xxSuccessful();

    } catch (IllegalArgumentException ex) {
      endpoint = "dms/terminals/";
      IllegalDataShow(endpoint, ex, "PUT", name);
      return false;
    } catch (HttpClientErrorException ex2) {
      endpoint = "dms/terminals/";
      HttpDataShow(endpoint, ex2, "PUT", name);
      return false;
    } catch (Exception e) {
      endpoint = "dms/terminals/";
      ExceptionDataShow(endpoint, e, "PUT", name);
      return false;
    }
  }

  public boolean updateTerminalParams(String terminalId, String templateName, String version, JsonNode body) {
    long t0 = System.currentTimeMillis();
    String endpoint = null;
    String name = "updateTerminalParams";

    try {

      String validatedTerminalId = safePathSegment(terminalId, "Terminal Id");
      String validatedTemplate = safePathSegment(templateName, "Template name");
      String validatedVersion = safePathSegment(version, "Template version");

      String encodedUrl = "https://estate-manager-nar01.preprod.icloud.ingenico.com/emapi/pms/terminals";
      endpoint = UriComponentsBuilder.fromUriString(encodedUrl)
          .pathSegment(validatedTerminalId, validatedTemplate, validatedVersion)
          .build()
          .toUriString();

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      HttpEntity<JsonNode> entity = new HttpEntity<>(body, headers);

      ResponseEntity<Void> resp = rest.exchange(endpoint, HttpMethod.PUT, entity, Void.class);
      addCall("PUT", endpoint, body, resp.getStatusCode().value(), null, System.currentTimeMillis() - t0);
      return resp.getStatusCode().is2xxSuccessful();

    } catch (IllegalArgumentException ex) {
      endpoint = "pms/terminals/";
      IllegalDataShow(endpoint, ex, "PUT", name);
      return false;
    } catch (HttpClientErrorException ex2) {
      endpoint = "pms/terminals/";
      HttpDataShow(endpoint, ex2, "PUT", name);
      return false;
    } catch (Exception e) {
      endpoint = "pms/terminals/";
      ExceptionDataShow(endpoint, e, "PUT", name);
      return false;
    }
  }

  private Optional<JsonNode> IllegalDataShow(String endpoint, IllegalArgumentException ex, String verb, String action) {
    long t0 = System.currentTimeMillis();
    String info = "[INVALID]";
    addCall(verb, endpoint + info, null, 400, ex.getMessage(), System.currentTimeMillis() - t0);
    setLastError(action + " illegal entry: " + ex.getMessage());
    log.warn(action + ": {}", ex.getMessage());
    return Optional.empty();
  }

  private Optional<JsonNode> HttpDataShow(String endpoint, HttpClientErrorException ex, String verb, String action) {
    long t0 = System.currentTimeMillis();
    String info = "[ERROR]";
    addCall(verb, endpoint + info, null, ex.getStatusCode().value(), null, System.currentTimeMillis() - t0);
    setLastError(fmt(ex));
    log.error(action + "fallback: {}", ex.getStatusCode().value(), ex.getResponseBodyAsString());
    return Optional.empty();
  }

  private Optional<JsonNode> ExceptionDataShow(String endpoint, Exception ex, String verb, String action) {
    long t0 = System.currentTimeMillis();
    String info = "[EXCEPTION]";
    addCall(verb, endpoint + info, null, 599, ex.getMessage(), System.currentTimeMillis() - t0);
    setLastError("Exception: " + ex.getMessage());
    log.error(action + "error: {}", ex.getMessage());
    return Optional.empty();
  }
}
