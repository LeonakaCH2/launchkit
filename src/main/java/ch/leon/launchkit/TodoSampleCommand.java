package ch.leon.launchkit;

import ch.leon.launchkit.generator.TodoSampleGenerator;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

@Command(
        name = "todo",
        description = "Generate a Todo sample application."
)
public class TodoSampleCommand implements Callable<Integer> {

    @Parameters(
            index = "0",
            description = "Name of the project to generate."
    )
    private String projectName;

    @Option(
            names = {"--backend"},
            description = "Backend type: springboot.",
            defaultValue = "springboot"
    )
    private String backend;

    @Option(
            names = {"--db"},
            description = "Database type: postgres, none.",
            defaultValue = "postgres"
    )
    private String database;

    @Option(
            names = {"--no-docker"},
            description = "Do not generate Docker setup."
    )
    private boolean noDocker = false;

    @Option(
            names = {"--no-ci"},
            description = "Do not generate GitHub Actions CI setup."
    )
    private boolean noCi = false;

    @Option(names = {"--db-port"}, description = "Host port for the database.", defaultValue = "5433")
    private int dbPort;

    @Override
    public Integer call() {
        try {
            boolean useDocker = !noDocker;
            boolean useCi = !noCi;

            new TodoSampleGenerator().generate(
                    projectName,
                    backend,
                    database,
                    useDocker,
                    useCi,
                    dbPort
            );
            return 0;
        } catch (Exception e) {
            System.err.println("Failed to create Todo sample project: " + e.getMessage());
            return 1;
        }
    }
}