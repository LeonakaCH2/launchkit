package ch.leon.launchkit.generator.backend;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SpringBootGenerator {

    public void generate(Path backendPath, String projectName) throws IOException {
        Files.createDirectories(backendPath.resolve("src/main/java/com/example/app"));
        Files.createDirectories(backendPath.resolve("src/main/resources"));
        Files.createDirectories(backendPath.resolve("src/test/java/com/example/app"));

        Files.writeString(backendPath.resolve("pom.xml"), createPom(projectName));
        Files.writeString(
                backendPath.resolve("src/main/java/com/example/app/Application.java"),
                createApplicationClass()
        );
        Files.writeString(
                backendPath.resolve("src/main/java/com/example/app/HealthController.java"),
                createHealthController()
        );
        Files.writeString(
                backendPath.resolve("src/main/resources/application.yml"),
                createApplicationYml()
        );
        Files.writeString(
                backendPath.resolve("src/test/java/com/example/app/ApplicationTests.java"),
                createTestClass()
        );
    }

    private String createPom(String projectName) {
        return """
                <project xmlns="http://maven.apache.org/POM/4.0.0"
                         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>

                    <parent>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter-parent</artifactId>
                        <version>3.3.5</version>
                        <relativePath/>
                    </parent>

                    <groupId>com.example</groupId>
                    <artifactId>%s-backend</artifactId>
                    <version>0.0.1-SNAPSHOT</version>

                    <properties>
                        <java.version>21</java.version>
                    </properties>

                    <dependencies>
                        <dependency>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-starter-web</artifactId>
                        </dependency>

                        <dependency>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-starter-validation</artifactId>
                        </dependency>

                        <dependency>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-starter-test</artifactId>
                            <scope>test</scope>
                        </dependency>
                        
                        <dependency>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-starter-data-jpa</artifactId>
                        </dependency>
                
                        <dependency>
                            <groupId>org.postgresql</groupId>
                            <artifactId>postgresql</artifactId>
                            <scope>runtime</scope>
                        </dependency>
                    </dependencies>

                    <build>
                        <plugins>
                            <plugin>
                                <groupId>org.springframework.boot</groupId>
                                <artifactId>spring-boot-maven-plugin</artifactId>
                            </plugin>
                        </plugins>
                    </build>
                </project>
                """.formatted(projectName);
    }

    private String createApplicationClass() {
        return """
                package com.example.app;

                import org.springframework.boot.SpringApplication;
                import org.springframework.boot.autoconfigure.SpringBootApplication;

                @SpringBootApplication
                public class Application {
                    public static void main(String[] args) {
                        SpringApplication.run(Application.class, args);
                    }
                }
                """;
    }

    private String createHealthController() {
        return """
                package com.example.app;

                import org.springframework.web.bind.annotation.GetMapping;
                import org.springframework.web.bind.annotation.RestController;

                import java.util.Map;

                @RestController
                public class HealthController {

                    @GetMapping("/api/health")
                    public Map<String, String> health() {
                        return Map.of("status", "UP");
                    }
                }
                """;
    }

    private String createApplicationYml() {
        return """
            server:
              port: 8080

            spring:
              application:
                name: backend

              datasource:
                url: ${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5433/app_db}
                username: ${SPRING_DATASOURCE_USERNAME:app_user}
                password: ${SPRING_DATASOURCE_PASSWORD:app_password}
                driver-class-name: org.postgresql.Driver

              jpa:
                hibernate:
                  ddl-auto: update
                show-sql: true
                properties:
                  hibernate:
                    format_sql: true
            """;
    }

    private String createTestClass() {
        return """
                package com.example.app;

                import org.junit.jupiter.api.Test;
                import org.springframework.boot.test.context.SpringBootTest;

                @SpringBootTest
                class ApplicationTests {

                    @Test
                    void contextLoads() {
                    }
                }
                """;
    }
}