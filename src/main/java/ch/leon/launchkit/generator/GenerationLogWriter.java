package ch.leon.launchkit.generator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

public class GenerationLogWriter {

    public void write(
            Path projectRoot,
            String projectName,
            String backend,
            String frontend,
            String database,
            boolean docker,
            boolean ci,
            int dbPort
    ) throws IOException {

        Path logDir = projectRoot
                .resolve(".launchkit")
                .resolve("logs");

        Files.createDirectories(logDir);

        String json = """
                {
                  "projectName": "%s",
                  "backend": "%s",
                  "frontend": "%s",
                  "database": "%s",
                  "docker": %s,
                  "ci": %s,
                  "dbPort": %d,
                  "generatedAt": "%s"
                }
                """.formatted(
                escape(projectName),
                escape(backend),
                escape(frontend),
                escape(database),
                docker,
                ci,
                dbPort,
                LocalDateTime.now()
        );

        Files.writeString(
                logDir.resolve("generation-log.json"),
                json
        );
    }

    private String escape(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }
}