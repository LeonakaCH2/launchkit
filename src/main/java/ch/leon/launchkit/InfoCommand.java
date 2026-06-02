package ch.leon.launchkit;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;

@Command(
        name = "info",
        description = "Show information about a generated LaunchKit project."
)
public class InfoCommand implements Callable<Integer> {

    @Parameters(
            index = "0",
            description = "Path to the generated project."
    )
    private String projectPath;

    @Override
    public Integer call() {
        try {
            Path logFile = Path.of(projectPath)
                    .resolve(".launchkit")
                    .resolve("logs")
                    .resolve("generation-log.json");

            if (!Files.exists(logFile)) {
                System.err.println("No LaunchKit generation log found.");
                System.err.println("Expected file:");
                System.err.println("  " + logFile.toAbsolutePath());
                return 1;
            }

            String json = Files.readString(logFile);

            System.out.println();
            System.out.println("LaunchKit project information");
            System.out.println();

            printValue(json, "projectName", "Project");
            printValue(json, "backend", "Backend");
            printValue(json, "frontend", "Frontend");
            printValue(json, "database", "Database");
            printValue(json, "docker", "Docker");
            printValue(json, "ci", "CI");
            printValue(json, "dbPort", "Database port");
            printValue(json, "generatedAt", "Generated at");

            System.out.println();

            return 0;
        } catch (Exception e) {
            System.err.println("Failed to read project info: " + e.getMessage());
            return 1;
        }
    }

    private void printValue(String json, String key, String label) {
        String value = extractValue(json, key);

        if (value == null || value.isBlank()) {
            value = "unknown";
        }

        if ("true".equalsIgnoreCase(value)) {
            value = "enabled";
        } else if ("false".equalsIgnoreCase(value)) {
            value = "disabled";
        }

        System.out.println(label + ": " + value);
    }

    private String extractValue(String json, String key) {
        String quotedKey = "\"" + key + "\"";
        int keyIndex = json.indexOf(quotedKey);

        if (keyIndex == -1) {
            return null;
        }

        int colonIndex = json.indexOf(":", keyIndex);

        if (colonIndex == -1) {
            return null;
        }

        int valueStart = colonIndex + 1;

        while (valueStart < json.length() && Character.isWhitespace(json.charAt(valueStart))) {
            valueStart++;
        }

        if (valueStart >= json.length()) {
            return null;
        }

        if (json.charAt(valueStart) == '"') {
            int stringStart = valueStart + 1;
            int stringEnd = json.indexOf("\"", stringStart);

            if (stringEnd == -1) {
                return null;
            }

            return json.substring(stringStart, stringEnd);
        }

        int valueEnd = valueStart;

        while (
                valueEnd < json.length()
                        && json.charAt(valueEnd) != ','
                        && json.charAt(valueEnd) != '\n'
                        && json.charAt(valueEnd) != '}'
        ) {
            valueEnd++;
        }

        return json.substring(valueStart, valueEnd).trim();
    }
}