package cl.getnet.jsontoapi.client;

import cl.getnet.jsontoapi.config.LocalSettings;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Optional;

@Component
public class EstateManagerClient {
  private static final Logger log = LoggerFactory.getLogger(EstateManagerClient.class);
  private final RestTemplate rest;
  private final String baseUrl;

  public EstateManagerClient(LocalSettings settings, RestTemplateBuilder builder) {
    this.baseUrl = settings.getBaseUrl();
    this.rest = builder
        .rootUri(trimTrailingSlash(this.baseUrl))
        .basicAuthentication(settings.getAuth().getUsername(), settings.getAuth().getPassword())
        .setConnectTimeout(Duration.ofSeconds(10))
        .setReadTimeout(Duration.ofSeconds(30))
        .build();
  }

  private static String trimTrailingSlash(String s) {
    if (s == null) return null;
    return s.endsWith("/") ? s.substring(0, s.length() - 1) : s;
  }

  private static String truncate(String s) {
    if (s == null) return null;
    return s.length() > 1200 ? s.substring(0, 1200) + "...<truncated>" : s;
  }

  public Optional<JsonNode> getFolder(String signature) {
    String uri = UriComponentsBuilder.fromPath("/emapi/dms/terminals/criterias/")
        .queryParam("category", 0)
        .queryParam("precise", false)
        .queryParam("recursive", true)
        .queryParam("signature", signature)
        .toUriString();
    try {
      log.debug("GET {}{}", this.baseUrl, uri);
      log.debug("GET {}{}", this.baseUrl, uri);
      ResponseEntity<JsonNode> resp = rest.exchange(uri, HttpMethod.GET, null, JsonNode.class);
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
      try {
        String alt = UriComponentsBuilder.fromPath("/emapi/dms/terminals/criterias/")
            .queryParam("precise", false)
            .queryParam("recursive", true)
            .queryParam("signature", signature)
            .toUriString();
        log.debug("GET {}{}", this.baseUrl, alt);
        ResponseEntity<JsonNode> resp = rest.exchange(alt, HttpMethod.GET, null, JsonNode.class);
        JsonNode node = resp.getBody();
        if (node == null) return Optional.empty();
        if (node.isArray() && node.size() > 0) return Optional.of(node.get(0));
        if (node.has("elements") && node.get("elements").isArray() && node.get("elements").size() > 0) {
          return Optional.of(node.get("elements").get(0));
        }
        return Optional.empty();
      } catch (Exception e2) {
        log.error("getFolder fallback error: {}", e2.getMessage());
        return Optional.empty();
      }
    } catch (HttpClientErrorException ex) {
      log.error("getFolder HTTP {}: {}", ex.getRawStatusCode(), ex.getResponseBodyAsString());
      return Optional.empty();
    } catch (Exception e) {
      log.error("getFolder error: {}", e.getMessage());
      return Optional.empty();
    }
  }

  public Optional<JsonNode> getTerminalBySignature(String terminalSignature) {
    String uri = "/emapi/dms/terminals/signature/" + terminalSignature;
    try {
      log.debug("GET {}{}", this.baseUrl, uri);
      ResponseEntity<JsonNode> resp = rest.exchange(uri, HttpMethod.GET, null, JsonNode.class);
      return Optional.ofNullable(resp.getBody());
    } catch (HttpClientErrorException.NotFound nf) {
      return Optional.empty();
    } catch (HttpClientErrorException ex) {
      log.error("getTerminalBySignature HTTP {}: {}", ex.getRawStatusCode(), ex.getResponseBodyAsString());
      return Optional.empty();
    } catch (Exception e) {
      log.error("getTerminalBySignature error: {}", e.getMessage());
      return Optional.empty();
    }
  }

  public Optional<JsonNode> createTerminal(String name, String parentId) {
    String uri = "/emapi/dms/terminals";
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
      log.debug("POST {}{} body={}", this.baseUrl, uri, payload);
      ResponseEntity<JsonNode> resp = rest.exchange(uri, HttpMethod.POST, entity, JsonNode.class);
      return Optional.ofNullable(resp.getBody());
    } catch (HttpClientErrorException ex) {
      log.error("createTerminal HTTP {}: {}", ex.getRawStatusCode(), ex.getResponseBodyAsString());
      return Optional.empty();
    } catch (Exception e) {
      log.error("createTerminal error: {}", e.getMessage());
      return Optional.empty();
    }
  }

  public boolean moveToFolder(String terminalId, String folderId) {
    String uri = "/emapi/dms/terminals/" + terminalId + "/parent/" + folderId;
    try {
      log.debug("POST {}{}", this.baseUrl, uri);
      ResponseEntity<Void> resp = rest.postForEntity(uri, null, Void.class);
      return resp.getStatusCode().is2xxSuccessful();
    } catch (HttpClientErrorException ex) {
      log.error("moveToFolder HTTP {}: {}", ex.getRawStatusCode(), ex.getResponseBodyAsString());
      return false;
    } catch (Exception e) {
      log.error("moveToFolder error: {}", e.getMessage());
      return false;
    }
  }

  public boolean updateTerminalParams(String terminalId, String template, String version, JsonNode body) {
    String uri = "/emapi/pms/terminals/" + terminalId + "/" + template + "/" + version;
    try {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      HttpEntity<JsonNode> entity = new HttpEntity<>(body, headers);
      log.debug("PUT {}{} body={}", this.baseUrl, uri, truncate(body.toString()));
      ResponseEntity<Void> resp = rest.exchange(uri, HttpMethod.PUT, entity, Void.class);
      return resp.getStatusCode().is2xxSuccessful();
    } catch (HttpClientErrorException ex) {
      log.error("updateTerminalParams HTTP {}: {}", ex.getRawStatusCode(), ex.getResponseBodyAsString());
      return false;
    } catch (Exception e) {
      log.error("updateTerminalParams error: {}", e.getMessage());
      return false;
    }
  }
}
