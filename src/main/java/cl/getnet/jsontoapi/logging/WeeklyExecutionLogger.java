package cl.getnet.jsontoapi.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;

@Component
public class WeeklyExecutionLogger {
  private static final Logger log = LoggerFactory.getLogger(WeeklyExecutionLogger.class);
  private final String baseDir;
  private final ZoneId zoneId;

  public WeeklyExecutionLogger(
      @Value("${app.exec-log.dir:logs}") String baseDir,
      @Value("${app.exec-log.timezone:America/Santiago}") String zone) {
    this.baseDir = baseDir == null || baseDir.isBlank() ? "logs" : baseDir;
    ZoneId zid;
    try {
      zid = ZoneId.of(zone);
    } catch (Exception e) {
      zid = ZoneId.systemDefault();
    }
    this.zoneId = zid;
  }

  public void log(ObjectNode record) {
    try {
      ZonedDateTime now = ZonedDateTime.now(zoneId);
      WeekFields wf = WeekFields.of(Locale.getDefault());
      int week = now.get(wf.weekOfWeekBasedYear());
      int year = now.get(wf.weekBasedYear());

      String fileName = String.format("week-%d-W%02d.jsonl", year, week);
      Path dir = Path.of(baseDir);
      Files.createDirectories(dir);
      Path file = dir.resolve(fileName);

      byte[] line = (record.toString() + System.lineSeparator()).getBytes(StandardCharsets.UTF_8);
      Files.write(file, line, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
    } catch (IOException ioe) {
      log.error("No se pudo escribir log de ejecución: {}", ioe.getMessage());
    } catch (Exception e) {
      log.error("Error inesperado al escribir log: {}", e.getMessage());
    }
  }
}
