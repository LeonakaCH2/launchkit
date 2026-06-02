package ch.leon.launchkit;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

import ch.leon.launchkit.generator.ProjectGenerator;

@Command(
        name = "create",
        description = "Create a new developer project setup."
)
public class CreateCommand implements Callable<Integer> {

    @Parameters(
            index = "0",
            description = "Name of the project to generate."
    )
    private String projectName;

    @Option(
            names = {"--backend"},
            description = "Backend type: springboot, fastapi.",
            defaultValue = "springboot"
    )
    private String backend;

    @Option(
            names = {"--frontend"},
            description = "Frontend type: angular, react, none.",
            defaultValue = "angular"
    )
    private String frontend;

    @Option(
            names = {"--db"},
            description = "Database type: postgres, mysql, mongodb, none.",
            defaultValue = "postgres"
    )
    private String database;

    @Option(
            names = {"--docker"},
            description = "Generate Docker setup.",
            defaultValue = "true"
    )
    private boolean docker;

    @Option(
            names = {"--ci"},
            description = "Generate GitHub Actions CI setup.",
            defaultValue = "true"
    )
    private boolean ci;

    @Override
    public Integer call() {
        try {

            new ProjectGenerator().generate(
                    projectName,
                    backend,
                    frontend,
                    database,
                    docker,
                    ci
            );

            return 0;
        } catch (Exception e) {
            System.err.println("Failed to create project: " + e.getMessage());
            return 1;
        }
    }
}