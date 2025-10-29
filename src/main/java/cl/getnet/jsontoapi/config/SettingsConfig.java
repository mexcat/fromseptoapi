package cl.getnet.jsontoapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
public class SettingsConfig {
  private static final Logger log = LoggerFactory.getLogger(SettingsConfig.class);

  @Value("${app.local-config-path:config/local.json}")
  private String defaultPath;

  @Bean
  public LocalSettings localSettings(ObjectMapper mapper) {
    try {
      String envPath = System.getenv("APP_LOCAL_CONFIG");
      if (envPath == null || envPath.isBlank()) envPath = System.getenv("LOCAL_CONFIG_PATH");
      String pathToUse = (envPath != null && !envPath.isBlank()) ? envPath : defaultPath;
      log.info("Cargando configuración local desde {}", pathToUse);
      byte[] bytes = Files.readAllBytes(Path.of(pathToUse));
      LocalSettings settings = mapper.readValue(bytes, LocalSettings.class);
      if (settings.getBaseUrl() == null || settings.getAuth() == null) {
        throw new IllegalStateException("local.json inválido: falta baseUrl o auth");
      }
      return settings;
    } catch (Exception e) {
      throw new IllegalStateException("No se pudo cargar local.json: " + e.getMessage(), e);
    }
  }
}
