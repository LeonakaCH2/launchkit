package ch.leon.launchkit.generator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import ch.leon.launchkit.generator.backend.SpringBootGenerator;

public class ProjectGenerator {

    public void generate(
            String projectName,
            String backend,
            String frontend,
            String database,
            boolean docker,
            boolean ci
    ) throws IOException {
        Path projectRoot = Path.of(projectName);

        if (Files.exists(projectRoot)) {
            throw new IOException("Project already exists: " + projectRoot.toAbsolutePath());
        }

        Files.createDirectories(projectRoot);

        if (backend.equalsIgnoreCase("springboot")) {
            Path backendPath = projectRoot.resolve("backend");
            new SpringBootGenerator().generate(backendPath, projectName);
        } else if (!backend.equalsIgnoreCase("none")) {
            Files.createDirectories(projectRoot.resolve("backend"));
        }

        if (!frontend.equalsIgnoreCase("none")) {
            Files.createDirectories(projectRoot.resolve("frontend"));
        }

        if (docker) {
            Files.writeString(
                    projectRoot.resolve("docker-compose.yml"),
                    createDockerCompose(projectName, database)
            );
        }

        if (ci) {
            Path workflowDir = projectRoot.resolve(".github").resolve("workflows");
            Files.createDirectories(workflowDir);

            Files.writeString(
                    workflowDir.resolve("ci.yml"),
                    createGithubActionsWorkflow()
            );
        }

        Files.writeString(
                projectRoot.resolve("README.md"),
                createReadme(projectName, backend, frontend, database)
        );

        Files.writeString(
                projectRoot.resolve(".env.example"),
                createEnvExample(database)
        );
    }

    private String createDockerCompose(String projectName, String database) {
        if (!database.equalsIgnoreCase("postgres")) {
            return "# Docker Compose setup for " + projectName + "\n";
        }

        return """
        services:
          postgres:
            image: postgres:16
            container_name: %s-postgres
            environment:
              POSTGRES_DB: app_db
              POSTGRES_USER: app_user
              POSTGRES_PASSWORD: app_password
            ports:
              - "5433:5432"
            volumes:
              - %s_postgres_data:/var/lib/postgresql/data
        
        volumes:
          %s_postgres_data:
        """.formatted(projectName, projectName, projectName);
    }

    private String createGithubActionsWorkflow() {
        return """
                name: CI
                
                on:
                  push:
                    branches: [ "main" ]
                  pull_request:
                    branches: [ "main" ]
                
                jobs:
                  build:
                    runs-on: ubuntu-latest
                
                    steps:
                      - name: Checkout repository
                        uses: actions/checkout@v4
                
                      - name: Print project info
                        run: echo "LaunchKit generated project"
                """;
    }

    private String createReadme(
            String projectName,
            String backend,
            String frontend,
            String database
    ) {
        return """
                # %s
                
                Generated with LaunchKit.
                
                ## Stack
                
                - Backend: %s
                - Frontend: %s
                - Database: %s
                
                ## Run
                
                ```bash
                docker compose up
                ```
                """.formatted(projectName, backend, frontend, database);
    }
    private String createEnvExample(String database) {
        if (database.equalsIgnoreCase("postgres")) {
            return """
                POSTGRES_DB=app_db
                POSTGRES_USER=app_user
                POSTGRES_PASSWORD=app_password
                POSTGRES_HOST=localhost
                POSTGRES_PORT=5433

                SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5433/app_db
                SPRING_DATASOURCE_USERNAME=app_user
                SPRING_DATASOURCE_PASSWORD=app_password
                """;
        }

        return """
            # Add your environment variables here
            """;
    }
}