package ch.leon.launchkit;

import ch.leon.launchkit.generator.SampleCommand;
import picocli.CommandLine.Command;

@Command(
        name = "launchkit",
        version = "launchkit 0.1.0",
        mixinStandardHelpOptions = true,
        description = "Generate developer project setups from the command line.",
        subcommands = {
                CreateCommand.class,
                SampleCommand.class,
                InfoCommand.class
        }
)
public class LaunchKitCommand {
}