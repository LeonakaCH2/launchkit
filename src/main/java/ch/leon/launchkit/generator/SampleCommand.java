package ch.leon.launchkit.generator;

import ch.leon.launchkit.TodoSampleCommand;
import picocli.CommandLine.Command;

@Command(
        name = "sample",
        description = "Generate sample applications.",
        subcommands = {
                TodoSampleCommand.class
        }
)
public class SampleCommand {
}