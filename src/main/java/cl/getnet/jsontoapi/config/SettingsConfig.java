package cl.getnet.jsontoapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Configuration
public class SettingsConfig {
  private static final Logger log = LoggerFactory.getLogger(SettingsConfig.class);

  @Value("${app.local-config-path:config/local.json}")
  private String defaultPath;

  @Value("${app.channels-config-path:config/channels.json}")
  private String defaultChannelsPath;

  @Bean
  public LocalSettings localSettings(ObjectMapper mapper) {
    try {
      String envPath = System.getenv("APP_LOCAL_CONFIG");
      if (envPath == null || envPath.isBlank())
        envPath = System.getenv("LOCAL_CONFIG_PATH");
      String pathToUse = (envPath != null && !envPath.isBlank()) ? envPath : defaultPath;

      log.info("Cargando configuración local desde {}", pathToUse);
      byte[] bytes = Files.readAllBytes(Path.of(pathToUse));
      LocalSettings settings = mapper.readValue(bytes, LocalSettings.class);
      if (settings.getBaseUrl() == null) {
        throw new IllegalStateException("local.json inválido: falta baseUrl o auth");
      }

      String envChannelsPath = System.getenv("APP_CHANNELS_CONFIG");
      String channelsPathToUse = (envChannelsPath != null && !envChannelsPath.isBlank()) ? envChannelsPath
          : defaultChannelsPath;

      log.info("Cargando configuración de canales desde {}", channelsPathToUse);
      if (Files.exists(Path.of(channelsPathToUse))) {
        byte[] channelsBytes = Files.readAllBytes(Path.of(channelsPathToUse));
        List<LocalSettings.ChannelInfo> channels = mapper.readValue(channelsBytes,
            new com.fasterxml.jackson.core.type.TypeReference<List<LocalSettings.ChannelInfo>>() {
            });
        settings.setChannelData(channels);
      } else {
        log.warn("No se encontró archivo de canales en {}, se inicia sin canales o con los que haya en local.json",
            channelsPathToUse);
      }

      return settings;
    } catch (Exception e) {
      throw new IllegalStateException("No se pudo cargar configuración: " + e.getMessage(), e);
    }
  }
}