package ch.leon.launchkit.generator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TodoSampleGeneratorTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldGenerateTodoSampleProject() throws Exception {
        Path originalDir = Path.of(System.getProperty("user.dir"));

        try {
            System.setProperty("user.dir", tempDir.toString());

            String projectName = tempDir.resolve("todo-test").toString();

            new TodoSampleGenerator().generate(
                    projectName,
                    "springboot",
                    "postgres",
                    true,
                    true,
                    5434
            );

            Path projectRoot = Path.of(projectName);

            assertTrue(Files.exists(projectRoot.resolve("backend/pom.xml")));
            assertTrue(Files.exists(projectRoot.resolve("backend/src/main/resources/application.yml")));
            assertTrue(Files.exists(projectRoot.resolve("docker-compose.yml")));
            assertTrue(Files.exists(projectRoot.resolve(".env.example")));
            assertTrue(Files.exists(projectRoot.resolve("README.md")));
            assertTrue(Files.exists(projectRoot.resolve(".github/workflows/ci.yml")));
        } finally {
            System.setProperty("user.dir", originalDir.toString());
        }
    }

    @Test
    void shouldUseCustomDatabasePort() throws Exception {
        String projectName = tempDir.resolve("todo-port-test").toString();

        new TodoSampleGenerator().generate(
                projectName,
                "springboot",
                "postgres",
                true,
                true,
                5439
        );

        Path projectRoot = Path.of(projectName);

        String dockerCompose = Files.readString(projectRoot.resolve("docker-compose.yml"));
        String applicationYml = Files.readString(projectRoot.resolve("backend/src/main/resources/application.yml"));

        assertTrue(dockerCompose.contains("\"5439:5432\""));
        assertTrue(applicationYml.contains("jdbc:postgresql://localhost:5439/app_db"));
    }
}